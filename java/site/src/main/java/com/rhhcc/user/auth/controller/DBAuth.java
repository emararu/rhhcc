package com.rhhcc.user.auth.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rhhcc.common.type.DBResult;
import com.rhhcc.user.data.UserData;
import com.rhhcc.user.data.Manage;

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
public class DBAuth {
    
    private final Logger log = LoggerFactory.getLogger(DBAuth.class);    
    
    @Autowired
    @Qualifier("manageUser")
    Manage manageUser;
    
    /**
     * Форма регистрации
     */
    @RequestMapping(value = "/register", method = { RequestMethod.GET })
    public String register() {
        return "user.register";
    }    
    /**
     * Форма регистрации через ajax запрос
     */
    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    public String registerTile() {
        return "page.body.content.user.register";
    }

    
    /**
     * Выполнение регистрации пользователя путем передачи данных со страницы с полной перезагрузкой
     */
    @RequestMapping(value = { "/register/do/submit" }, method = { RequestMethod.POST } )
    public String doRegisterSubmit(final UserData user, Model model) {
        return manageUser.create(user).doSubmit(model);
    }        
    
    /**
     * Выполнение регистрации пользователя через ajax запрос
     */
    @RequestMapping(value = { "/register/do" }, method = { RequestMethod.POST } )
    @ResponseBody
    public DBResult doRegister(@RequestBody final UserData user) {
        return manageUser.create(user);
    }
    
    
    /**
     * Подтверждение регистрации пользователя по ссылке из отправленного письма 
     * с полной загрузкой страницы 
     */
    @RequestMapping(value = { "/confirm/{user_id}/{secret}/" }, method = { RequestMethod.GET } )
    public String doCоnfirmSubmit(@PathVariable final long user_id, @PathVariable final String secret, Model model) {
        return manageUser.confirm(user_id, secret).doSubmit(model);
    }    
    
    /**
     * Подтверждение регистрации пользователя в случае нажатия кнопок истории 
     * перехода в браузере с загрузкой только части контента страницы 
     */
    @RequestMapping(value = { "/confirm/{user_id}/{secret}/" }, method = { RequestMethod.POST } )
    public String doCоnfirmTile(@PathVariable final long user_id, @PathVariable final String secret, Model model) {
        return manageUser.confirm(user_id, secret).doTile(model);
    }
    
    
    /**
     * Выполнение аутентификации пользователя через ajax запрос
     */
    @RequestMapping(value = { "/login/do" }, method = { RequestMethod.POST } )
    @ResponseBody
    public DBResult doLogin(@RequestBody final ObjectNode json) {
        
        String login    = json.get("login").asText();
        String password = json.get("password").asText(); 
        
        return manageUser.login(login, DigestUtils.md5Hex(password));
    }
    
}
