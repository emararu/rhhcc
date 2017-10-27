package com.rhhcc.user.controller;

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
import com.rhhcc.user.data.ManageUser;

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
    ManageUser manageUser;
    
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
    public String doRegisterSubmit(UserData user, Model model) {
        return manageUser.create(user).doSubmit(model);
    }
    /**
     * Выполнение регистрации пользователя через ajax запрос
     */
    @RequestMapping(value = { "/register/do" }, method = { RequestMethod.POST } )
    @ResponseBody
    public DBResult doRegister(@RequestBody UserData user) {
        return manageUser.create(user);
    }
    
    
    /**
     * Форма редактирования профиля пользователя
     */
    @RequestMapping(value = "/update", method = { RequestMethod.GET })
    public String update() {
        return "user.update";
    }    
    /**
     * Форма редактирования профиля пользователя через ajax запрос
     */
    @RequestMapping(value = "/update", method = { RequestMethod.POST })
    public String updateTile() {
        return "page.body.content.user.update";
    }
    
    
    /**
     * Подтверждение регистрации пользователя по ссылке из отправленного письма 
     * с полной загрузкой страницы 
     */
    @RequestMapping(value = { "/confirm/{user_id}/{secret}/" }, method = { RequestMethod.GET } )
    public String doCоnfirmAuthSubmit(@PathVariable long user_id, @PathVariable String secret, Model model) {
        return manageUser.confirmAuth(user_id, secret).doSubmit(model);
    }    
    /**
     * Подтверждение регистрации пользователя в случае нажатия кнопок истории 
     * перехода в браузере с загрузкой только части контента страницы 
     */
    @RequestMapping(value = { "/confirm/{user_id}/{secret}/" }, method = { RequestMethod.POST } )
    public String doCоnfirmAuthTile(@PathVariable long user_id, @PathVariable String secret, Model model) {
        return manageUser.confirmAuth(user_id, secret).doTile(model);
    }
    
    
    /**
     * Подтверждение email пользователя по ссылке из отправленного письма 
     * с полной загрузкой страницы 
     */
    @RequestMapping(value = { "/confirm_email/{user_id}/{secret}/" }, method = { RequestMethod.GET } )
    public String doCоnfirmEmailSubmit(@PathVariable long user_id, @PathVariable String secret, Model model) {
        return manageUser.confirmEmail(user_id, secret).doSubmit(model);
    }    
    
    /**
     * Подтверждение email пользователя в случае нажатия кнопок истории 
     * перехода в браузере с загрузкой только части контента страницы 
     */
    @RequestMapping(value = { "/confirm_email/{user_id}/{secret}/" }, method = { RequestMethod.POST } )
    public String doCоnfirmEmailTile(@PathVariable long user_id, @PathVariable String secret, Model model) {
        return manageUser.confirmEmail(user_id, secret).doTile(model);
    }
    
    
    /**
     * Подтверждение телефонного номера пользователя
     */
    @RequestMapping(value = { "/confirm_phone/{user_id}/{verify}/" }, method = { RequestMethod.POST } )
    public DBResult doCоnfirmPhone(@PathVariable long user_id, @PathVariable String verify) {
        return manageUser.confirmPhone(user_id, verify);
    }
    
    
    /**
     * Выполнение аутентификации пользователя через ajax запрос
     */
    @RequestMapping(value = { "/login/do" }, method = { RequestMethod.POST } )
    @ResponseBody
    public DBResult doLogin(@RequestBody ObjectNode json) {
        
        String login    = json.get("login").asText();
        String password = json.get("password").asText(); 
        
        return manageUser.login(login, DigestUtils.md5Hex(password));
    }
    
}
