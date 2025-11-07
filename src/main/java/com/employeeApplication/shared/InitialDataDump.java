package com.employeeApplication.shared;

import com.employeeApplication.auth.UserDetailProviderRepository;
import com.employeeApplication.auth.Users;
import com.employeeApplication.department.Department;
import com.employeeApplication.department.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.employeeApplication.department.DepartmentName.*;

@Component
@RequiredArgsConstructor
public class InitialDataDump {

    private final DepartmentRepository departmentRepository;
    private final UserDetailProviderRepository userDetailProviderRepository;
    private final BCryptPasswordEncoder encoder;
    private final Properties properties;
    public void dataDump(){
        createDefaultAdminIfNotExists();
        createDefaultDepartmentsIfNotExists();
    }

    private void createDefaultDepartmentsIfNotExists() {
        List<Department> defaultDepartments = List.of(
                Department.builder()
                        .departmentName(HR)
                        .description("hr department")
                        .build(),
                Department.builder()
                        .departmentName(SALES)
                        .description("sales department")
                        .build(),
                Department.builder()
                        .departmentName(TECH)
                        .description("tech department")
                        .build()
        );

        for (Department dept : defaultDepartments) {
            departmentRepository.findByDepartmentName(dept.getDepartmentName())
                    .ifPresentOrElse(
                            existing -> System.out.println("Department " + existing.getDepartmentName() + " already exists."),
                            () -> {
                                departmentRepository.save(dept);
                                System.out.println("Created department: " + dept.getDepartmentName());
                            }
                    );
        }
    }

    private void createDefaultAdminIfNotExists() {
        String defaultAdminUsername = properties.getUserName();

        boolean exists = userDetailProviderRepository.getAllUserNames()
                .stream()
                .anyMatch(name -> name.equalsIgnoreCase(defaultAdminUsername));

        if (!exists) {
            Users admin = Users.builder()
                    .userName(defaultAdminUsername)
                    .password(encoder.encode(properties.getUserPassword()))
                    .roles(List.of(properties.getUserRole()))
                    .build();

            userDetailProviderRepository.save(admin);
            System.out.println("Default user created successfully!");
        } else {
            System.out.println("Default user already exists. Skipping creation.");
        }
    }
}
