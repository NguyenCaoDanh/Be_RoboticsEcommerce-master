package com.wisdom.roboticsecommerce.Controller;

import com.wisdom.roboticsecommerce.Model.ResponseModel;
import com.wisdom.roboticsecommerce.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @GetMapping("link")
     ResponseEntity<?> getlink(@Param("orderId") Long orderId){
        try{
            var link = paymentService.linkPayment(orderId);
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(),link));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Error", LocalDate.now().toString(),e.getMessage()));
        }
    }
    @PostMapping("getlink")
    ResponseEntity<?> getAll(@Param("id") Long orderId, @Param("amount") Double amount){
        try{
            var getAll = paymentService.getAll(amount, orderId);
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(),getAll));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Error", LocalDate.now().toString(),e.getMessage()));
        }
    }
}
