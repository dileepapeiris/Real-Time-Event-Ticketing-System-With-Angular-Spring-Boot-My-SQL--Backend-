package com.oop.backend.DTO;

import lombok.Data;

@Data
public class UserLogin {
    private String email;
    private String password;
    private String role;
}
