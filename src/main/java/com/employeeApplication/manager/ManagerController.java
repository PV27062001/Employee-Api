package com.employeeApplication.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manager")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping("/getAll-manager")
    public List<Manager> getAllManager(){
        return managerService.getAllManager();
    }

    @PostMapping("/save-manager")
    public Manager saveManager(@RequestBody ManagerRequest manager){
        return managerService.saveManager(manager);
    }
}
