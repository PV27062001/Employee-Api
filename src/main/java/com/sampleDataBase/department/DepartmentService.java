package com.sampleDataBase.department;


import com.sampleDataBase.exception.EmployeeException;
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
            throw new EmployeeException("Deaprtment not found");
        }
    }

    public Department getDepartmentByName(String name){
        return departmentRepository.findAll()
                .stream()
                .filter(department -> department.getDepartmentName().equals(DepartmentName.valueOf(name)))
                .findFirst()
                .orElseThrow(() -> new EmployeeException("No department Found"));
    }

}
