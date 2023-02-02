package com.forum.server.server.security.services;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forum.server.server.exception.TokenRefreshException;
import com.forum.server.server.models.Logout;
import com.forum.server.server.models.RefreshToken;
import com.forum.server.server.repository.LogoutRepository;
import com.forum.server.server.repository.RefreshTokenRepository;
import com.forum.server.server.repository.UserRepository;

@Service
public class RefreshTokenService {
  @Value("${farida.app.jwtRefreshExpirationMs}")
  private Long refreshTokenDurationMs;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private LogoutRepository logoutRepository;

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  } 

  public RefreshToken editRefreshToken(String token, String requestRefreshToken) {
    Optional<RefreshToken> cariToken = findByToken(requestRefreshToken);
    RefreshToken refreshToken = cariToken.get();
    refreshToken.setStatus(true);
    refreshTokenRepository.save(refreshToken);
    return refreshToken;
  }

  public RefreshToken createRefreshToken(Long userId, String token) {
    RefreshToken refreshToken = new RefreshToken();

    refreshToken.setUser(userRepository.findById(userId).get());
    refreshToken.setCreated_at(new Date());
    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken.setTokenJwt(token);


    refreshToken = refreshTokenRepository.save(refreshToken);
    return refreshToken;
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
    }

    return token;
  }

  public Logout createUserLogout(String username, Long userId) {
    Logout logout = new Logout();
    // String token = refreshTokenRepository.findTokenByUserId(userId);
    Optional<RefreshToken> list = refreshTokenRepository.findByUserId(userId);
    RefreshToken refreshtoken = list.get();
    
    logout.setUsername(username);
    logout.setToken(refreshtoken.getTokenJwt());
    logout.setCreated_at(refreshtoken.getCreated_at());
    logout.set_logged_out(true);
    logout.setExpired_at(refreshtoken.getExpiryDate());
    logout = logoutRepository.save(logout);
    return logout;
  }

  @Transactional
  public int deleteByUserId(Long userId) {
    return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
  }
}

