package com.forum.server.server.interceptor;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import com.forum.server.server.models.Logout;
import com.forum.server.server.repository.LogoutRepository;

@Component
public class LogoutInterceptor implements HandlerInterceptor {
  @Autowired
  LogoutRepository logoutRepository;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String url = request.getRequestURI();
    System.out.println(url);

    String headerAuth = request.getHeader("Authorization");
    if(headerAuth == null || !headerAuth.contains("Bearer ")) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    String token = headerAuth.substring(7);

    Optional<Logout> logout = logoutRepository.findStatusByToken(token);
    if(logout.isPresent()) {
      Logout list = logout.get();
      if(list.is_logged_out() == true) {
        throw (new Exception("Token telah digunakan!"));
      }
    }
    
    return HandlerInterceptor.super.preHandle(request, response, handler);
  }
}

