package com.wisdom.roboticsecommerce.Dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class AuthResponse {
    private String username;
    private String token;
    private String name;
//    private String type = "Bearer";
    private Collection<? extends GrantedAuthority> role;
}
