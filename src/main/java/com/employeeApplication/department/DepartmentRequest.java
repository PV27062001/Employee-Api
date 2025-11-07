package com.employeeApplication.department;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentRequest {

    private DepartmentName departmentName;

    private String description;
}
