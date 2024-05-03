package com.biblioteca.infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Request Tracker field class used to capture requests and generate logs related to them
 *
 * @author Renato Virto (renatovirtomoreira@outlook.com)
 * @since 1.0.0
 */
@Component
public class RequestTrackerFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestTrackerFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        final HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        Long start = System.currentTimeMillis();
        filterChain.doFilter(servletRequest, servletResponse);
        logger.info(this.generateLogLine(httpRequest, httpResponse, start), this.getClass());
    }

    private String generateLogLine(HttpServletRequest httpRequest, HttpServletResponse clientHttpResponse, Long start) {
        String remoteHost = httpRequest.getRemoteAddr();
        String uri = httpRequest.getRequestURI();
        String httpMethod = httpRequest.getMethod();
        int httpStatus = clientHttpResponse.getStatus();
        long elapsedTime = System.currentTimeMillis() - start;

        return String.format("httpMethod=%s remoteHost=%s uri=%s httpStatus=%d elapsedTime=%d",
                httpMethod, remoteHost, uri, httpStatus, elapsedTime);
    }
}
