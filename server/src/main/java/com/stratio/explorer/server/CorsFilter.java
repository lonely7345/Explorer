package com.stratio.explorer.server;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cors filter
 *
 */
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String sourceHost = request.getServerName();
        String currentHost = java.net.InetAddress.getLocalHost().getHostName();
        String origin = "";
        if (currentHost.equals(sourceHost) || "localhost".equals(sourceHost)) {
            origin = ((HttpServletRequest) request).getHeader("Origin");
        }

        if (((HttpServletRequest) request).getMethod().equals("OPTIONS")) {
            HttpServletResponse resp = ((HttpServletResponse) response);
            addCorsHeaders(resp, origin);
            return;
        }

        if (response instanceof HttpServletResponse) {
            HttpServletResponse alteredResponse = ((HttpServletResponse) response);
            addCorsHeaders(alteredResponse, origin);
        }
        filterChain.doFilter(request, response);
    }

    private void addCorsHeaders(HttpServletResponse response, String origin) {
        response.addHeader("Access-Control-Allow-Origin", origin + ", *");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Headers", "authorization,Content-Type");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, HEAD, DELETE");
        DateFormat fullDateFormatEN =
                DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, new Locale("EN", "en"));
        response.addHeader("Date", fullDateFormatEN.format(new Date()));
    }

    @Override
    public void destroy() {}

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}
}