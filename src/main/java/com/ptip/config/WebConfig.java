package com.ptip.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("forward:/index.html");
//    }

    @Bean
    public RedirectFilter redirectFilter() {
        return new RedirectFilter();
    }

    // HTTP -> HTTPS 리디렉션을 위한 필터
    public static class RedirectFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response,
                             FilterChain chain) throws java.io.IOException, ServletException {
            if (request instanceof HttpServletRequest) {
                HttpServletRequest req = (HttpServletRequest) request;
                HttpServletResponse res = (HttpServletResponse) response;
                if ("http".equalsIgnoreCase(req.getScheme())) {
                    String newUrl = "https://" + req.getServerName() + req.getRequestURI();
                    res.sendRedirect(newUrl);
                    return;
                }
            }
            chain.doFilter(request, response);
        }

        @Override
        public void destroy() {
        }
    }
}