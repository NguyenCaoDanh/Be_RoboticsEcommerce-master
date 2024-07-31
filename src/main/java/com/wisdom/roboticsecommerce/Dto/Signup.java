package com.wisdom.roboticsecommerce.Dto;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class Signup {
    private String fullname;
    private String password;
    private String email;
    private Date birthday;
    private Integer gender;
    //address
    private String addressname;
    private String phone;
    private String tag_name;
    private Long provinceId;
    private Long districtId;
    private Long wardId;
    private String address_detail;
}
