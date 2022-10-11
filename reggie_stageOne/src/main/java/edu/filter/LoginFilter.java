package edu.filter;

import com.alibaba.fastjson.JSON;
import edu.common.BaseThreadLocal;
import edu.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
@WebFilter(filterName = "loginFilter",urlPatterns = "/*")
public class LoginFilter implements Filter {
    //用来进行路径匹配
    public static final AntPathMatcher pathMatcher = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        /*
        * 1、doFilter参数强制转换成HttpServlet类型
        * 2、获得请求的URI地址，并设置放行的url模式，使用AntPathMatcher进行路径匹配
        * 3、判断是否在放行的url数组中，是则放行
        * 4、判断是否含有登录的用户session信息，是则放行
        * 5、返回失败消息，由前端跳转到login页面*/


        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        String[] urls = {"/backend/**","/front/**","/employee/login","/employee/logout","/user/sendMsg","/user/login"};
        boolean check = check(urls, requestURI);
        if(check){
            filterChain.doFilter(request,response);
            return;
        }
        Object employee = request.getSession().getAttribute("employee");
        if(employee != null){
            BaseThreadLocal.set((Long) employee);
            filterChain.doFilter(request,response);
            return;
        }
        Object user = request.getSession().getAttribute("user");
        if(user != null){
            BaseThreadLocal.set((Long) user);
            filterChain.doFilter(request,response);
            return;
        }

        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }
    public boolean check(String[] urls,String URI){
        for (String url:urls){
            boolean match = pathMatcher.match(url, URI);
            if(match) return  true;
        }
        return false;
    }
}
