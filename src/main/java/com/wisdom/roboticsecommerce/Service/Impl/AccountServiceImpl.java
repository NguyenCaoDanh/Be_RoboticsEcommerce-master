package com.wisdom.roboticsecommerce.Service.Impl;

import com.wisdom.roboticsecommerce.Dto.AccountInfoDto;
import com.wisdom.roboticsecommerce.Entities.Account;
import com.wisdom.roboticsecommerce.ExceptionHandler.CustomException;
import com.wisdom.roboticsecommerce.Mapper.Mapper;
import com.wisdom.roboticsecommerce.Dto.ChangePasswordDto;
import com.wisdom.roboticsecommerce.Repositories.AccountRepository;
import com.wisdom.roboticsecommerce.Service.AccountService;
import com.wisdom.roboticsecommerce.Utils.Constants;
import com.wisdom.roboticsecommerce.Utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private Mapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<Account> findByKeyword(String keyword, Integer status, Pageable pageable) {
        Page<Account> accountList = accountRepository.findByKeyword(keyword, status, pageable);

        return accountList;
    }

    @Override
    public AccountInfoDto getInfoDetail() {
        Long accountId = mapper.getUserIdByToken();

        Account account = accountRepository.getOneCustom(
                accountId,
                Constants.STATUS_ACTIVE,
                Constants.DONT_DELETE);

        if (account == null) {
            throw new CustomException("Không tìm thấy thông tin tài khoản");
        }

        ModelMapper modelMapper = new ModelMapper();
        AccountInfoDto accountInfoDto = new AccountInfoDto();
        modelMapper.map(account, accountInfoDto);

        return accountInfoDto;
    }

    @Override
    public AccountInfoDto updateInfo(AccountInfoDto accountInfoDto) {
        Long accountId = mapper.getUserIdByToken();
        Account account = accountRepository.getOneCustom(
                accountId,
                Constants.STATUS_ACTIVE,
                Constants.DONT_DELETE);
        if (account == null) {
            throw new CustomException("Không tìm thấy thông tin tài khoản");
        }

        Utils.modelMapper(false, false).map(accountInfoDto, account);

        accountRepository.save(account);

        return accountInfoDto;
    }

    @Override
    public void changePassword(ChangePasswordDto changePasswordDto) {
        Long accountId = mapper.getUserIdByToken();
        Account account = accountRepository.getOneCustom(
                accountId,
                Constants.STATUS_ACTIVE,
                Constants.DONT_DELETE);
        if (account == null) {
            throw new CustomException("Không tìm thấy thông tin tài khoản");
        }
        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), account.getPassword())) {
            throw new CustomException("Mật khẩu nhập vào không đúng");
        }

        account.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        accountRepository.save(account);
    }
}
