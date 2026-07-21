package com.infoworks.lab.webapp.config.tenancy;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TenantFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request
            , HttpServletResponse response
            , FilterChain chain) throws ServletException, IOException {
        //Read tenant-id from http-headers:
        String tenant = request.getHeader("X-Tenant");
        if (tenant != null) {
            //Validate the tenant against a known list or mapping first:
            //TODO:
        }
        TenantContext.setTenant(tenant);
        //Passing request to filter-chain:
        try {
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
