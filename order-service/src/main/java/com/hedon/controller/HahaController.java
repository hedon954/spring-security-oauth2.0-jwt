package com.hedon.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Hedon Wang
 * @create 2020-10-12 15:26
 */
@RestController
@RequestMapping("/haha")
public class HahaController {

    @GetMapping("/haha")
    public String haha(){
        return "哈哈！我是 /haha/haha 接口，我不需要认证就可以访问啦！";
    }
}
