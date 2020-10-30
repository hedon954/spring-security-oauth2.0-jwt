package com.hedon.controller;

import com.hedon.dto.OrderDto;
import com.hedon.dto.PriceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * @author Hedon Wang
 * @create 2020-10-12 08:32
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    /**
     * 发送请求的工具类：会帮我们自动传递 token
     */
    @Autowired
    private OAuth2RestTemplate oauth2RestTemplate;

    @PostMapping("/create")
    public OrderDto create(@RequestBody OrderDto orderDto, @AuthenticationPrincipal String username){
        System.out.println("user is " + username);
        ResponseEntity<PriceDto> entity = oauth2RestTemplate.getForEntity("http://localhost:9080/price/" + orderDto.getId(), PriceDto.class);
        PriceDto priceDto = entity.getBody();
        System.out.println(priceDto);
        return orderDto;
    }


    @GetMapping("/{id}")
    public OrderDto getById(@PathVariable("id")Integer id){
        OrderDto orderDto = new OrderDto();
        orderDto.setId(id);
        return orderDto;
    }

}
