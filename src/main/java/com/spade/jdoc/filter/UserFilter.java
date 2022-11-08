package com.spade.jdoc.filter;

import com.spade.jdoc.model.User;
import com.spade.jdoc.service.UserService;
import com.spade.jdoc.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
@WebFilter(urlPatterns = "/*", filterName = "filterToken")
public class UserFilter implements Filter {

    private static final Set<String> ALLOWED_PATHS = Set.of(
            "/auth/login", "/auth/logout", "/auth/register"
            , "/auth/swagger-ui", "/v3/api-docs"
            , "/h2-console"
            , "favicon.ico"
    );

    @Autowired
    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        System.out.println("----UserFilter过滤器初始化----");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("/+$", "");
        System.out.println("----UserFilter current path " + path + "----");
        boolean flag = false;
        for (String sub : ALLOWED_PATHS) {
            if (path.indexOf(sub) == 0) {
                flag = true;
                break;
            }
        }
        if (flag || ALLOWED_PATHS.contains(path)) {
            System.out.println("----UserFilter run in allow path----");
            filterChain.doFilter(servletRequest, servletResponse); // 让目标资源执行，放行
            return;
        }
        String key = request.getHeader("Authorization");
        if (key == null || key.isEmpty()) {
            key = request.getParameter("token");
        }
        // query user
        var user = findUser(key);
        if (user == null) {
            System.out.println("----UserFilter rollback in token error ----" + key);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("token error");
            return;
        }
        boolean check = true;//暂时不验证权限，只要登录了就有所有权限。
        //check user path.
        if (check) {
            System.out.println("----UserFilter run in check allow----");
            CommonUtils.cacheMap.put("user", user);
            filterChain.doFilter(servletRequest, servletResponse); // 让目标资源执行，放行
            return;
        }
        System.out.println("----UserFilter rollback in check error ----");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("not allow");
    }

    @Override
    public void destroy() {
        System.out.println("filter销毁中...");
        Filter.super.destroy();
    }

    private User findUser(String key) {
        if (key==null || key.isEmpty() || !key.substring(0, 7).trim().equals("Bearer")) {
            return null;
        }
        String tokenStr = key.substring(7);
        var token = userService.findToken(tokenStr);
        if (token == null || token.getExpireAt() < System.currentTimeMillis() / 1000) {
            return null;
        }
        var user = userService.findById(token.getUserId());
        if (user == null || !user.getDeleted().equals(0) || !user.getStatus().equals(0)) {
            return null;
        }
        userService.addTokenTime(token);
        return user;
    }
}
