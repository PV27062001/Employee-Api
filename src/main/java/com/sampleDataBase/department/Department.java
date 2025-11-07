package com.sampleDataBase.department;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sampleDataBase.employee.Employee;
import com.sampleDataBase.interview.interviewdepartmentstatus.InterviewDepartmentStatus;
import com.sampleDataBase.manager.Manager;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
//    @JsonIgnore
    private DepartmentName departmentName;

    private String description;

    @OneToMany
    @Cascade(CascadeType.ALL)
    private List<Manager> managers;

    @OneToMany
    @Cascade(CascadeType.ALL)
    private List<Employee> employees;

    @OneToMany(mappedBy = "department")
    @JsonManagedReference
    private List<InterviewDepartmentStatus> interviewStatuses;
}
