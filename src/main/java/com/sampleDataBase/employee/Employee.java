package com.sampleDataBase.employee;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sampleDataBase.department.Department;
import com.sampleDataBase.manager.Manager;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = {"manager","colleagues"})
public class Employee {

    @Id
    private String employeeId;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Department dept;

    private double salary;
    private double bonus;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "manager_id")
    @JsonBackReference
    private Manager manager;

    @ManyToMany
//    @JoinTable(name = "employee_colleagues",
//    joinColumns = @JoinColumn(name = "employee_id"),
//    inverseJoinColumns = @JoinColumn(name = "colleague_id"))
    private List<Employee> colleagues;

    private boolean active;

    @CreatedDate
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
