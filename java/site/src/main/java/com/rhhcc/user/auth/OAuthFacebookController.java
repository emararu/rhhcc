package com.rhhcc.user.auth;

import java.io.IOException;
import javax.servlet.http.HttpSession;

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
 * Класс-контроллер аутентификации пользователя в системе через Facebook
 * 
 * @author EMararu
 * @version 0.00.01
 */
@RequestMapping("/user/auth/facebook")
@Controller
public class OAuthFacebookController {

    private final Logger log = LoggerFactory.getLogger(OAuthFacebookController.class);
    
    @Autowired
    @Qualifier("openAuthFacebook")
    private OAuth auth;
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "redirect:" + auth.login();
    }
        
    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public String data(HttpSession session, Model model, @RequestParam(value = "code", required = false) String code, @RequestParam(value = "state", required = false) String state, @RequestParam(value = "error", required = false) String error) throws IOException {
        return auth.data(session, model, code, state, error);
    }
    
}