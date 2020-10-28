package com.hedon.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Hedon Wang
 * @create 2020-10-28 10:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginUser {

    private String username;
    private String password;
}
