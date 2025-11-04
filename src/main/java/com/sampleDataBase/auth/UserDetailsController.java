package com.sampleDataBase.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserDetailsController {

    private final UserDetailService userDetailService;

    @PostMapping("/save-user")
    public Users saveUsers(@RequestBody Users users){
        return userDetailService.saveUsers(users);
    }
}
