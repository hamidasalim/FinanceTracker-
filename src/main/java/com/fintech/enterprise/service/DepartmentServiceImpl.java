package com.fintech.enterprise.service;

import com.fintech.enterprise.model.Department;
import com.fintech.enterprise.model.User;
import com.fintech.enterprise.repository.DepartmentRepository;
import com.fintech.enterprise.repository.UserRepository;
import com.fintech.enterprise.service.dto.BudgetOverviewDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Added for member management

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository; // Needed to manage User relationship

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    // --- CRUD Operations ---

    @Override
    public Department createDepartment(Department department) {
        if (department.getSpentAmount() == null) {
            department.setSpentAmount(BigDecimal.ZERO);
        }
        return departmentRepository.save(department);
    }

    @Override
    public List<Department> findAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Optional<Department> findDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public Department updateDepartment(Long id, Department departmentDetails) {
        Department department = findDepartmentById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + id));

        // Only allow updating name and budget. Spent amount is managed by expense approvals.
        if (departmentDetails.getName() != null) {
            department.setName(departmentDetails.getName());
        }
        if (departmentDetails.getYearlyBudget() != null) {
            department.setYearlyBudget(departmentDetails.getYearlyBudget());
        }

        return departmentRepository.save(department);
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    // --- Membership Operations ---

    @Override
    @Transactional
    public Department addMemberToDepartment(Long departmentId, Long userId) {
        Department department = findDepartmentById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + departmentId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        user.setDepartment(department);
        userRepository.save(user);

        return department;
    }

    @Override
    @Transactional
    public Department removeMemberFromDepartment(Long departmentId, Long userId) {
        Department department = findDepartmentById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + departmentId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        if (!departmentId.equals(user.getDepartment().getId())) {
            throw new IllegalArgumentException("User ID " + userId + " does not belong to Department ID " + departmentId);
        }

        user.setDepartment(null);
        userRepository.save(user);

        return department;
    }

    // --- Reporting ---

    @Override
    public List<BudgetOverviewDTO> getBudgetOverview() {
        return departmentRepository.findAll().stream()
                .map(dept -> {
                    BigDecimal remaining = dept.getYearlyBudget().subtract(dept.getSpentAmount());
                    BigDecimal remainingPercent = BigDecimal.ZERO;

                    if (dept.getYearlyBudget().compareTo(BigDecimal.ZERO) > 0) {
                        remainingPercent = remaining
                                .divide(dept.getYearlyBudget(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100));
                    }

                    BigDecimal spentPercent = dept.getYearlyBudget().compareTo(BigDecimal.ZERO) > 0
                            ? dept.getSpentAmount().divide(dept.getYearlyBudget(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                            : BigDecimal.ZERO;

                    return new BudgetOverviewDTO(
                            dept.getName(),
                            dept.getYearlyBudget(),
                            dept.getSpentAmount(),
                            remaining,
                            remainingPercent,
                            spentPercent
                    );
                })
                .collect(Collectors.toList());
    }
}
