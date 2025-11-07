package com.employeeApplication.department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DepartmentRepository extends JpaRepository<Department,Integer> {

    @Query("select d from Department  d where d.departmentName = :name")
    Optional<Department> findByDepartmentName(@Param("name") DepartmentName  name);
}
