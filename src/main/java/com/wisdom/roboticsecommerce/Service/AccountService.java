package com.wisdom.roboticsecommerce.Service;

import com.wisdom.roboticsecommerce.Dto.AccountInfoDto;
import com.wisdom.roboticsecommerce.Entities.Account;
import com.wisdom.roboticsecommerce.Dto.ChangePasswordDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {
    Page<Account> findByKeyword(String keyword, Integer status, Pageable pageable);
    AccountInfoDto getInfoDetail();
    AccountInfoDto updateInfo(AccountInfoDto accountInfoDto);
    void changePassword(ChangePasswordDto changePasswordDto);
}
