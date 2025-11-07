package com.employeeApplication.auth;

import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRoleRequest {
    private String name;
    private List<String> roles;
}
