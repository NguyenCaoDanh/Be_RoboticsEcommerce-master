package com.wisdom.roboticsecommerce.Security.jwt;


import com.wisdom.roboticsecommerce.Security.UserPrincical.Userdetail;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private Userdetail userdetail;
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = getJwt(request);
            if(token != null && jwtProvider.validateToken(token)){
                String userName =jwtProvider.getUsernameFromToken(token);
                UserDetails userDetails = userdetail.loadUserByUsername(userName);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            logger.error("Can't set user authentication -> Message: {}",e);
        }
        filterChain.doFilter(request,response);
    }
    private String getJwt(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }
        return null;
    }
}
