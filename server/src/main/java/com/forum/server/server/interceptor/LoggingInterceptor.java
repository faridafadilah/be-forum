package com.forum.server.server.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.forum.server.server.models.Logs;
import com.forum.server.server.repository.LogRepository;
import java.util.Date;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
  
  @Autowired
  private LogRepository logrepo;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String method = request.getMethod();
    String endpoint = request.getRequestURI();
    String logSource = handler.toString();
    String logMessage = String.format("%s %s", method, endpoint);
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    Date createdAt = new Date();

    request.setAttribute("startTime", System.currentTimeMillis());
    request.setAttribute("method", method);
    request.setAttribute("endpoint", endpoint);
    request.setAttribute("username", username);
    request.setAttribute("createdAt", createdAt);
    request.setAttribute("logSource", logSource);
    request.setAttribute("logMessage", logMessage);

    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    long startTime = (long) request.getAttribute("startTime");
    long endTime = System.currentTimeMillis();
    long executeTime = endTime - startTime;

    String method = (String) request.getAttribute("method");
    String endpoint = (String) request.getAttribute("endpoint");
    Integer status = response.getStatus();
    String username = (String) request.getAttribute("username");
    Date createdAt = (Date) request.getAttribute("createdAt");
    String logSource = (String) request.getAttribute("logSource");
    String logMessage = (String) request.getAttribute("logMessage");

    Logs log = new Logs();
    log.setMethod(method);
    log.setEndpoint(endpoint);
    log.setStatus(status);
    log.setUsername(username);
    log.setCreatedAt(createdAt);
    log.setLogMessage(logMessage);
    log.setLogSource(logSource);

    logrepo.save(log);
  }
}
