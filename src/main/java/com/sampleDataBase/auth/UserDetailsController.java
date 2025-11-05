package com.sampleDataBase.auth;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserDetailsController {

    private final UserDetailService userDetailService;

    @SneakyThrows
    @PostMapping("/save-user")
    public Users saveUsers(@RequestBody UserRequest users){
        return userDetailService.saveUsers(users);
    }

    @PostMapping("/login")
    public String checkLoginIsValid(@RequestBody UserRequest user){
        return userDetailService.verifyUsers(user);
    }
}
