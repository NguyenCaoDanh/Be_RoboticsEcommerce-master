package com.wisdom.roboticsecommerce.Service;

import com.wisdom.roboticsecommerce.Entities.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    Page<Notification> getAllByCurrentAccount(Pageable pageable);
    Notification getDetail(Long notificationId);
}
