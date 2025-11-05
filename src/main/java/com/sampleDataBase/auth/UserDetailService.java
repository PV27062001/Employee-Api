package com.sampleDataBase.auth;

import com.sampleDataBase.exception.UserNameAuthenticationException;
import com.sampleDataBase.security.JWTConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
                    .build();
            return userDetailProviderRepository.save(user);
        }
        else throw new UserNameAuthenticationException("UserName Already taken try with different username");
    }

    public boolean checkIfUserNameISAlreadyPresent(String userName){
       return userDetailProviderRepository.getAllUserNames().parallelStream()
               .anyMatch(name -> name.equals(userName));
    }

    public String verifyUsers(UserRequest userRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getUserName(),userRequest.getPassword()));
        if(authentication.isAuthenticated()){
            Users user = userDetailProviderRepository.findUserByUserName(userRequest.getUserName());
            return jwtConfiguration.generateToken(user);
        }else {
            return "failed to generate token";
        }

    }
}
