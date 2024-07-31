package com.wisdom.roboticsecommerce.Controller;

import com.wisdom.roboticsecommerce.Dto.CartRequest;
import com.wisdom.roboticsecommerce.Dto.ListCartRequest;
import com.wisdom.roboticsecommerce.Dto.PageableDto;
import com.wisdom.roboticsecommerce.Entities.Cart;
import com.wisdom.roboticsecommerce.Entities.Product;
import com.wisdom.roboticsecommerce.Mapper.PageMapper;
import com.wisdom.roboticsecommerce.Model.ResponseModel;
import com.wisdom.roboticsecommerce.Service.CartService;
import com.wisdom.roboticsecommerce.Service.Impl.CartServiceImpl;
import com.wisdom.roboticsecommerce.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.ErrorHandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private PageMapper pageMapper;

    @GetMapping("/getAllCart")
    public ResponseEntity<?> getAllCart(@ModelAttribute PageableDto pageableDto){
        try {
            PageableDto pageableDtoReal = new PageableDto(0, 10, "id", "desc");
            Utils.modelMapper(true, true).map(pageableDto, pageableDtoReal);
            Pageable pageable = pageMapper.customPage(
                    pageableDtoReal.getPageNo(),
                    pageableDtoReal.getPageSize(),
                    pageableDtoReal.getSortBy(),
                    pageableDtoReal.getSortType());
            Page<Cart> cartList = cartService.getAllCart(pageable);
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(), cartList));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Fail",
                            LocalDate.now().toString(), e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCart(@RequestBody CartRequest cartRequest){
        try {

            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(), cartService.addCart(cartRequest)));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Fail",
                            LocalDate.now().toString(), e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCart(@RequestBody CartRequest cartRequest){
        try {

            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(), cartService.updateCart(cartRequest)));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Fail",
                            LocalDate.now().toString(), e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCart(@RequestBody ListCartRequest listCartRequest){
        try {
            cartService.deleteCart(listCartRequest);
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(), "Xóa thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Fail",
                            LocalDate.now().toString(), e.getMessage()));
        }
    }
}
