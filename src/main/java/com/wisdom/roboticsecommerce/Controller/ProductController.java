
package com.wisdom.roboticsecommerce.Controller;

import com.wisdom.roboticsecommerce.Dto.ProductDto;
import com.wisdom.roboticsecommerce.Entities.Product;
import com.wisdom.roboticsecommerce.Mapper.PageMapper;
import com.wisdom.roboticsecommerce.Model.ResponseModel;
import com.wisdom.roboticsecommerce.Service.ProductImageService;
import com.wisdom.roboticsecommerce.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
@RestController
@RequestMapping("api/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private PageMapper pageMapper;
    @PostMapping(value = "create",consumes = {"multipart/form-data"})
    private ResponseEntity<?> create(@RequestPart(value = "file") MultipartFile file,
                                     @ModelAttribute ProductDto productDto) {
        try {
             productService.create(file,productDto);
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(),"Thêm sản phẩm thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Error",
                            LocalDate.now().toString(),e.getMessage()));
        }
    }
    @PutMapping(value = "update",consumes = {"multipart/form-data"})
    private ResponseEntity<?> update(@RequestPart(value = "file") MultipartFile file,
                                     @ModelAttribute ProductDto productDto,@Param("id") Long id) {
        try {
            productService.update(file,productDto,id);
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(),"Sửa sản phẩm thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Error",
                            LocalDate.now().toString(),e.getMessage()));
        }
    }
    @GetMapping("findbyid")
    private ResponseEntity<?> findById(@Param("id") Long id) {
        try {
            var findbyid = productService.findById(id);
            String imageUrl = productImageService.getImage(findbyid.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("product", findbyid);
            response.put("image", imageUrl);

            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(),response));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Error",
                            LocalDate.now().toString(),e.getMessage()));
        }
    }
    @PostMapping("delete")
    private ResponseEntity<?> delete(@Param("id") Long id) {
        try {
              productService.deleted(id);
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(),"Xóa sản phẩm thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Error",
                            LocalDate.now().toString(),e.getMessage()));
        }
    }
    @GetMapping("search")
    private ResponseEntity<?> getall(@RequestParam(defaultValue = "0") Integer pageNo,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam(defaultValue = "id") String sortBy,
                                     @RequestParam(name = "sortType", required = false, defaultValue = "desc") String sortType,
                                     @Param("name") String name,
                                     @Param("categoryId") Long categoryId,
                                     @Param("status") Integer status) {
        try {
            var getall = productService.search(categoryId, name, status, pageNo, pageSize, sortBy, sortType);
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(),getall));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Error",
                            LocalDate.now().toString(),e.getMessage()));
        }
    }
    @GetMapping("/detail")
    public ResponseEntity<?> productDetail(@RequestParam("id") Long id){
        try {
            Product product = productService.detailProduct(id);

            String imageUrl = productImageService.getImage(product.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("product", product);
            response.put("image", imageUrl);

            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(), response));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Fail",
                            LocalDate.now().toString(), null));
        }
    }

    @GetMapping("/searchKeyword")
    private ResponseEntity<?> searchKeyword(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(name = "sortType", required = false, defaultValue = "desc") String sortType
            ) {
        try {

            org.springframework.data.domain.Pageable pageable = pageMapper.customPage(
                    pageNo, pageSize, sortBy, sortType  );

            Page<Map<String,Object>> productList = productService.findByKeyword(keyword, categoryId, pageable);

            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(), productList));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Fail",
                            LocalDate.now().toString(), "Error"));
        }
    }
}
