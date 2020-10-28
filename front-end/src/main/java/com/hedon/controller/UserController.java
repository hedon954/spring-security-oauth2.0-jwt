package com.hedon.controller;

import com.hedon.constants.ResultCode;
import com.hedon.pojo.LoginUser;
import com.hedon.pojo.TokenInfo;
import com.hedon.result.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resources;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Hedon Wang
 * @create 2020-10-28 10:28
 */
@RestController
public class UserController {

    private RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/login")
    public CommonResult login(@RequestParam("username")String username,@RequestParam("password")String password, HttpServletRequest request){

        try{
            //去获取token
            String oauthServiceUrl = "http://localhost:9527/token/oauth/token";

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            httpHeaders.setBasicAuth("frontEnd","123456");
            MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
            params.add("username",username);
            params.add("password",password);
            params.add("grant_type","password");
            params.add("scope","write read");
            HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(params,httpHeaders);
            ResponseEntity<TokenInfo> response = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, TokenInfo.class);
            //放到请求域中
            request.getSession().setAttribute("token",response.getBody());
            return CommonResult.success().add("tokenInfo",response.getBody());
        }catch (Exception e){
            return CommonResult.fail(ResultCode.USER_PWD_WRONG);
        }
    }

    @PostMapping("/logout")
    public CommonResult logout(HttpServletRequest request){
        request.getSession().invalidate();
        return CommonResult.success();
    }

    @PostMapping("/checkLogin")
    public CommonResult checkLogin(HttpServletRequest request){
        try {
            TokenInfo tokenInfo = (TokenInfo)request.getSession().getAttribute("token");
            if (tokenInfo != null){
                return CommonResult.success().add("tokenInfo",tokenInfo);
            }else{
                return CommonResult.fail(ResultCode.NO_LOGIN);
            }
        }catch (Exception e){
            e.printStackTrace();
            return CommonResult.fail(ResultCode.NO_LOGIN);
        }
    }

}
