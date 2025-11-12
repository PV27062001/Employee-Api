package com.employeeApplication.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {

    @Query("SELECT e.name FROM Employee e WHERE e.name = :name")
    Optional<String> checkIsEmployeePresent(@Param("name") String name);


    @Query(value = "select * from public.employee where name = :name",nativeQuery = true)
    Employee getEmployeeByNAme(@Param("name") String name);
}
