package com.hedon.controller;

import com.hedon.dto.OrderDto;
import com.hedon.dto.PriceDto;
import org.springframework.http.ResponseEntity;
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
    public OrderDto create(@RequestBody OrderDto orderDto, @RequestHeader String username){
        System.out.println("user is " + username);
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
