package com.hedon.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义一个拦截器，来拦截刷新令牌失败的情况
 *
 * @author Hedon Wang
 * @create 2020-10-29 20:21
 */
@Component
public class RefreshTokenInterceptor  implements HandlerInterceptor {

    /**
     * 在请求之前拦截请求
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        final Integer REFRESH_FAIL = 10086;

        /**
         * 状态码为 500
         * 请求体信息为 refresh failed
         *
         * 上面两个结合起来，那就是刷新失败了
         */
        if (response.getStatus() == REFRESH_FAIL){
            System.out.println("刷新失败啦！！！");
            //强制重新登录
            request.getSession().invalidate();
            request.getRequestDispatcher("/index").forward(request,response);
            return false;
        }

        //没失败就放行
        return true;
    }
}
