package com.employeeApplication;

import com.employeeApplication.shared.InitialDataDump;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@EnableJpaAuditing
@EnableTransactionManagement
@RequiredArgsConstructor
public class EmployeeApplication implements CommandLineRunner {
	private final InitialDataDump initialDataDump;
	public static void main(String[] args) {
		SpringApplication.run(EmployeeApplication.class, args);
	}

	@Override
	public void run(String... args) {
		initialDataDump.dataDump();
	}
}
