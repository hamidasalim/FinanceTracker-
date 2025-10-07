package com.fintech.enterprise.config;

import com.fintech.enterprise.model.Department;
import com.fintech.enterprise.model.Role;
import com.fintech.enterprise.model.User;
import com.fintech.enterprise.repository.DepartmentRepository;
import com.fintech.enterprise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class InitialDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public InitialDataLoader(UserRepository userRepository, DepartmentRepository departmentRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only run if the database is currently empty of users
        if (userRepository.count() == 0) {
            System.out.println("--- Seeding Initial Database Data ---");

            // 1. Create Departments
            Department techDept = departmentRepository.save(
                    new Department(null, "Technology", new BigDecimal("100000.00"), BigDecimal.ZERO)
            );
            Department hrDept = departmentRepository.save(
                    new Department(null, "Human Resources", new BigDecimal("50000.00"), BigDecimal.ZERO)
            );
            Department salesDept = departmentRepository.save(
                    new Department(null, "Sales", new BigDecimal("150000.00"), BigDecimal.ZERO)
            );

            // 2. Create Users (passwords are 'password' for all)

            // ADMIN User
            User admin = new User(
                    null,
                    "admin",
                    passwordEncoder.encode("password"),
                    Role.ADMIN
            );
            admin.setDepartment(techDept);

            // MANAGER User
            User manager = new User(
                    null,
                    "manager",
                    passwordEncoder.encode("password"),
                    Role.MANAGER
            );
            manager.setDepartment(techDept);

            // EMPLOYEE User
            User employee = new User(
                    null,
                    "employee",
                    passwordEncoder.encode("password"),
                    Role.EMPLOYEE
            );
            employee.setDepartment(hrDept);

            userRepository.saveAll(List.of(admin, manager, employee));

            System.out.println("--- Seeding Complete: 3 Users and 3 Departments Created ---");
        }
    }
}
