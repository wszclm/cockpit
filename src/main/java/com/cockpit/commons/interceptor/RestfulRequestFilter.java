package com.cockpit.commons.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "RestfulRequestFilter",urlPatterns = "/*")
@Component
public class RestfulRequestFilter implements Filter{


    private static Logger logger = LoggerFactory.getLogger(RestfulRequestFilter.class);


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("init filter start...");
    }


    // 跨域请求，添加header 信息
    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String curOrigin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", curOrigin == null ? "true" : curOrigin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader("P3P","CP=CAO PSA OUR");
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(200);
            return;
        }

        String url = request.getRequestURL().toString();
        String urlParams = request.getQueryString();
        String contnetType = request.getContentType();

        logger.debug("url:" + url);
        chain.doFilter(req, res);
        return;
    }

    @Override
    public void destroy() {

    }
}
