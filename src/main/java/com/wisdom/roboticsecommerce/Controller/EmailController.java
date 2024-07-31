package com.wisdom.roboticsecommerce.Controller;

import com.wisdom.roboticsecommerce.Dto.EmailDto;
import com.wisdom.roboticsecommerce.Dto.ResetPassword;
import com.wisdom.roboticsecommerce.Model.ResponseModel;
import com.wisdom.roboticsecommerce.Service.EmailService;
import com.wisdom.roboticsecommerce.Utils.Utility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody EmailDto email, HttpServletRequest request) {
        try {
            String token = emailService.checkEmail(email.getEmail());
            if (!token.startsWith("Invalid")) {
                token = Utility.getSiteURL(request) + "/email/reset-password?token=" + token;
                emailService.sendMail(email.getEmail(), token);
            }
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(),token));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Error", LocalDate.now().toString(),e.getMessage()));
        }
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPassword resetPassword) {
        try {
            emailService.resetPassword(resetPassword);
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(),"Đổi mật khẩu thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Error", LocalDate.now().toString(),e.getMessage()));
        }
    }
}
