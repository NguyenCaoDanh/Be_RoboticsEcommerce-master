package com.wisdom.roboticsecommerce.Service;

import com.wisdom.roboticsecommerce.Dto.RevenueDto;
import com.wisdom.roboticsecommerce.Dto.RevenueResponse;

import java.util.List;

public interface RevenueService {
    List<RevenueResponse> getRevenue(RevenueDto revenueDto);
}
