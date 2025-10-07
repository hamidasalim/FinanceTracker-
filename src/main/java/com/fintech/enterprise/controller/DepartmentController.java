package com.fintech.enterprise.controller;

import com.fintech.enterprise.model.Department;
import com.fintech.enterprise.service.DepartmentService;
import com.fintech.enterprise.service.dto.BudgetOverviewDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // --- CRUD Endpoints (Admin only) ---

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        Department createdDepartment = departmentService.createDepartment(department);
        return new ResponseEntity<>(createdDepartment, HttpStatus.CREATED);
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.findAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        return departmentService.findDepartmentById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @RequestBody Department departmentDetails) {
        Department updatedDepartment = departmentService.updateDepartment(id, departmentDetails);
        return ResponseEntity.ok(updatedDepartment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    // --- Membership Endpoints (Admin only) ---

    @PostMapping("/{departmentId}/members/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Department> addMemberToDepartment(@PathVariable Long departmentId, @PathVariable Long userId) {
        Department updatedDepartment = departmentService.addMemberToDepartment(departmentId, userId);
        return ResponseEntity.ok(updatedDepartment);
    }

    @DeleteMapping("/{departmentId}/members/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Department> removeMemberFromDepartment(@PathVariable Long departmentId, @PathVariable Long userId) {
        Department updatedDepartment = departmentService.removeMemberFromDepartment(departmentId, userId);
        return ResponseEntity.ok(updatedDepartment);
    }

    // --- Reporting Endpoint (Admin/Manager only) ---

    @GetMapping("/budget-overview")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<BudgetOverviewDTO>> getBudgetOverview() {
        List<BudgetOverviewDTO> overview = departmentService.getBudgetOverview();
        return ResponseEntity.ok(overview);
    }
}
