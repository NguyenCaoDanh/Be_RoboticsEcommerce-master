package com.wisdom.roboticsecommerce.Security.UserPrincical;

import com.wisdom.roboticsecommerce.Entities.Account;
import com.wisdom.roboticsecommerce.Repositories.AccountRepository;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class Userdetail implements UserDetailsService {
    @Autowired
    private AccountRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account user = userRepository.findByEmail(username);
        if (user == null){
            throw new MessageDescriptorFormatException("Không tồn tại user");
        }
        return UserPrinciple.build(user);
    }
}
