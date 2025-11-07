package com.employeeApplication.interview;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface InterviewRepository extends JpaRepository<Interview, Integer> {
    @Query(value = "select * from interview where user_name = :userName",nativeQuery = true)
    Interview findInterviewByName(@Param("userName") String userName);
}
