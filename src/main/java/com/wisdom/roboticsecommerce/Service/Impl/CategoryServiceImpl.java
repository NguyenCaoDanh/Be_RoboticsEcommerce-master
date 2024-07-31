package com.wisdom.roboticsecommerce.Service.Impl;

import com.wisdom.roboticsecommerce.Dto.CategoryDto;
import com.wisdom.roboticsecommerce.Entities.Category;
import com.wisdom.roboticsecommerce.Mapper.Mapper;
import com.wisdom.roboticsecommerce.Mapper.PageMapper;
import com.wisdom.roboticsecommerce.Repositories.CategoryRepository;
import com.wisdom.roboticsecommerce.Service.CategoryService;
import com.wisdom.roboticsecommerce.Utils.Constants;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private Mapper mapper;
    @Autowired
    private PageMapper pageMapper;
    @Override
    public Category create(CategoryDto categoryDto) {
        try {
            Category category = new Category();
            category.setName(categoryDto.getName());
            category.setAccountId(mapper.getUserIdByToken());
            category.setCreatAt(new Date());
            category.setStatus(Constants.STATUS_ACTIVE);
            category.setDeleted(Constants.DONT_DELETE);
            categoryRepository.save(category);
            return category;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Category update(CategoryDto categoryDto, Long id) {
        try{
            Optional<Category> categoryOptional = categoryRepository.findByIdAndDeleted(id,Constants.DONT_DELETE);
            if (categoryOptional.isEmpty()){
                throw new MessageDescriptorFormatException("Không tìm thấy danh mục");
            }
            Category category = categoryOptional.get();
            category.setName(categoryDto.getName());
            category.setUpdateAt(new Date());
            category.setAccountId(mapper.getUserIdByToken());
            categoryRepository.save(category);
            return category;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Category findById(Long id) {
        try{
            Optional<Category> categoryOptional = categoryRepository.findByIdAndDeleted(id, Constants.DONT_DELETE);
            if (categoryOptional.isEmpty()){
                throw new MessageDescriptorFormatException("Không tìm thấy danh mục");
            }
            return categoryOptional.get();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        try{
            Optional<Category> categoryOptional = categoryRepository.findByIdAndDeleted(id, Constants.DONT_DELETE);
            if (categoryOptional.isEmpty()){
                throw new MessageDescriptorFormatException("Không tìm thấy danh mục");
            }
            Category category = categoryOptional.get();
            category.setDeleted(Constants.DELETED);
            categoryRepository.save(category);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> search(String name, Integer status, Integer pageNo, Integer pageSize, String sortBy, String sortType) {
        try {
            var pageable = pageMapper.customPage(pageNo, pageSize, sortBy, sortType);
            var search = categoryRepository.search(name, status, pageable);
            return search;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
