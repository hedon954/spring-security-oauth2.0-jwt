package com.hedon.filter;

import com.hedon.bean.TokenInfo;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 授权
 *
 * @author Hedon Wang
 * @create 2020-10-13 17:17
 */
@Component
public class AuthorizationFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    //在"进来的审计"后面
    @Override
    public int filterOrder() {
        return 3;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    //授权
    @Override
    public Object run() throws ZuulException {
        System.out.println("授权开始=============>>>>>>>>>>>>>>>>>>");
        
        //拿到当前请求上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        //拿到请求体
        HttpServletRequest request = requestContext.getRequest();

        /**
         * ============================================
         *                  下面开始授权
         * ============================================
         */
        //① 该请求是否需要权限
        if (isNeedAuth(request)){
            //② 需要认证，那就拿出 tokenInfo
            TokenInfo tokenInfo = (TokenInfo) request.getAttribute("tokenInfo");
            //③ tokenInfo 是否存在，存在是否可用
            if (tokenInfo != null && tokenInfo.isActive()){
                //④ 认证成功（能拿到用户信息且有效） => 是否有权限
                if (hasPermissions(tokenInfo,request)){
                    //有权限就放行
                    //不仅放行，还将用户信息放在 requestHeader 中，让接口可以获取到用户相关的信息
                    requestContext.addZuulRequestHeader("username",tokenInfo.getUser_name());
                }else{
                    //没有权限，记录审计日志
                    System.out.println("异常审计===============>>>>>403 错误，授权失败....");
                    handleError(403,requestContext);
                }
            }else{
                /**
                 * tokenInfo 不存在或者不可用情况一：本来就不需要携带
                 */
                if (StringUtils.startsWith(request.getRequestURI(),"/token")){
                    // '/token' 开头的请求放行
                }else{
                    /**
                     * tokenInfo 不存在或者不可用情况二：需要带来认证
                     *      不存在：没传 tokenInfo
                     *      不可用：token 过期或无效
                     */
                    System.out.println("异常审计=============>>>>>401 错误，认证失败...");
                    //处理异常
                    handleError(401,requestContext);
                }
            }
        }

        /**
         * 进到下一个过滤器，3种情况：
         *
         *  1. isNeedAuth 返回 false，也就是请求不需要认证授权
         *  2. 到 ④ 后检查到用户是有权限的，就正常放行
         *  3. 请求路径是以 /token 开头的，其实这也是不需要认证授权的情况
         *
         */
        return null;
    }

    //判断是否拥有权限
    private boolean hasPermissions(TokenInfo tokenInfo,HttpServletRequest request) {
        //这里按道理是要查询数据库然后进行对比的，这里我们随意些，就给50%的几率可以通过
        return RandomUtils.nextInt() % 2 == 0;
    }

    //处理异常 => 发送异常信息，并且停止前行
    private void handleError(int status, RequestContext requestContext) {
        requestContext.getResponse().setContentType("application/json");
        requestContext.setResponseStatusCode(status);
        requestContext.setResponseBody("{\"message: \": \"auth failed.\"}");
        //不要往后走了
        requestContext.setSendZuulResponse(false);
    }

    //判断当前请求是否需要认证
    private boolean isNeedAuth(HttpServletRequest request) {
        //这里简单搞，都要经过认证
        return true;
    }
}
