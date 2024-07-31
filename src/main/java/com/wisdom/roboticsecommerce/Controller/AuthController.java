package com.wisdom.roboticsecommerce.Controller;

import com.wisdom.roboticsecommerce.Dto.AuthRequest;
import com.wisdom.roboticsecommerce.Dto.Signup;
import com.wisdom.roboticsecommerce.Model.ResponseModel;
import com.wisdom.roboticsecommerce.Service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("signin")
    private ResponseEntity<?> signin(@RequestBody AuthRequest signin) {
        try {
            var login = authService.signIn(signin);
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Đăng nhập thành công", LocalDate.now().toString(), login));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Đăng nhập thất bại",
                            LocalDate.now().toString(), "Tài khoản hoặc mật khẩu không chính xác"));
        }
    }
    @PostMapping("signup")
    private ResponseEntity<?> signup(@RequestBody Signup signup) {
        try {
            var signuped = authService.signUp(signup);
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Đăng ký thành công", LocalDate.now().toString(), signuped));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Đăng ký thất bại",
                            LocalDate.now().toString(),e.getMessage()));
        }
    }
    @PostMapping("logout")
    public ResponseEntity<?> logout(HttpSession session) {
        try{
            session.invalidate();
            return ResponseEntity.status(OK).body(
                    new ResponseModel("Success", LocalDate.now().toString(),"Đăng xuất thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(
                    new ResponseModel("Error", LocalDate.now().toString(),"Đăng xuất thất bại"));
        }
    }
}
