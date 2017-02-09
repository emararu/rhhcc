package com.rhhcc.user.auth;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс-контроллер аутентификации пользователя в системе вне зависимости от способа аутентификации
 * 
 * @author EMararu
 * @version 0.00.01
 */
@RequestMapping("/user/auth")
@Controller
public class AuthController {

    private final Logger log = LoggerFactory.getLogger(AuthController.class);
    
    /**
     * Проверка аутентификации пользователя в системе
     * @return True - Пользователь аутентифицирован, False - Пользователь не аутентифицирован
     */
    private boolean isAuth() {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       return (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated());
    }
    
    @RequestMapping(value = "/success-logout", method = {RequestMethod.GET})
    @ResponseBody
    public void successLogout() {
    }
    
    @RequestMapping(value = "/tile-icon-auth", method = {RequestMethod.POST})
    public String tileIconAuth() {
       return (isAuth() ? "page.body.header.icon.logout" : "page.body.header.icon.login");
    }
    
    @RequestMapping(value = "/tile-menu-auth", method = {RequestMethod.POST})
    public String tileMenuAuth() { 
       return (isAuth() ? "page.body.header.logout" : "page.body.header.login");
    }
    
}
