
package com.wisdom.roboticsecommerce.Dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PageableDto {
    private Integer pageNo;
    private Integer pageSize;
    private String sortBy;
    private String sortType;
}
