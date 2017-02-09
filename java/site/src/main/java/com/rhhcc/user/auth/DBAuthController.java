package com.rhhcc.user.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author EMararu
 */
@RequestMapping("/user/auth")
@Controller
public class DBAuthController {
    
    private final Logger log = LoggerFactory.getLogger(DBAuthController.class);
        
    @RequestMapping(value = "/create", method = { RequestMethod.GET })
    public String create() {
        return "user.auth.create";
    }
    
    @RequestMapping(value = "/create", method = { RequestMethod.POST })
    public String createTile() {
        return "page.body.content.user.auth.create";
    }
}
