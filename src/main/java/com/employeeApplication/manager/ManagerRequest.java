package com.employeeApplication.manager;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManagerRequest {

    private String name;

    private String departmentName;
}
