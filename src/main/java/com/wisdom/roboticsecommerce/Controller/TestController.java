package com.wisdom.roboticsecommerce.Controller;

import com.wisdom.roboticsecommerce.Utils.ResponseUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/test")
public class TestController {
    @GetMapping("")
    private ResponseEntity<?> test() {
        return ResponseUtil.success("Gọi api thành công");
    }
}
