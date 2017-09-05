package com.rhhcc.user.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rhhcc.common.cache.Cache;
import com.rhhcc.common.type.DBResult;
import com.rhhcc.user.auth.SpringAuth;

/**
 * Класс-контроллер аутентификации пользователя в системе вне зависимости от способа аутентификации
 * 
 * @author EMararu
 * @version 0.00.01
 */
@RequestMapping("/user/auth")
@Controller
public class Auth {
    
    @Autowired
    @Qualifier("cacheMessage")
    private Cache cache;
    
    @Autowired
    @Qualifier("springAuthService")
    private SpringAuth springAuth;
    
    @RequestMapping(value = "/success-logout", method = {RequestMethod.GET})
    @ResponseBody
    public DBResult successLogout() {
        return cache.get(3);
    }
    
    @RequestMapping(value = "/tile-icon-auth", method = {RequestMethod.POST})
    public String tileIconAuth() {
       return (springAuth.isAuth() ? "page.body.header.icon.logout" : "page.body.header.icon.login");
    }
    
    @RequestMapping(value = "/tile-menu-auth", method = {RequestMethod.POST})
    public String tileMenuAuth() { 
       return (springAuth.isAuth() ? "page.body.header.logout" : "page.body.header.login");
    }
    
}
