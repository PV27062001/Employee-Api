package com.employeeApplication.shared;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class Properties {
    @Value("${secret.key}")
    private String secretKey;

    @Value("${initial.user}")
    private String userName;
    @Value("${initial.user.secure}")
    private String userPassword;
    @Value("${initial.role}")
    private String userRole;
}
