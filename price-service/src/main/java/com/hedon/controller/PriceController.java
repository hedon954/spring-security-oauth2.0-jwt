package com.hedon.controller;

import com.hedon.dto.PriceDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Hedon Wang
 * @create 2020-10-12 08:41
 */
@RestController
@RequestMapping("/price")
public class PriceController {

    //传一个 Order Id，传回相应的价格
    @GetMapping("/{id}")
    public PriceDto getByOrderId(@PathVariable("id") Integer id){
        PriceDto priceDto = new PriceDto();
        priceDto.setOrderId(id);
        priceDto.setPrice(100.11);
        return priceDto;
    }
}
