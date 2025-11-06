package com.sampleDataBase.auth;


import com.sampleDataBase.exception.UserNameAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.sax.SAXResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String,String>> checkLoginIsValid(@RequestBody UserRequest user) {
        return ResponseEntity.ok(userDetailService.getAccessTokenAndRefreshToken(user));
    }

    @SneakyThrows
    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String,String>> getRefreshToken(@RequestBody Map<String,String> request){
        return ResponseEntity.ok(userDetailService.getNewToken(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all")
    public List<String> getAllUserNames(){
        return userDetailService.getAllUserName();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody Map<String,String> request){
        return ResponseEntity.ok(userDetailService.logout(request));
    }

    @SneakyThrows
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/update-role/for-users")
    public String updateRoleForUsersByUserName(@RequestBody UpdateRoleRequest  updatedRequest){
        return userDetailService.updateRoleForUsersByUserName(updatedRequest);
    }


}
