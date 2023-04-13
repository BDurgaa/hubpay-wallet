package com.hubpay.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Log4j2
public class HubPayInterceptor implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ViolationInterceptor()).addPathPatterns("/**");
    }

    public class ViolationInterceptor implements HandlerInterceptor {
        private List<String> requestIds = new ArrayList<>();

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            log.info("inside prehandle " + requestIds.size());
            String requestId = request.getHeader("requestId");
            /*if (requestIds.contains(requestId))
                throw new IllegalArgumentException("Violation request, reason requestid already registered");*/
            if (requestId != null)
                requestIds.add(requestId);
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }
    }
}
