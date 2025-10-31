package com.sampleDataBase.manager;


import com.sampleDataBase.department.DepartmentName;
import com.sampleDataBase.exception.EmployeeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.ls.LSInput;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ManagerService {

    private final ManagerRepository managerRepository;

    public List<Manager> getAllManager(){
        return managerRepository.findAll();
    }

    public Manager saveManager(ManagerRequest managerRequest){
        Manager manager = Manager.builder()
                .name(managerRequest.getName())
                .departmentName(DepartmentName.valueOf(managerRequest.getDepartmentName().toUpperCase()))
                .build();
        return managerRepository.save(manager);
    }

    public Manager getManagerByName(String name){
        return managerRepository.findAll()
                .stream()
                .filter(manager -> manager.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new EmployeeException("No Such Manager found"));
    }


}
