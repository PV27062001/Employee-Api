package com.sampleDataBase.manager;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sampleDataBase.department.DepartmentName;
import com.sampleDataBase.employee.Employee;
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
@ToString(exclude = "employees")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private DepartmentName departmentName;

    @OneToMany(mappedBy = "manager")
    @Cascade(CascadeType.ALL)
    @JsonManagedReference
    private List<Employee> employees;
}
