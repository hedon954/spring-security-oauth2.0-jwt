package com.hedon.controller;

import com.hedon.dto.PriceDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@EnableGlobalMethodSecurity(prePostEnabled = true)  //别忘了这个
public class PriceController {

    //传一个 Order Id，传回相应的价格
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_PRICE')")
    public PriceDto getByOrderId(@PathVariable("id") Integer id, @AuthenticationPrincipal String username){
        System.out.println("user is " + username);
        PriceDto priceDto = new PriceDto();
        priceDto.setOrderId(id);
        priceDto.setPrice(100.11);
        return priceDto;
    }
}
