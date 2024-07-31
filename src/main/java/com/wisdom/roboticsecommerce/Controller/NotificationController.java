package com.wisdom.roboticsecommerce.Controller;

import com.wisdom.roboticsecommerce.Dto.PageableDto;
import com.wisdom.roboticsecommerce.Entities.Notification;
import com.wisdom.roboticsecommerce.ExceptionHandler.CustomException;
import com.wisdom.roboticsecommerce.Mapper.PageMapper;
import com.wisdom.roboticsecommerce.Service.NotificationService;
import com.wisdom.roboticsecommerce.Utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private PageMapper pageMapper;

    @GetMapping("all")
    private ResponseEntity<?> getAllByAccount(@ModelAttribute PageableDto pageableDto) {
        Pageable pageable = pageMapper.customPage(
                pageableDto.getPageNo(),
                pageableDto.getPageSize(),
                pageableDto.getSortBy().isEmpty() ? "id" : pageableDto.getSortBy(),
                pageableDto.getSortType().isEmpty() ? "desc" : pageableDto.getSortType()
        );
        Page<Notification> notificationList = notificationService.getAllByCurrentAccount(pageable);

        return ResponseUtil.success(notificationList);
    }

    @GetMapping("detail")
    private ResponseEntity<?> getDetail(@RequestParam Long notificationId) {
        Notification notification = notificationService.getDetail(notificationId);

        return ResponseUtil.success(notification);
    }
}
