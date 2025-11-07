package com.sampleDataBase.interview.interviewdepartmentstatus;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sampleDataBase.department.Department;
import com.sampleDataBase.interview.Interview;
import com.sampleDataBase.interview.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class InterviewDepartmentStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @ManyToOne
    @JsonBackReference
    private Interview interview;

    @ManyToOne
//    @JsonBackReference
    @JsonIgnoreProperties({"interviewStatuses", "managers", "employees"})
    private Department department;

    @Enumerated(EnumType.STRING)
    private Status status;

    @CreatedDate
    private Date createdAt;
}
