package com.employeeApplication.interview;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InterviewRequest {
    private String userName;
    private List<String> departmentName;
}
