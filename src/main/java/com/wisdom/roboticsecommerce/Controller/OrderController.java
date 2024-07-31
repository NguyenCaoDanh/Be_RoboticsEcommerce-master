
package com.wisdom.roboticsecommerce.Controller;

import com.wisdom.roboticsecommerce.Dto.OrderRequest;
import com.wisdom.roboticsecommerce.Dto.PageableDto;
import com.wisdom.roboticsecommerce.Entities.Order;
import com.wisdom.roboticsecommerce.Mapper.PageMapper;
import com.wisdom.roboticsecommerce.Model.ResponseModel;
import com.wisdom.roboticsecommerce.Service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/order")
public class OrderController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private PageMapper pageMapper;
    @GetMapping("search")
    ResponseEntity<?> search(@RequestParam(defaultValue = "0") Integer pageNo,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(defaultValue = "id") String sortBy,
                             @RequestParam(name = "sortType", required = false, defaultValue = "desc") String sortType,
                             @Param("status") Integer status){
        try{
            var search = ordersService.search(status, pageNo, pageSize, sortBy, sortType);
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(),search));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Error", LocalDate.now().toString(),e.getMessage()));
        }
    }
    @PutMapping("updateStatus")
    ResponseEntity<?> updateStatus(@Param("id") Long id,@Param("status") Integer status){
        try{
            var updateStatus = ordersService.updateStatus(id, status);
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(),updateStatus));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Error", LocalDate.now().toString(),e.getMessage()));
        }
    }
    @GetMapping("detail")
    ResponseEntity<?> findById(@Param("orderId") Long orderId) {
        try {
            var updateStatus = ordersService.findById(orderId);
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(), updateStatus));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Error", LocalDate.now().toString(), e.getMessage()));
        }
    }
    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest){
        try {
                Order order = ordersService.createOrder(orderRequest);
                return ResponseEntity.status(OK).body(
                        new ResponseModel("Success", LocalDate.now().toString(),order));
        } catch (Exception e) {
                return ResponseEntity.status(BAD_REQUEST).body(
                        new ResponseModel("Fail",
                                LocalDate.now().toString(), e.getMessage()));
            }
        }

    @GetMapping("/getAllOrdersByUserId")
    public ResponseEntity<?> getAllOrder(@ModelAttribute PageableDto pageableDto){
        try {
            Pageable pageable = pageMapper.customPage(
                    pageableDto.getPageNo(),
                    pageableDto.getPageSize(),
                    pageableDto.getSortBy().isEmpty() ? "id" : pageableDto.getSortBy(),
                    pageableDto.getSortType().isEmpty() ? "desc" : pageableDto.getSortType());
            Page<Order> orderList = ordersService.getAllOrdersByUserId(pageable);
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(),orderList));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Fail",
                            LocalDate.now().toString(),e.getMessage()));
        }
    }


    @GetMapping("/getOrdersByUserIdAndStatus")
    public ResponseEntity<?> getOrdersByUserIdAndStatus(@RequestParam Integer status, @ModelAttribute PageableDto pageableDto) {
        try {
            Pageable pageable = pageMapper.customPage(
                    pageableDto.getPageNo(),
                    pageableDto.getPageSize(),
                    pageableDto.getSortBy().isEmpty() ? "id" : pageableDto.getSortBy(),
                    pageableDto.getSortType().isEmpty() ? "desc" : pageableDto.getSortType());
            Page<Order> orderListByStatus = ordersService.getOrdersByUserIdAndStatus(status,pageable);
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(),orderListByStatus));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Fail",
                            LocalDate.now().toString(), e.getMessage()));
        }
    }

    @PutMapping("/cancelOrder")
    public ResponseEntity<?> cancelOrder(@Param("orderId") Long orderId) {
        try {
            System.out.println("orderId = " + orderId);
            ordersService.cancelOrder(orderId);
            System.out.println("cancel order success");
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(),"Hủy đơn hàng thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Fail",
                            LocalDate.now().toString(), e.getMessage()));
        }
    }

    @PutMapping("/updateOrderStatus")
    public ResponseEntity<?> updateOrderStatus(@Param("orderId") Long orderId, @Param("status") Integer status) {

        try {
            ordersService.updateOrderStatus(orderId, status);
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(),"Cập nhập trạng thái thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Fail",
                            LocalDate.now().toString(),e.getMessage()));
        }
    }

    @PutMapping("/addFeedback")
    public ResponseEntity<?> addFeedback(@Param("orderId") Long orderId,@Param("productId") Long productId,@Param("content") String content,@Param("rating") Integer rating) {
        try {
            ordersService.addFeedback(orderId, productId, content, rating);
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(),"Feedback thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Fail",
                            LocalDate.now().toString(), e.getMessage()));
        }
    }

}
