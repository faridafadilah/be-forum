package com.forum.server.server.interceptor;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import com.forum.server.server.models.User;
import com.forum.server.server.payload.response.ListUserResponse;
import com.forum.server.server.repository.UserRepository;
import com.forum.server.server.security.jwt.JwtUtils;


@Component
public class SuperAdminInterceptor implements HandlerInterceptor {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtUtils jwtUtils = new JwtUtils();
  

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String signKey = "riDaSecretKey";
    String url = request.getRequestURI();
    System.out.print(url);

    String headerAuth = request.getHeader("Authorization");
    if (headerAuth == null || !headerAuth.contains("Bearer ")) {
      System.out.println("UNAUTORIZATION");
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    String token = headerAuth.substring(7);
    System.out.print("TOkEN"+token);
    
    String username = jwtUtils.usernameFromToken(token, signKey);
    System.out.println("username: "+ username);

    Optional<User> userOptional = userRepository.findByUsername(username);
    if (!userOptional.isPresent()) {
      System.out.println("UNAUTORIZATION");
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    User user = userOptional.get();
    if(user.getRoles().isEmpty()) {
      System.out.println("UNAUTORIZATION");
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    List<ListUserResponse> responses = userRepository.findByUserAndRoleSuper(username);
    if(responses.isEmpty()) {
      System.out.println("UNAUTHORIZATION");
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    return HandlerInterceptor.super.preHandle(request, response, handler);
  }
}
