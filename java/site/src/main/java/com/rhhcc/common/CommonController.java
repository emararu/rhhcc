package com.rhhcc.common;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс-контроллер для общих страниц
 * 
 * @author EMararu
 * @version 0.00.01
 */
@Controller
public class CommonController {

    private final Logger log = LoggerFactory.getLogger(CommonController.class);
    
    @RequestMapping(value = "/index", method = { RequestMethod.GET })
    public String index() {
        return "index";
    }
    
    @RequestMapping(value = "/index", method = { RequestMethod.POST })
    public String indexTile() {
        return "page.body.content.index";
    }

    
    @RequestMapping(value = { "/complete/{type}" }, method = { RequestMethod.GET })
    public String complete(@PathVariable String type, Model uiModel) {
        uiModel.addAttribute("complete_type", type);
        return "complete";
    }

    @RequestMapping(value = { "/complete/{type}" }, method = { RequestMethod.POST })
    public String completeTile(@PathVariable String type, Model uiModel) {
        uiModel.addAttribute("complete_type", type);
        return "page.body.content.complete";
    }
    
}