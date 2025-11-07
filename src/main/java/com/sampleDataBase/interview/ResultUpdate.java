package com.sampleDataBase.interview;


import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultUpdate {
    private String userName;
    private Map<String,Boolean> update;
}