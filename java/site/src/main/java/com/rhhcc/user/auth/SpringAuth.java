package com.rhhcc.user.auth;

import java.util.ArrayList;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.rhhcc.user.data.User;
import com.rhhcc.user.data.UserData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс для "аутентификации" в Spring Security
 * 
 * @author  EMararu
 * @version 0.00.01
 */
@Service("springAuth")
public class SpringAuth {
    
    private final Logger log = LoggerFactory.getLogger(SpringAuth.class);
    
    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    
    /**
     * Проверка аутентификации пользователя в системе
     * @return True - Пользователь аутентифицирован, False - Пользователь не аутентифицирован
     */
    public boolean isAuth() {
        final Authentication authentication = this.getAuth();
        return (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated());
    }
    
    /**
     * Данные текущего аутентифицированного пользователя
     * @return Данные пользователя
     */
    public User getCurrent() {
        return (UserData)this.getAuth().getPrincipal();
    }
    
    /**
     * Выполняет аутентификация пользователя в spring security
     * @param user      Данные пользователя
     * @param privilege Привилегии пользователя
     */
    public void process(User user, ArrayList<String> privilege) {
        // Принудительная аутентификация пользователя в spring-security
        final Authentication authentication = new UsernamePasswordAuthenticationToken(user, "", AuthorityUtils.createAuthorityList(privilege.toArray(new String[privilege.size()])));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    
}
