package com.hedon.filter;

import com.hedon.pojo.TokenInfo;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基于 Cookie 的 SSO
 *
 * @author Hedon Wang
 * @create 2020-10-28 11:29
 */
@Component
public class CookieTokenFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    private RestTemplate restTemplate = new RestTemplate();


    /**
     * 从 session 中拿到 token 并加到请求头
     * @return
     * @throws ZuulException
     */
    @SneakyThrows
    @Override
    public Object run() throws ZuulException {

        //拿到请求
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        HttpServletResponse response = requestContext.getResponse();

        //拿 accessToken
        String accessToken = getCookie("cookie_access_token");

        //如果能拿到 accessToken，就将它加到请求头里面
        if (StringUtils.isNotBlank(accessToken)){
            requestContext.addZuulRequestHeader("Authorization","bearer "+accessToken);
        }else{
            //如果没拿到，说明 accessToken 过期了,这个时候尝试去拿 refreshToken
            String refreshToken = getCookie("cookie_refresh_token");
            if (StringUtils.isNotBlank(refreshToken)){
                //如果拿得到，就去认证服务器发送刷新令牌的请求
                String oauthServiceUrl = "http://localhost:9527/token/oauth/token";
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                httpHeaders.setBasicAuth("frontEnd","123456");
                MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
                params.add("grant_type","refresh_token");
                params.add("refresh_token",refreshToken);
                HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(params,httpHeaders);
                try{
                    //尝试刷新拿到新的令牌
                    ResponseEntity<TokenInfo> newToken = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, TokenInfo.class);
                    TokenInfo newTokenInfo = newToken.getBody().init();

                    //把新的 accessToken 放到请求头里面，传下去
                    requestContext.addZuulRequestHeader("Authorization","bearer "+newTokenInfo.getAccess_token());

                    //拿到的话也是要放到 cookie 中

                    /**
                     * 存到 access_token 到 cookie 中
                     */

                    //放 access_token
                    Cookie accessTokenCookie = new Cookie("cookie_access_token",newTokenInfo.getAccess_token());
                    //cookie 有效期
                    accessTokenCookie.setMaxAge(newTokenInfo.getExpires_in().intValue() - 3);
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
                    Cookie refreshTokenCookie = new Cookie("cookie_refresh_token",newTokenInfo.getRefresh_token());
                    //cookie 有效期
                    refreshTokenCookie.setMaxAge(259200);
                    //一级域名
                    refreshTokenCookie.setDomain("localhost");
                    //根目录
                    refreshTokenCookie.setPath("/");
                    //将 token 放到响应体中
                    response.addCookie(refreshTokenCookie);

                }catch (Exception e){
                    //刷新失败了，说明令牌也是过期了
                    requestContext.setSendZuulResponse(false);
                    requestContext.setResponseStatusCode(10086);
                    requestContext.setResponseBody("{\"message\": \"refresh failed!\"}");
                    requestContext.getResponse().setContentType("application/json");
                }
            }else{
                //如果没拿到，那就需要抛出10086异常提醒用户登录了
                requestContext.setSendZuulResponse(false);
                requestContext.setResponseStatusCode(10086);
                requestContext.setResponseBody("{\"message\": \"refresh failed!\"}");
                requestContext.getResponse().setContentType("application/json");
            }
        }

        return null;
    }

    /**
     * 从 cookie 中拿到 token
     * @param name
     * @return
     */
    private String getCookie(String name) {
        String result = null;
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        //拿到所有的 cookie
        Cookie[] cookies = request.getCookies();
        //遍历找到我们要的 cookie
        for (Cookie cookie : cookies){
            if (StringUtils.equals(name,cookie.getName())){
                result = cookie.getValue();
            }
        }
        return result;
    }
}
