package com.rhhcc.user.auth;

import com.rhhcc.user.data.User;

import java.util.ArrayList;
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
@Service("springAuthService")
public class SpringAuthService implements SpringAuth {
    
    private final Logger log = LoggerFactory.getLogger(SpringAuthService.class);
    
    @Autowired
    private HttpSession session;
    
    @Override
    public boolean isAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated());
    }
        
    @Override
    public boolean isOther() {
        boolean result = true;
        //User user = (User)session.getAttribute("user");
        //if (user != null) { result = user.getId() != (long)session.getAttribute("last_id"); }
        return result;
    }
    
    @Override
    public void process(User user, ArrayList<String> privilege) {
        // Пользовать который ранее вошел в систему
        //User oldUser = (User)session.getAttribute("user");
        // Проверка перерегистрации пользователя
        //if (oldUser != null && oldUser.getId() != user.getId()) session.setAttribute("user_changed", 1);        
        // Сохранение данных пользователя на время существования сессии
        session.setAttribute("user", user);
        // Принудительная аутентификация пользователя в spring-security
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getName(), user.getId(), AuthorityUtils.createAuthorityList(privilege.toArray(new String[privilege.size()])));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    
}
