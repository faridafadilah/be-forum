package com.forum.server.server.security.jwt;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.forum.server.server.security.services.UserDetailslmpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${farida.app.jwtSecret}")
  private String jwtSecret;

  @Value("${farida.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  public String generateJwtToken(UserDetailslmpl userPrincipal) {
    return generateTokenFromUsername(userPrincipal.getUsername());
  }

  public String generateTokenFromUsername(String username) {
    return Jwts.builder().setSubject(username).setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  // Dapatkan username dari jwt
  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  public String usernameFromToken(String token, String signingKey) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean isJwtTokenExpired(String token) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
      return false;
    } catch (ExpiredJwtException ex) {
      return true;
    } catch (Exception e) {
      return true;
    }
  }

  public String refreshJwtToken(String oldToken) {
    String refreshedToken;
    try {
      Claims claims = getClaimsFromToken(oldToken);
      refreshedToken = Jwts.builder()
      .setClaims(claims)
      .setIssuedAt(new Date())
      .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
      .signWith(SignatureAlgorithm.HS512, jwtSecret)
      .compact();
    } catch (Exception e) {
      refreshedToken = null;
    }
    return refreshedToken;
  }

  private Claims getClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
  }

  // Memvalidasi Jwt
  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
      // kirim tanggapan HTTP ke klien jika konteks permintaan web tersedia
      RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
      if (requestAttributes instanceof ServletRequestAttributes) {
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();
        if (response != null) {
          response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
      }
      // meminta klien untuk membuat permintaan baru untuk mendapatkan token baru
      return false;
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }
}
