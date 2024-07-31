package com.wisdom.roboticsecommerce.Controller;

import com.wisdom.roboticsecommerce.Dto.CategoryDto;
import com.wisdom.roboticsecommerce.Model.ResponseModel;
import com.wisdom.roboticsecommerce.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("search")
    ResponseEntity<?> search(@Param("name") String name,
                             @Param("status") Integer status,
                             @RequestParam(defaultValue = "0") Integer pageNo,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(defaultValue = "id") String sortBy,
                             @RequestParam(name = "sortType", required = false, defaultValue = "desc") String sortType){
        try{
            var search = categoryService.search(name, status, pageNo, pageSize, sortBy, sortType);
            return ResponseEntity.status(OK).body(new ResponseModel("Success", LocalDate.now().toString(),search));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ResponseModel("Error", LocalDate.now().toString(),e.getMessage()));
        }
    }
    @GetMapping("findbyid")
    ResponseEntity<?> findById(@Param("id") Long id){
        try{
            var findById = categoryService.findById(id);
            return ResponseEntity.status(OK).body(new ResponseModel("Success", LocalDate.now().toString(),findById));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ResponseModel("Error", LocalDate.now().toString(),e.getMessage()));
        }
    }
    @PostMapping("create")
    ResponseEntity<?> create(@RequestBody CategoryDto categoryDto){
        try{
            var create = categoryService.create(categoryDto);
            return ResponseEntity.status(OK).body(new ResponseModel("Success", LocalDate.now().toString(),create));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ResponseModel("Error", LocalDate.now().toString(),e.getMessage()));
        }
    }
    @PutMapping("update")
    ResponseEntity<?> update(@Param("id") Long id, @RequestBody CategoryDto categoryDto){
        try{
            var update = categoryService.update(categoryDto, id);
            return ResponseEntity.status(OK).body(new ResponseModel("Success", LocalDate.now().toString(),update));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ResponseModel("Error", LocalDate.now().toString(),e.getMessage()));
        }
    }
    @PostMapping("delete")
    ResponseEntity<?> delete(@Param("id") Long id){
        try{
            categoryService.delete(id);
            return ResponseEntity.status(OK).body(new ResponseModel("Success", LocalDate.now().toString(),"Xóa danh mục thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ResponseModel("Error", LocalDate.now().toString(),e.getMessage()));
        }
    }
}
