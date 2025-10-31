package com.sampleDataBase.employee;

import com.sampleDataBase.department.Department;
import com.sampleDataBase.department.DepartmentService;
import com.sampleDataBase.manager.Manager;
import com.sampleDataBase.manager.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeService {


    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;
    private final ManagerService managerService;


    public List<Employee> getAllEmployee(){
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(String id){
        return (getAllEmployee().stream()
                .filter(employee -> employee.getEmployeeId().equals(id.toUpperCase()))
                .findFirst()).orElseThrow();
    }
    private String idGenerator(){
        String initial = "EMP";
        int id = getAllEmployee().size() + 1;
        return initial + id;
    }

    public Department getDepartmentByName(String name){
        return departmentService.getDepartmentByName(name);
    }

    public Manager getManagerByName(String name){
        return managerService.getManagerByName(name);
    }

    @Transactional
    public Employee saveEmployee(EmployeeRequest employeeRequest){
       Employee employee = Employee.builder()
                .employeeId(idGenerator())
                .name(employeeRequest.getName())
                .dept(getDepartmentByName(employeeRequest.getDepartmentName().toUpperCase()))
                .salary(employeeRequest.getSalary())
//                .manager(getManagerByName(employeeRequest.getManagerName().toLowerCase()))
                .build();
        return employeeRepository.save(employee);
    }

    public Employee updateEmployeeName(String id, boolean status, String name){
        Employee employee = getEmployeeById(id.toUpperCase());
        employee.setActive(Boolean.TRUE.equals(status));
        employee.setName(name.isBlank()? employee.getName() : name);
       return employeeRepository.save(employee);
    }

    public Page<Employee> getEmployeeByPage(Pageable pageable){
        return employeeRepository.findAll(pageable);
    }

    public Employee saveManagerToEmployee(String id,String managerName){
        Employee employee = getEmployeeById(id.toUpperCase());
        employee.setManager(getManagerByName(managerName));
        return employeeRepository.save(employee);
    }


    public Employee calculateBonusAndUpdateIt(String id){
        Employee employee = getEmployeeById(id.toUpperCase());
        employee.setSalary(calculateBonus(employee.getDept(),employee.getSalary()));
        employee.setBonus(getBonusPercentageByDepartment(employee.getDept()));
        return employeeRepository.save(employee);
    }

    private Double calculateBonus(Department department,Double salary){
        double bonusPercentage;
        bonusPercentage = getBonusPercentageByDepartment(department);
        return salary + (salary * bonusPercentage/100);
    }

    private static double getBonusPercentageByDepartment(Department department) {
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