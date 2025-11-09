package com.employeeApplication.employee;

import com.employeeApplication.department.Department;
import lombok.*;


import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class EmployeeResponse {

    private String employeeId;

    private String name;

    private String departmentName;

    private double salary;

    private double bonus;

    private boolean active;

    private String managerName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
