package com.rhhcc.test;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rhhcc.user.data.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Тестовый класс, выполняет вывод теущего состояния сессии
 * 
 * @author EMararu
 * @version 0.00.01
 */
@Controller             
public class test {

    private final Logger log = LoggerFactory.getLogger(test.class);
                
    @RequestMapping("test")
    public String test(HttpSession session) {
        log.info("* * * TEST * * *");
        User user = (User)session.getAttribute("user");
        if (user != null ) { log.info("* User *" + user.toString()); }
        
        return "test.test";
    }
}
