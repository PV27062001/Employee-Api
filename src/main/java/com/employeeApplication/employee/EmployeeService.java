package com.employeeApplication.employee;

import com.employeeApplication.department.Department;
import com.employeeApplication.department.DepartmentService;
import com.employeeApplication.interview.ResultUpdate;
import com.employeeApplication.manager.Manager;
import com.employeeApplication.manager.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


    public String saveEmployee(ResultUpdate resultUpdate){
        String departmentName = "";
        for(Map.Entry<String,String> status: resultUpdate.getStatusByDepartment().entrySet()){
            if(status.getValue().equals("ACCEPTED")){
                departmentName = status.getKey();
            }
        }

        Employee employee = Employee.builder()
                .employeeId(idGenerator())
                .name(resultUpdate.getUserName())
                .dept(getDepartmentByName(departmentName))
                .salary(departmentService.setBaseSalaryForDepartment(departmentName))
                .build();
         employeeRepository.save(employee);
         return "employee saved successfully";
    }

    public Employee saveManagerToEmployee(String id,String managerName){
        Employee employee = getEmployeeById(id.toUpperCase());
        employee.setManager(getManagerByName(managerName));
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

    public Employee calculateBonusAndUpdateIt(String id){
        Employee employee = getEmployeeById(id.toUpperCase());
        employee.setSalary(calculateBonus(employee.getDept(),employee.getSalary()));
        employee.setBonus(departmentService.getBonusPercentageByDepartment(employee.getDept()));
        return employeeRepository.save(employee);
    }

    private Double calculateBonus(Department department,Double salary){
        double bonusPercentage;
        bonusPercentage = departmentService.getBonusPercentageByDepartment(department);
        return salary + (salary * bonusPercentage/100);
    }

    public List<EmployeeResponse> getAllEmployeeResponse(){
        List<Employee> getAll = getAllEmployee();
        List<EmployeeResponse> result = new ArrayList<>();
        for(Employee  employee : getAll){
            result.add(buildEmployeeResponse(employee));
        }
        return result;
    }

    public EmployeeResponse buildEmployeeResponse(Employee employee){

        String managerName = employee.getManager().getName();
        return EmployeeResponse.builder()
                .employeeId(employee.getEmployeeId())
                .name(employee.getName())
                .bonus(employee.getBonus())
                .active(employee.isActive())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .managerName(managerName.isEmpty()?"":managerName)
                .departmentName(employee.getDept().getDepartmentName().name())
                .salary(employee.getSalary())
                .build();
    }

}