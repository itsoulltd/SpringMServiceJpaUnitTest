package com.infoworks.lab.webapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RouteExecutionLogger extends GenericFilterBean {

    private static final Logger LOG = LoggerFactory.getLogger(RouteExecutionLogger.class);

    @Override
    public void doFilter(ServletRequest servletRequest
            , ServletResponse servletResponse
            , FilterChain filterChain) throws IOException, ServletException {
        //Logging time between request and response:
        long startTime = System.currentTimeMillis();
        // Continue with the next filter or controller
        filterChain.doFilter(servletRequest, servletResponse);
        //Now log time elapsed:
        long endTime = System.currentTimeMillis();
        long requestTime = endTime - startTime;
        //Get the info from request & response:
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        LOG.info("{}:{} took {}ms and returned {}"
                , req.getMethod(), req.getRequestURI(), requestTime, res.getStatus());
    }
}
