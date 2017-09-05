package com.rhhcc.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rhhcc.common.cache.Cache;

/**
 * Класс-контроллер для очистки кэша
 * 
 * @author EMararu
 * @version 0.00.01
 */
@RequestMapping("/cache/clear")
@Controller
public class CacheClear {
        
    @Autowired
    @Qualifier("cacheMessage")
    private Cache cache;
        
    @RequestMapping(value = "/messages", method = {RequestMethod.GET})
    @ResponseBody
    public String messages() {
        cache.clear();
        return "OK";
    }
    
}
