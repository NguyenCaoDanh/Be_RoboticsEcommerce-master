package com.wisdom.roboticsecommerce.Controller;

import com.wisdom.roboticsecommerce.Dto.AccountInfoDto;
import com.wisdom.roboticsecommerce.Dto.ChangePasswordDto;
import com.wisdom.roboticsecommerce.Entities.Account;
import com.wisdom.roboticsecommerce.ExceptionHandler.CustomException;
import com.wisdom.roboticsecommerce.Mapper.PageMapper;
import com.wisdom.roboticsecommerce.Dto.PageableDto;
import com.wisdom.roboticsecommerce.Service.AccountService;
import com.wisdom.roboticsecommerce.Utils.ResponseUtil;
import com.wisdom.roboticsecommerce.Utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private PageMapper pageMapper;

    @GetMapping("search")
    private ResponseEntity<?> search(
            @RequestParam String keyword,
            @RequestParam(required = false) Integer status,
            @ModelAttribute PageableDto pageableDto) {

        PageableDto pageableDtoReal = new PageableDto(0, 10, "id", "desc");
        Utils.modelMapper(true, true).map(pageableDto, pageableDtoReal);
        Pageable pageable = pageMapper.customPage(
                pageableDtoReal.getPageNo(),
                pageableDtoReal.getPageSize(),
                pageableDtoReal.getSortBy(),
                pageableDtoReal.getSortType());

        Page<Account> accountList = accountService.findByKeyword(keyword, status, pageable);

        return ResponseUtil.success(accountList);
    }

    @GetMapping("info/detail")
    private ResponseEntity<?> getInfoDetail() {
        AccountInfoDto accountInfo = accountService.getInfoDetail();

        return ResponseUtil.success(accountInfo);
    }

    @PutMapping("info/update")
    private ResponseEntity<?> updateInfo(@RequestBody AccountInfoDto accountInfoDto) {
        accountService.updateInfo(accountInfoDto);

        return ResponseUtil.success(accountInfoDto);
    }

    @PutMapping("password/change")
    private ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        accountService.changePassword(changePasswordDto);

        return ResponseUtil.success("Đổi mật khẩu thành công");
    }
}
