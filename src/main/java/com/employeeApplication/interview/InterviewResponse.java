package com.employeeApplication.interview;

import lombok.*;

import java.util.Date;
import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InterviewResponse {
    private String userName;
    private Map<String,String> statusByDepartment;
    private Date appliedDate;
}
