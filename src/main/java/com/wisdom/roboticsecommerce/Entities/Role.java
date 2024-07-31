package com.wisdom.roboticsecommerce.Entities;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "code", length = 50)
    private String code;
    @Column(name = "name", length = 100)
    private String name;
//    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "role", fetch = FetchType.EAGER)
//    @ToString.Exclude
//    @JsonIgnore
//    private Set<Account> accounts;

    public Role(String roleName, String identification) {
        this.code = roleName;
        this.name = identification;
    }

    public Role() {

    }
}
