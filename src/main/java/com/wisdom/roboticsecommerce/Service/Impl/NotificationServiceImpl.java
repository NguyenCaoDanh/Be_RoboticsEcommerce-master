package com.wisdom.roboticsecommerce.Service.Impl;

import com.wisdom.roboticsecommerce.Entities.Notification;
import com.wisdom.roboticsecommerce.ExceptionHandler.CustomException;
import com.wisdom.roboticsecommerce.Mapper.Mapper;
import com.wisdom.roboticsecommerce.Repositories.NotificationRepository;
import com.wisdom.roboticsecommerce.Service.NotificationService;
import com.wisdom.roboticsecommerce.Utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private Mapper mapper;

    @Override
    public Page<Notification> getAllByCurrentAccount(Pageable pageable) {
        Long accountId = mapper.getUserIdByToken();
        Page<Notification> notificationList = notificationRepository.getAllCustom(
                accountId,
                Constants.STATUS_ACTIVE,
                Constants.DONT_DELETE,
                pageable);

        return notificationList;
    }

    @Override
    public Notification getDetail(Long notificationId) {
        Long accountId = mapper.getUserIdByToken();
        Notification notification = notificationRepository.getOneCustom(
                notificationId,
                accountId,
                Constants.STATUS_ACTIVE,
                Constants.DONT_DELETE);
        if (notification == null) {
//            throw new
            throw new CustomException("Không tìm thấy thông báo với id " + notificationId);
        }

        return notification;
    }
}
