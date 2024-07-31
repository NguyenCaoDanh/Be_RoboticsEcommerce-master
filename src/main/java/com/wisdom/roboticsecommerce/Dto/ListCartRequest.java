package com.wisdom.roboticsecommerce.Dto;

import lombok.Data;

import java.util.List;

@Data
public class ListCartRequest {
    private List<Long> listCartId;
}
