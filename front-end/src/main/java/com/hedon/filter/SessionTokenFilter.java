package com.hedon.filter;

import com.hedon.pojo.TokenInfo;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Hedon Wang
 * @create 2020-10-28 11:29
 */
@Component
public class SessionTokenFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }


    /**
     * 从 session 中拿到 token 并加到请求头
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {

        //拿到请求
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        //从请求中拿到 tokenInfo
        TokenInfo token = (TokenInfo) request.getSession().getAttribute("token");
        //检查是否为空
        if (token!=null){
            requestContext.addZuulRequestHeader("Authorization","bearer "+token.getAccess_token());
        }
        return null;
    }
}
