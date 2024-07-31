package com.wisdom.roboticsecommerce.Service.Impl;

import com.wisdom.roboticsecommerce.Dto.RevenueDto;
import com.wisdom.roboticsecommerce.Dto.RevenueResponse;
import com.wisdom.roboticsecommerce.Repositories.OrderRepository;
import com.wisdom.roboticsecommerce.Service.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RevenueServiceImpl implements RevenueService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<RevenueResponse> getRevenue(RevenueDto revenueDto) {
        List<RevenueResponse> revenueResponsesList = new ArrayList<>();
        List<Object[]> revenueList = new ArrayList<>();

        if (revenueDto.getYear() == null) {
            revenueList = orderRepository.getRevenueByYear();
        } else if (revenueDto.getMonth() == null) {
            revenueList = orderRepository.getRevenueByMonth(revenueDto.getYear());
        } else {
            revenueList = orderRepository.getRevenueByDay(revenueDto.getYear(), revenueDto.getMonth());
        }

        for (Object[] item : revenueList) {
            RevenueResponse revenueResponse = new RevenueResponse();
            revenueResponse.setUnitName((String)(item[0]));
            revenueResponse.setUnitValue((Integer) (item[1]));
            revenueResponse.setRevenue((Double) (item[2]));
            revenueResponsesList.add(revenueResponse);
        }

        return revenueResponsesList;
    }
}
