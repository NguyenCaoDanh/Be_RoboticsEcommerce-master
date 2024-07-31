package com.wisdom.roboticsecommerce.Service.Impl;

import com.wisdom.roboticsecommerce.Dto.AuthRequest;
import com.wisdom.roboticsecommerce.Dto.AuthResponse;
import com.wisdom.roboticsecommerce.Dto.Signup;
import com.wisdom.roboticsecommerce.Entities.Account;
import com.wisdom.roboticsecommerce.Entities.Address;
import com.wisdom.roboticsecommerce.Entities.Role;
import com.wisdom.roboticsecommerce.Repositories.AccountRepository;
import com.wisdom.roboticsecommerce.Repositories.AddressRepository;
import com.wisdom.roboticsecommerce.Security.UserPrincical.UserPrinciple;
import com.wisdom.roboticsecommerce.Security.jwt.JwtProvider;
import com.wisdom.roboticsecommerce.Service.AuthService;
import com.wisdom.roboticsecommerce.Utils.Constants;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Account signUp(Signup signup) {
        try{
            Account accountOptional = accountRepository.findByEmail(signup.getEmail());
            if(accountOptional != null){
                throw new MessageDescriptorFormatException("Email này đã được đăng ký rồi");
            }
            Account account = new Account();
            account.setFullname(signup.getFullname());
            account.setEmail(signup.getEmail());
            account.setPassword(passwordEncoder.encode(signup.getPassword()));
            account.setBirthday(signup.getBirthday());
            account.setGender(signup.getGender());
            account.setCreatAt(new Date());
            account.setStatus(Constants.STATUS_ACTIVE);
            account.setDeleted(Constants.DONT_DELETE);
            accountRepository.save(account);
            accountRepository.create(account.getId(),2l);
            Address address = new Address();
            address.setFullname(signup.getAddressname());
            address.setPhone(signup.getPhone());
            address.setTag_name(signup.getTag_name());
            address.setProvinceId(signup.getProvinceId());
            address.setDistrictId(signup.getDistrictId());
            address.setWardId(signup.getWardId());
            address.setAddress_detail(signup.getAddress_detail());
            address.setAccountId(account.getId());
            address.setCreatAt(new Date());
            address.setDeleted(Constants.DONT_DELETE);
            address.setStatus(Constants.STATUS_ACTIVE);
            addressRepository.save(address);
            return account;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public AuthResponse signIn(AuthRequest signin) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signin.getEmail(),signin.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.createToken(authentication);
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        AuthResponse authResponse = new AuthResponse();
        authResponse.setUsername(userPrinciple.getUsername());
        authResponse.setName(userPrinciple.getFullname());
        authResponse.setToken(token);
        authResponse.setRole(userPrinciple.getAuthorities());
        return authResponse;
    }
}
