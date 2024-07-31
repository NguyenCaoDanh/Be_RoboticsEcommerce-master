package com.wisdom.roboticsecommerce.Service;

import com.wisdom.roboticsecommerce.Dto.AuthRequest;
import com.wisdom.roboticsecommerce.Dto.AuthResponse;
import com.wisdom.roboticsecommerce.Dto.Signup;
import com.wisdom.roboticsecommerce.Entities.Account;

public interface AuthService {
    Account signUp(Signup signup);
    AuthResponse signIn(AuthRequest signin);
}
