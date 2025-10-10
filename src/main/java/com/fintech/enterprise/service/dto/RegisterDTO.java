package com.fintech.enterprise.service.dto;


import com.fintech.enterprise.model.Role;
import lombok.Data;

@Data
public class RegisterDTO{
    private String username;
    private String password;
    private Role role;
}
