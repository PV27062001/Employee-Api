package com.employeeApplication.department;


import com.employeeApplication.exception.EmployeeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public List<Department> getAllDepartment(){
        return departmentRepository.findAll();
    }

    public Department saveDepartment(DepartmentRequest departmentRequest){
            if(DepartmentName.isValidDepartmentName(departmentRequest.getDepartmentName().name())) {
                Department department = Department.builder()
                        .departmentName(departmentRequest.getDepartmentName())
                        .description(departmentRequest.getDescription())
                        .build();
                return departmentRepository.save(department);
            }
        else {
            throw new EmployeeException("Department not found");
        }
    }

    public Department getDepartmentByName(String name){
        return departmentRepository.findAll()
                .parallelStream()
                .filter(department -> department.getDepartmentName().equals(DepartmentName.valueOf(name.toUpperCase())))
                .findFirst()
                .orElseThrow(() -> new EmployeeException("No department Found"));
    }

}
