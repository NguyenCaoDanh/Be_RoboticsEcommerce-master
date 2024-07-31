package com.wisdom.roboticsecommerce.Service;

import com.wisdom.roboticsecommerce.Dto.CategoryDto;
import com.wisdom.roboticsecommerce.Entities.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    Category create(CategoryDto categoryDto);
    Category update(CategoryDto categoryDto, Long id);
    Category findById(Long id);
    void delete(Long id);
    List<Map<String, Object>> search(String name, Integer status,Integer pageNo, Integer pageSize, String sortBy, String sortType);
}
