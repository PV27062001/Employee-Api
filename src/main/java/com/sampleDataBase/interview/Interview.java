package com.sampleDataBase.interview;


import com.sampleDataBase.auth.Users;
import com.sampleDataBase.department.Department;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private String userName;

    private boolean isAppliedAny;

    private boolean isGotOffer;

    @OneToMany
    private List<Department> department;
}
