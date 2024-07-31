package com.wisdom.roboticsecommerce.Controller;


import com.wisdom.roboticsecommerce.Dto.PageableDto;
import com.wisdom.roboticsecommerce.Entities.OrderDetail;
import com.wisdom.roboticsecommerce.Mapper.PageMapper;
import com.wisdom.roboticsecommerce.Model.ResponseModel;
import com.wisdom.roboticsecommerce.Service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/order-detail")
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private PageMapper pageMapper;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(@Param("orderId") Long orderId, @ModelAttribute PageableDto pageableDto) {
        try {
            Pageable pageable = pageMapper.customPage(
                    pageableDto.getPageNo(),
                    pageableDto.getPageSize(),
                    pageableDto.getSortBy().isEmpty() ? "id" : pageableDto.getSortBy(),
                    pageableDto.getSortType().isEmpty() ? "desc" : pageableDto.getSortType());
            Page<OrderDetail> orderDetailList = orderDetailService.getAll(orderId, pageable);
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(),orderDetailList));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Fail",
                            LocalDate.now().toString(), null));
        }
    }
}
