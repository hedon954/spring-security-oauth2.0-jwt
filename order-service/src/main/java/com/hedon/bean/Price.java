package com.hedon.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Hedon Wang
 * @create 2020-10-12 08:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Price {

    private Integer orderId;
    private Double price;
}
