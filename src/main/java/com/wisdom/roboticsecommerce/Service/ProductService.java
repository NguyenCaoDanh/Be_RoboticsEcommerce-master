
package com.wisdom.roboticsecommerce.Service;

import com.wisdom.roboticsecommerce.Dto.ProductDto;
import com.wisdom.roboticsecommerce.Entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ProductService {
    void create(MultipartFile imageProduct, ProductDto productDto);
    void update(MultipartFile imageProduct,ProductDto productDto, Long productId);
    void deleted(Long productId);
    Page<Map<String, Object>> search(Long categoryId, String name, Integer status, Integer pageNo,Integer pageSize,String sortBy,String sortType);
    Product findById(Long productId);
    Product detailProduct(Long id);
    Page<Map<String,Object>> findByKeyword(String keyword, Long categoryId, Pageable pageable);
}
