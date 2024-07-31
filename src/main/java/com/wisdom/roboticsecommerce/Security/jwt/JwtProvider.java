package com.wisdom.roboticsecommerce.Security.jwt;

import com.wisdom.roboticsecommerce.Security.UserPrincical.UserPrinciple;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
@Slf4j
public class JwtProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
    private String jwtSecret = "secret";
    private int jwtExpiration = 7 * 24 * 60 * 60 * 1000;
    //Tạo Token
    public String createToken(Authentication authentication){
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        return Jwts.builder().setSubject(userPrinciple.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512,jwtSecret)
                .compact();
    }
    //Check token hợp lệ hay không
    public Boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e){
            logger.error("Expired JWT token -> Message: {}",e);
        } catch (MalformedJwtException e) {
            logger.error("Invalid format token -> Message: {}",e);
        } catch (UnsupportedJwtException e){
            logger.error("Unsupported JWT token -> Message: {}",e);
        } catch (IllegalArgumentException e){
            logger.error("JWT claims string is empty -> Message: {}",e);
        }
        return false;
    }
    //Tìm tên token
    public String getUsernameFromToken(String token){
        String username = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
        return username;
    }
}
