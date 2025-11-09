package com.employeeApplication.employee;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/employee")
@CrossOrigin("*")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @GetMapping("/getAll")
    public List<EmployeeResponse> getAll(){
        return employeeService.getAllEmployeeResponse();
    }

    @GetMapping(value = "/getEmployeeByPages",params = {"pageNo","pageSize","sortBy","isAscending"})
    public Page<Employee> getEmployeeByPagination(
            @RequestParam(name = "pageNo") int pageNo,
            @RequestParam(name = "pageSize") int pageSize,
            @RequestParam(name = "sortBy") String sortBy,
            @RequestParam(name = "isAscending") boolean isAscending
    ){
        Sort sort = isAscending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable page = PageRequest.of(pageNo,pageSize,sort);
        return employeeService.getEmployeeByPage(page);
    }

    @PutMapping(value = "/update-name-status/{id}",params = {"status","name"})
    public Employee updateEmployeeNameAndStatus(@PathVariable String id,@RequestParam boolean status, @RequestParam String name){
        return employeeService.updateEmployeeName(id,status,name);
    }

    @PutMapping(value = "/assign-manager-employee",params = {"id","managerName"})
    public Employee assignOrUpdateManagerToEmployee(@RequestParam String id,@RequestParam String managerName){
        return employeeService.saveManagerToEmployee(id,managerName);
    }

    @PatchMapping(value = "/employee/{id}/bonus")
    public Employee updateSalaryAndBonusForGivenEmployee(@PathVariable String id){
        return employeeService.calculateBonusAndUpdateIt(id);
    }


    /// learning purpose to get the csrf Token
    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest httpServletRequest){
        return (CsrfToken) httpServletRequest.getAttribute("_csrf");
    }

}
