package com.employeeApplication.interview.interviewdepartmentstatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewDepartmentStatusRepository extends JpaRepository<InterviewDepartmentStatus,Integer> {

}
