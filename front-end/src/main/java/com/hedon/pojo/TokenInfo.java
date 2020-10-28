package com.hedon.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Hedon Wang
 * @create 2020-10-28 10:36
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TokenInfo {

    private String access_token;
    private String token_type;
    private Long expires_in;
    private String scope;
}
