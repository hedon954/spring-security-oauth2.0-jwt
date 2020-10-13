package com.hedon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Hedon Wang
 * @create 2020-10-12 08:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PriceDto {
    private Integer orderId;
    private Double price;
}
