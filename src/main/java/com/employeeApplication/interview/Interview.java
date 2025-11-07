package com.employeeApplication.interview;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.employeeApplication.interview.interviewdepartmentstatus.InterviewDepartmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private String userName;

    private boolean isAppliedAny;

    @OneToMany(mappedBy = "interview",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<InterviewDepartmentStatus> departmentStatuses;

    @CreatedDate
    private Date createdAt;
}
