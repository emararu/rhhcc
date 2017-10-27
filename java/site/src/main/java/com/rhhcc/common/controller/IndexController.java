package com.rhhcc.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс-контроллер для главной страницы
 * 
 * @author EMararu
 * @version 0.00.01
 */
@Controller
public class IndexController {

    private final Logger log = LoggerFactory.getLogger(IndexController.class);
    
    @RequestMapping(value = "/index", method = { RequestMethod.GET })
    public String index() {
        return "index";
    }
    
    @RequestMapping(value = "/index", method = { RequestMethod.POST })
    public String indexTile() {
        return "page.body.content.index";
    }
    
}