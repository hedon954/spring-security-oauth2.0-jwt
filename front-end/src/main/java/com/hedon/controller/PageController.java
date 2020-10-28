package com.hedon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Hedon Wang
 * @create 2020-10-14 15:48
 */
@Controller
public class PageController {

    @GetMapping("/index")
    public String index(){
        return "index";
    }


}
