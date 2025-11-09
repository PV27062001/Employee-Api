package com.employeeApplication.auth;

import com.employeeApplication.auth.refreshtoken.RefreshToken;
import com.employeeApplication.auth.refreshtoken.RefreshTokenService;
import com.employeeApplication.exception.UserNameAuthenticationException;
import com.employeeApplication.security.JWTConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final RefreshTokenService refreshTokenService;

    public String isUserNamePresent(String userName) throws UserNameAuthenticationException{
        return getAllUserName().stream()
                .filter(name -> name.equalsIgnoreCase(userName))
                .findFirst()
                .orElseThrow(() -> new UserNameAuthenticationException("UserName not found on the registry"));
    }

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
        Users user = getUserByUserName(userRequest.getUserName());
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



    @SneakyThrows
    public Map<String, String> getAccessTokenAndRefreshToken(UserRequest userRequest) {
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("access_token", verifyUsers(userRequest));
        tokenMap.put("refresh_token", getRefreshToken(getUserByUserName(userRequest.getUserName())).getToken());
        return tokenMap;
    }

    private RefreshToken getRefreshToken(Users user) {
        return refreshTokenService.createRefreshToken(user.getUserName());
    }

    public Map<String, String> getNewToken(Map<String, String> request) throws UserNameAuthenticationException {
        String requestToken = request.get("refresh_token");
        if (requestToken == null || requestToken.isBlank()) {
            throw new UserNameAuthenticationException("Missing refresh token in request");
        }

        RefreshToken refreshToken = refreshTokenService.findByToken(requestToken)
                .orElseThrow(() -> new UserNameAuthenticationException("Invalid refresh token"));

        if (refreshTokenService.isExpired(refreshToken)) {
            refreshTokenService.deleteByUsername(refreshToken.getUserName());
            throw new UserNameAuthenticationException("Refresh token expired. Please log in again.");
        }

        Users user = getUserByUserName(refreshToken.getUserName());
        String newAccessToken = jwtConfiguration.generateAccessToken(user);

        refreshTokenService.deleteByUsername(user.getUserName());
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getUserName());

        return Map.of(
                "access_token", newAccessToken,
                "refresh_token", newRefreshToken.getToken() 
        );
    }

    public String logout(Map<String,String> request){
        String userName = request.get("userName");
        refreshTokenService.deleteByUsername(userName);
        return "logged out successfully";
    }

    private Users getUserByUserName(String userName) {
        return userDetailProviderRepository.findUserByUserName(userName);
    }
}
