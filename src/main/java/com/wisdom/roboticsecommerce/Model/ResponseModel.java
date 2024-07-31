package com.wisdom.roboticsecommerce.Model;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseModel {
    private String status;
    private String timestamp;
    private Object message;
}
