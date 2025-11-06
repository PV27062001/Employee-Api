package com.sampleDataBase.interview;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultUpdate {
    private String userName;
    private String departmentName;
    private boolean result;
}
