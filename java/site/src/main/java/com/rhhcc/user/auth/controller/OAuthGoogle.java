package com.rhhcc.user.auth.controller;

import com.rhhcc.user.auth.OAuth;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс-контроллер аутентификации пользователя в системе через Google+
 * 
 * @author EMararu
 * @version 0.00.01
 */
@RequestMapping("/user/auth/google")
@Controller             
public class OAuthGoogle {

    private final Logger log = LoggerFactory.getLogger(OAuthGoogle.class);
    
    @Autowired
    @Qualifier("openAuthGoogle")
    private OAuth auth;
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "redirect:" + auth.login();
    }
        
    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public String data(Model model, @RequestParam(value = "code", required = false) String code, @RequestParam(value = "state", required = false) String state, @RequestParam(value = "error", required = false) String error) throws IOException {
        return auth.data(model, code, state, error);
    }
}
