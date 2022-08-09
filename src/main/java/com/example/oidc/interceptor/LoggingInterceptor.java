package com.example.oidc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) {
    log.info("req url : {}", req.getRequestURI());
    log.info("client Address : {}", req.getRemoteAddr());
    log.info("client port : {}", req.getRemotePort());
    log.info("client req host : {}", req.getRemoteHost());
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object handler,
      ModelAndView modelAndView) {
    log.info("response status: {}", resp.getStatus());
  }
}