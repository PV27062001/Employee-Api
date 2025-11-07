package com.employeeApplication.department;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/getAll-department")
    public List<Department> getAllDepartment(){
        return departmentService.getAllDepartment();
    }

    @PostMapping("/save-department")
    public Department saveDepartment(@RequestBody DepartmentRequest department){
        return departmentService.saveDepartment(department);
    }
}
