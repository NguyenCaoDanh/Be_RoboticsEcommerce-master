package com.wisdom.roboticsecommerce.Controller;

import com.wisdom.roboticsecommerce.Dto.RevenueDto;
import com.wisdom.roboticsecommerce.Dto.RevenueResponse;
import com.wisdom.roboticsecommerce.Repositories.OrderRepository;
import com.wisdom.roboticsecommerce.Service.RevenueService;
import com.wisdom.roboticsecommerce.Utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/revenue")
public class RevenueController {
    @Autowired
    private RevenueService revenueService;

    @GetMapping("get")
    private ResponseEntity<?> getRevenue(@ModelAttribute RevenueDto revenueDto) {
        List<RevenueResponse> revenueResponsesList = revenueService.getRevenue(revenueDto);

        return ResponseUtil.success(revenueResponsesList);
    }
}
