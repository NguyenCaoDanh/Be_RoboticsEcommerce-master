package com.wisdom.roboticsecommerce.Service;

import com.wisdom.roboticsecommerce.Dto.ResetPassword;

public interface EmailService {
    void sendMail(String email, String link);
    String checkEmail(String email);
    void resetPassword(ResetPassword resetPassword);
}
