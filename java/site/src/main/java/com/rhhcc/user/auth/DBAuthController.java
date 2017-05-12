package com.rhhcc.user.auth;

import com.rhhcc.common.type.DBResult;
import com.rhhcc.user.data.UserData;
import com.rhhcc.user.data.Manage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Контроллер для работы с пользователями БД
 * 
 * @author EMararu
 * @version 0.00.01
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
        

    @RequestMapping(value = { "/register/do" }, method = { RequestMethod.POST } )
    @ResponseBody
    public DBResult doRegister(@RequestBody final UserData user) {
        return mUser.create(user);
    }

    @RequestMapping(value = { "/register/do/submit" }, method = { RequestMethod.POST } )
    public String doSubmitRegister(final UserData user, Model uiModel) {
        DBResult result = mUser.create(user);
        if (result.getId() >= 0) {
            return "forward:"+result.getPath();
        } else {
            uiModel.addAttribute("complete_type", result.getComplete().name());
            uiModel.addAttribute("error_descr", result.getText());
            return "complete";
        }
    }
    
}
