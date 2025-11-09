package com.employeeApplication.department;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentResponse {

    private int id;

    @Enumerated(EnumType.STRING)
    private DepartmentName departmentName;

    private String description;

    private Double baseSalary;
}
