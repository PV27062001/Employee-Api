package com.sampleDataBase.employee;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class EmployeeRequest {
    private String name;
    private String departmentName;
    private double salary;
}