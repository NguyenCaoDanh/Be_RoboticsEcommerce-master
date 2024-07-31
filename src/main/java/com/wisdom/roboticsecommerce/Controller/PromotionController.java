package com.wisdom.roboticsecommerce.Controller;

import com.wisdom.roboticsecommerce.Dto.PromotionDto;
import com.wisdom.roboticsecommerce.Entities.Promotion;
import com.wisdom.roboticsecommerce.Model.ResponseModel;
import com.wisdom.roboticsecommerce.Service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/promotion")
public class PromotionController {
    @Autowired
    private PromotionService promotionService;
    @PostMapping("create")
    ResponseEntity<?> create(@RequestBody PromotionDto promotionDto){
        try {
            var create = promotionService.create(promotionDto);
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(),create));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Error", LocalDate.now().toString(),e.getMessage()));
        }
    }
    @PutMapping("update")
    ResponseEntity<?> update(@RequestBody Promotion promotion, @Param("id") Long id){
        try {
            var update = promotionService.update(promotion, id);
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(),update));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Error", LocalDate.now().toString(),e.getMessage()));
        }
    }
    @PostMapping("delete")
    ResponseEntity<?> delete(@Param("id") Long id){
        try {
            promotionService.deleted(id);
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(),"Xóa khuyến mãi thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Error", LocalDate.now().toString(),e.getMessage()));
        }
    }
    @GetMapping("findbyid")
    ResponseEntity<?> findById(@Param("id") Long id){
        try {
            var findById = promotionService.findById(id);
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(),findById));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Error", LocalDate.now().toString(),e.getMessage()));
        }
    }
    @GetMapping("search")
    ResponseEntity<?> getAll(){
        try {
            var search = promotionService.search();
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(),search));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Error", LocalDate.now().toString(),e.getMessage()));
        }
    }
}
