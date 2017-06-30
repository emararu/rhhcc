package com.rhhcc.user.auth;

import com.rhhcc.user.data.User;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Общий класс, для всех доступных типов аутентификация пользователя в системе
 * 
 * @author  EMararu
 * @version 0.00.01
 */
@Service("authService")
public class AuthService implements Auth{
    
    private final Logger log = LoggerFactory.getLogger(OAuthSocial.class);
    
    @Autowired
    private HttpSession session;
    
    @Override
    public boolean is() {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       return (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated());
    }
    
    @Override
    public void process(User user) {
        // Сохранение данных пользователя на время существования сессии
        session.setAttribute("user", user);
        // Принудительная аутентификация пользователя в spring-security
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getName(), user.getId(), AuthorityUtils.createAuthorityList("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    
}
