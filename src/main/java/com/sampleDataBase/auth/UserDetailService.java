package com.sampleDataBase.auth;

import com.sampleDataBase.exception.UserNameAuthenticationException;
import com.sampleDataBase.security.JWTConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        return jwtConfiguration.generateToken(user);
    }

    public List<String> getAllUserName() {
        return userDetailProviderRepository.getAllUserNames();
    }

    public String updateRoleForUsersByUserName(UpdateRoleRequest updateRoleRequest) throws UserNameAuthenticationException {
       Users user =  userDetailProviderRepository.findUserByUserName(updateRoleRequest.getName());
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
}
