package com.sampleDataBase.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService {
    private final UserDetailProviderRepository userDetailProviderRepository;
    private final BCryptPasswordEncoder encoder;

    public Users saveUsers(Users user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userDetailProviderRepository.save(user);
    }

}
