package com.employeeApplication.department;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/department")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/all")
    public List<DepartmentResponse> getAllDepartment(){
        return departmentService.getAllDepartmentResponse();
    }

    @PostMapping("/save-department")
    public Department saveDepartment(@RequestBody DepartmentRequest department){
        return departmentService.saveDepartment(department);
    }
}
