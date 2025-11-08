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

    public double setBaseSalaryForDepartment(String departmentName){
        double baseSalary;
        switch (DepartmentName.valueOf(departmentName)){
            case HR -> baseSalary = 5000.0;
            case TECH -> baseSalary = 1000000.0;
            case SALES -> baseSalary = 12000.0;
            case FINANCE -> baseSalary = 800000.0;
            default -> baseSalary = 10000;
        }
        return baseSalary;
    }

   public double getBonusPercentageByDepartment(Department department) {
        double bonusPercentage;
        switch (department.getDepartmentName()){
            case HR -> bonusPercentage = 5;
            case TECH -> bonusPercentage = 10;
            case SALES -> bonusPercentage = 12;
            case FINANCE -> bonusPercentage = 8;
            default -> bonusPercentage = 1;
        }
        return bonusPercentage;
    }


}
