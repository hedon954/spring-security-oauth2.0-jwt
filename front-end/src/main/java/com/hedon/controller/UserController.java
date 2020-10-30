package com.hedon.controller;

import com.hedon.constants.ResultCode;
import com.hedon.pojo.TokenInfo;
import com.hedon.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Hedon Wang
 * @create 2020-10-28 10:28
 */
@RestController
@Slf4j
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
            //放到请求域中，放进去之前需要初始化，设置过期时间
            request.getSession().setAttribute("token",response.getBody().init());
            System.out.println(response.getBody().init());
            return CommonResult.success().add("tokenInfo",response.getBody().init());
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
            //先尝试看能不能从 session 中拿到 token
            TokenInfo tokenInfo = (TokenInfo)request.getSession().getAttribute("token");
            if (tokenInfo != null){
                return CommonResult.success().add("tokenInfo",tokenInfo);
            }else{
                //如果 session 中没有，那就再试试看能不能从 cookie 中找到
                Cookie[] cookies = request.getCookies();
                for (Cookie cookie: cookies){
                    if (StringUtils.equals("cookie_access_token",cookie.getName())){
                        //找到了就说明已经登录了
                        return CommonResult.success();
                    }
                }
                //都没找到的话，就说没登录
                return CommonResult.fail(ResultCode.NO_LOGIN);
            }
        }catch (Exception e){
            e.printStackTrace();
            return CommonResult.fail(ResultCode.NO_LOGIN);
        }
    }


    /**
     * 回调接口
     *
     * @param code         认证服务器返回的授权码，必须
     * @param state        我们自定义的返回的客户端状态信息，可选
     * @param request
     */
    @GetMapping("/oauth/callback")
    public void callBack(@RequestParam("code") String code, String state, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //日志输出 state 状态信息
        log.info("state is "+ state);
        //去认证服务器拿token，还是走网关
        String oauthServiceUrl = "http://localhost:9527/token/oauth/token";
        //请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth("frontEnd","123456");
        /**
         * 参数:
         * 1. 授权码
         * 2. 授权模式：授权码模式authorization_code
         * 3. 重定向 uri：要跟前端的一样
         */
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("code",code);
        params.add("grant_type","authorization_code");
        params.add("redirect_uri","http://localhost:9060/oauth/callback");
        //封装请求体
        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(params,headers);
        //发送请求
        ResponseEntity<TokenInfo> token = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, TokenInfo.class);
        /*
        //存到 session 中
        request.getSession().setAttribute("token",token.getBody().init());
         */

        /**
         * 存到 access_token 到 cookie 中
         */

        //放 access_token
        Cookie accessTokenCookie = new Cookie("cookie_access_token",token.getBody().getAccess_token());
        //cookie 有效期
        accessTokenCookie.setMaxAge(token.getBody().getExpires_in().intValue() - 3);
        //一级域名
        accessTokenCookie.setDomain("localhost");
        //根目录
        accessTokenCookie.setPath("/");
        //将 cookie 放到响应体中
        response.addCookie(accessTokenCookie);

        /**
         * 存到 refresh_token 到 cookie 中
         */

        //放 refresh_token
        Cookie refreshTokenCookie = new Cookie("cookie_refresh_token",token.getBody().getRefresh_token());
        //cookie 有效期
        refreshTokenCookie.setMaxAge(259200);
        //一级域名
        refreshTokenCookie.setDomain("localhost");
        //根目录
        refreshTokenCookie.setPath("/");
        //将 token 放到响应体中
        response.addCookie(refreshTokenCookie);

        //跳转回浏览器
        response.sendRedirect("/index");
    }
}
