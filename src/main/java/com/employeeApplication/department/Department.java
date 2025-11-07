package com.employeeApplication.department;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.employeeApplication.employee.Employee;
import com.employeeApplication.interview.interviewdepartmentstatus.InterviewDepartmentStatus;
import com.employeeApplication.manager.Manager;
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
