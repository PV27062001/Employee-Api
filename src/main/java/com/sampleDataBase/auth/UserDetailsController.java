package com.sampleDataBase.auth;


import com.sampleDataBase.exception.UserNameAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @SneakyThrows
    public String checkLoginIsValid(@RequestBody UserRequest user) {
        return userDetailService.verifyUsers(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all")
    public List<String> getAllUserNames(){
        return userDetailService.getAllUserName();
    }

    @SneakyThrows
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/update-role/for-users")
    public String updateRoleForUsersByUserName(@RequestBody UpdateRoleRequest  updatedRequest){
        return userDetailService.updateRoleForUsersByUserName(updatedRequest);
    }


}
