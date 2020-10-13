package com.hedon.controller;

import com.hedon.bean.User;
import com.hedon.dto.OrderDto;
import com.hedon.dto.PriceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * @author Hedon Wang
 * @create 2020-10-12 08:32
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    private RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/create")
    public OrderDto create(@RequestBody OrderDto orderDto, @AuthenticationPrincipal User user){
        System.out.println("user is " + user);
        ResponseEntity<PriceDto> entity = restTemplate.getForEntity("http://localhost:9080/price/" + orderDto.getId(), PriceDto.class);
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
