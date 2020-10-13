package com.hedon.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

/**
 * 审计
 *
 * @author Hedon Wang
 * @create 2020-10-13 17:14
 */
@Component
public class AuditLogFilter extends ZuulFilter {

    //在请求进来前执行
    @Override
    public String filterType() {
        return "pre";
    }

    //在认证后面
    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    //审计
    @Override
    public Object run() throws ZuulException {
        System.out.println("请求进入审计开始 =======>>>>>>>>>>>");
        return null;
    }
}
