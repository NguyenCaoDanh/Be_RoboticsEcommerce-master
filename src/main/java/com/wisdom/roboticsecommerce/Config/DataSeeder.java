package com.wisdom.roboticsecommerce.Config;


import com.wisdom.roboticsecommerce.Entities.Account;
import com.wisdom.roboticsecommerce.Entities.Role;
import com.wisdom.roboticsecommerce.Repositories.AccountRepository;
import com.wisdom.roboticsecommerce.Repositories.RoleRepository;
import com.wisdom.roboticsecommerce.Utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final AccountRepository userRepository;

    private final RoleRepository roleRepository;

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent arg0) {

        createRoleIfNotFound("ADMIN", "Quản trị hệ thống");
        createRoleIfNotFound("USER", "Người dùng");

        // Admin account
        if (userRepository.findByEmail("admin@gmail.com") == null) {
            Account admin = new Account();
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder().encode("1234567aA"));
            admin.setStatus(Constants.STATUS_ACTIVE);
            admin.setDeleted(Constants.DONT_DELETE);
            admin.setCreatAt(new Date());
            admin.setFullname("tuấn trần");
            HashSet<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByCodeAndName("ADMIN", "Quản trị hệ thống"));
            admin.setRole(roles);
            userRepository.save(admin);
        }
    }

    @Transactional
    public void createRoleIfNotFound(String roleName, String identification) {
        Role roles = roleRepository.findByCodeAndName(roleName, identification);
        if (roles == null) {
            roles = new Role(roleName, identification);
            roles.setCode(roleName);
            roles.setName(identification);
            roleRepository.save(roles);
        }
    }
}