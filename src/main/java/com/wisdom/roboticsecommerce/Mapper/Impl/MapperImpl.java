package com.wisdom.roboticsecommerce.Mapper.Impl;

import com.wisdom.roboticsecommerce.Entities.Account;
import com.wisdom.roboticsecommerce.Mapper.Mapper;
import com.wisdom.roboticsecommerce.Repositories.AccountRepository;
import com.wisdom.roboticsecommerce.Security.UserPrincical.UserPrinciple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MapperImpl implements Mapper {

    private final AccountRepository usersRepository;
    @Override
    public Long getUserIdByToken() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            log.error("Can't get info of user current!");
            throw new MessageDescriptorFormatException("Can't get info of user current!");
        }
        UserPrinciple userDetails = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        var userBy = userDetails.getId();
        Optional<Account> usersOptional = usersRepository.findById(userBy);
        if (usersOptional.isEmpty()) {
            log.error("User with username {} not found", usersOptional);
            throw new MessageDescriptorFormatException("Current user not found");
        }
        return userBy;
    }
}
