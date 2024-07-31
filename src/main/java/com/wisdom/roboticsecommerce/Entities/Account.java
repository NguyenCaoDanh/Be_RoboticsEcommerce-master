package com.wisdom.roboticsecommerce.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_name", length = 200)
    private String fullname;
    @Column(name = "email", length = 100)
    private String email;
    @Column(name = "password", length = 100)
    private String password;
    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    private Date birthday;
    @Column(name = "gender")
    private Integer gender;
    @Column(name = "token")
    private String token;
    @Column(name = "createAt")
    @Temporal(TemporalType.DATE)
    private Date creatAt;
    @Temporal(TemporalType.DATE)
    @Column(name = "updateAt")
    private Date updateAt;
    @Column(name = "status")
    private Integer status;
    @Column(name = "deleted")
    private Integer deleted;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime expiredToken;

    @ManyToMany(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(name = "user_role",joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Role> role;
}
