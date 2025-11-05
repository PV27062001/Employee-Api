package com.sampleDataBase.auth;

import com.sampleDataBase.exception.UserNameAuthenticationException;
import com.sampleDataBase.security.JWTConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserDetailService {
    private final UserDetailProviderRepository userDetailProviderRepository;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JWTConfiguration jwtConfiguration;

    public Users saveUsers(UserRequest userRequest) throws UserNameAuthenticationException {
        if(!checkIfUserNameISAlreadyPresent(userRequest.getUserName())) {
            Users user = Users.builder()
                    .userName(userRequest.getUserName())
                    .password(encoder.encode(userRequest.getPassword()))
                    .roles(List.of("USER"))
                    .build();
            return saveUsers(user);
        }
        else throw new UserNameAuthenticationException("UserName Already taken try with different username");
    }

    private Users saveUsers(Users user) {
        return userDetailProviderRepository.save(user);
    }

    public boolean checkIfUserNameISAlreadyPresent(String userName){
       return userDetailProviderRepository.getAllUserNames().parallelStream()
               .anyMatch(name -> name.equals(userName));
    }

    public String verifyUsers(UserRequest userRequest) throws UserNameAuthenticationException {
        Users user = userDetailProviderRepository.findUserByUserName(userRequest.getUserName());
        if (user == null) {
            throw new UserNameAuthenticationException("User not found. Please register first.");
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getUserName(), userRequest.getPassword()));
        if (!authentication.isAuthenticated()) {
            throw new UserNameAuthenticationException("Invalid username or password.");
        }
        return jwtConfiguration.generateAccessToken(user);
    }

    public List<String> getAllUserName() {
        return userDetailProviderRepository.getAllUserNames();
    }

    public String updateRoleForUsersByUserName(UpdateRoleRequest updateRoleRequest) throws UserNameAuthenticationException {
       Users user = getUserByUserName(updateRoleRequest.getName());
        if (user == null) {
            throw new UserNameAuthenticationException("User not found. Please register first.");
        }
        List<String> upperCasedRoles= user.getRoles()
                .stream()
                .map(String::toUpperCase)
                .toList();
        Set<String> updatedRoles = new HashSet<>(upperCasedRoles);
        updatedRoles.addAll(updateRoleRequest.getRoles());
        user.setRoles(new ArrayList<>(updatedRoles));
        saveUsers(user);
        return "roles updated successfully";
    }

    private Users getUserByUserName(String userName) {
        return userDetailProviderRepository.findUserByUserName(userName);
    }

    @SneakyThrows
    public Map<String, String> getAccessTokenAndRefreshToken(UserRequest userRequest) {
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("access_token", verifyUsers(userRequest));
        tokenMap.put("refresh_token", getRefreshToken(getUserByUserName(userRequest.getUserName())));
        return tokenMap;
    }

    private String getRefreshToken(Users user) {
       return jwtConfiguration.generateRefreshToken(user);
    }

    public Map<String, String> getNewToken(Map<String, String> request) throws UserNameAuthenticationException {
        String refreshToken = request.get("refresh_token");
        if(refreshToken.isBlank()){
            throw new UserNameAuthenticationException("Missing refresh token in the header");
        }
        String userName = jwtConfiguration.extractUserName(refreshToken);
        Users user = getUserByUserName(userName);
        if(!jwtConfiguration.validateToken(refreshToken, new UserNameProvider(user))){
            throw new UserNameAuthenticationException("Invalid or expired refresh token,Please Login Again  ");
        }
        String newAccessToken = jwtConfiguration.generateAccessToken(user);
        return Map.of("access_token",newAccessToken);
    }

}
