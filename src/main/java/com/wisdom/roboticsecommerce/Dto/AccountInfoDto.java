package com.wisdom.roboticsecommerce.Dto;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfoDto {
    private String fullname;
    @Temporal(TemporalType.DATE)
    private Date birthday;
    private Integer gender;
}
