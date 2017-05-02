package com.rhhcc.user.auth;

import java.sql.SQLException;

import com.rhhcc.user.data.UserData;
import com.rhhcc.user.data.Manage;
import com.rhhcc.user.data.ManageUser;
        
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author EMararu
 */
@RequestMapping("/user")
@Controller
public class DBAuthController {
    
    private final Logger log = LoggerFactory.getLogger(DBAuthController.class);    
    
    @Autowired
    @Qualifier("manageUser")
    Manage mUser;
    
    @RequestMapping(value = "/register", method = { RequestMethod.GET })
    public String register() {
        return "user.register";
    }
    
    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    public String registerTile() {
        return "page.body.content.user.register";
    }
    
    /**
     * Запрос на регистрацию в системе при полной поддержке браузером html5
     */
    @RequestMapping(value = { "/register/do" }, method = { RequestMethod.POST } )
    @ResponseBody
    public String doRegister(@RequestBody final UserData user) {
        log.info("[Register]" + user.toString());
        try {
            long r = mUser.create(user);
        } catch (SQLException e) {
            log.info("Error:"+e.getMessage());
        }
        return "{ \"result\": \"0\", \"complete\": \"register\", \"error\": \"\" }";
    }
    
    /**
     * Запрос на регистрацию в системе если браузер не поддерживает History и требуется перезагрузка страницы
     */
    @RequestMapping(value = { "/register/do/submit" }, method = { RequestMethod.POST } )
    public String doSubmitRegister(final UserData user) {
        log.info("[Register]" + user.toString());
        return "forward:/complete/register/submit";
    }
    
}
