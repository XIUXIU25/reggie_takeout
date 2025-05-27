import cn.hutool.core.text.AntPathMatcher;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.Filter;
import com.reggi.common.R;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class UserFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();


    private static final String[] URLS = new String[]{
            "/employee/login",
            "/employee/logout",
            "/backend/**",
            "/front/**"
    };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI=request.getRequestURI();
        log.info("拦截到请求:{}",requestURI);

       boolean check = check(URLS, requestURI);
       if(check){
           log.info("本次请求{}不需要处理",requestURI);
           filterChain.doFilter(request,response);
           return;
       }

       //判断登录状态，如果已登录，则立刻放行
       if (request.getSession().getAttribute("employee")!=null){
           log.info("用户已登录，用户id为:{}",request.getSession().getAttribute("employee"));
           filterChain.doFilter(request,response);
           return;
        }


       //5.如果未登录，则返回登录结果，通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配
     * @param requestURI
     * @return
     */

    public boolean check(String[] urls, String requestURI) {
        for(String url : urls){
            if(PATH_MATCHER.match(url, requestURI)){
                return true;
            }
        }
        return false;
    }
}