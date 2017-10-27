package com.rhhcc.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс-контроллер для страницы результата обработки запроса пользователя
 * 
 * @author EMararu
 * @version 0.00.01
 */
@Controller
public class ResultController {

    private final Logger log = LoggerFactory.getLogger(ResultController.class);
    
    
    @RequestMapping(value = { "/complete/{type}" }, method = { RequestMethod.GET })
    public String complete(@PathVariable String type, Model model) {
        model.addAttribute("method_type", type);
        return "complete";
    }

    @RequestMapping(value = { "/complete/{type}" }, method = { RequestMethod.POST })
    public String completeTile(@PathVariable String type, Model model) {
        model.addAttribute("method_type", type);
        return "page.body.content.complete";
    }

    
    @RequestMapping(value = { "/failure/{type}" }, method = { RequestMethod.GET })
    public String failure(@PathVariable String type, Model model) {
        model.addAttribute("method_type", type);
        return "faluire";
    }

    @RequestMapping(value = { "/faluire/{type}" }, method = { RequestMethod.POST })
    public String failureTile(@PathVariable String type, Model model) {
        model.addAttribute("method_type", type);
        return "page.body.content.failure";
    }
    
}