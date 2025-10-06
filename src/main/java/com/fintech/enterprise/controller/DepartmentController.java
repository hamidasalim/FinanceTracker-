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

/**
 * REST Controller for managing Department entities.
 * Base path: /api/departments
 */
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // --- CRUD Endpoints (Admin only) ---

    /**
     * Creates a new department.
     * POSTMAN TEST: POST {{baseURL}}/api/departments
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        Department createdDepartment = departmentService.createDepartment(department);
        return new ResponseEntity<>(createdDepartment, HttpStatus.CREATED);
    }

    /**
     * Retrieves all departments.
     * POSTMAN TEST: GET {{baseURL}}/api/departments
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.findAllDepartments();
        return ResponseEntity.ok(departments);
    }

    /**
     * Retrieves a department by ID.
     * POSTMAN TEST: GET {{baseURL}}/api/departments/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        return departmentService.findDepartmentById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + id));
    }

    /**
     * Updates an existing department.
     * POSTMAN TEST: PUT {{baseURL}}/api/departments/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @RequestBody Department departmentDetails) {
        Department updatedDepartment = departmentService.updateDepartment(id, departmentDetails);
        return ResponseEntity.ok(updatedDepartment);
    }

    /**
     * Deletes a department.
     * POSTMAN TEST: DELETE {{baseURL}}/api/departments/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    // --- Membership Endpoints (Admin only) ---

    /**
     * Adds a user as a member to the specified department.
     * POSTMAN TEST: POST {{baseURL}}/api/departments/{departmentId}/members/{userId}
     */
    @PostMapping("/{departmentId}/members/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Department> addMemberToDepartment(@PathVariable Long departmentId, @PathVariable Long userId) {
        Department updatedDepartment = departmentService.addMemberToDepartment(departmentId, userId);
        return ResponseEntity.ok(updatedDepartment);
    }

    /**
     * Removes a user from the specified department.
     * POSTMAN TEST: DELETE {{baseURL}}/api/departments/{departmentId}/members/{userId}
     */
    @DeleteMapping("/{departmentId}/members/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Department> removeMemberFromDepartment(@PathVariable Long departmentId, @PathVariable Long userId) {
        Department updatedDepartment = departmentService.removeMemberFromDepartment(departmentId, userId);
        return ResponseEntity.ok(updatedDepartment);
    }

    // --- Reporting Endpoint (Admin/Manager only) ---

    /**
     * Retrieves a budget overview for all departments.
     * POSTMAN TEST: GET {{baseURL}}/api/departments/budget-overview
     */
    @GetMapping("/budget-overview")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<BudgetOverviewDTO>> getBudgetOverview() {
        List<BudgetOverviewDTO> overview = departmentService.getBudgetOverview();
        return ResponseEntity.ok(overview);
    }
}
