package com.example.oidc.service.util;

import com.example.oidc.config.security.JwtTokenProvider;
import com.example.oidc.dto.auth.TokenValidResponseDto;
import com.example.oidc.entity.PlayerEntity;
import com.example.oidc.repository.PlayerRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final JwtTokenProvider jwtTokenProvider;
  private final PlayerRepository playerRepository;

  @Value("${spring.jwt.secret}")
  private String secretKey;

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public TokenValidResponseDto checkTokenValid(String token) {
    System.out.println("secretKey = " + secretKey);
    try {
      String tokenInfo = resolveToken(token);
      Jwts.parser().setSigningKey(secretKey).parseClaimsJws(tokenInfo);
    } catch (MalformedJwtException e) {
      return getTokenValidResponseDto(false, "손상된 토큰입니다.", token);
    } catch (ExpiredJwtException e) {
      return getTokenValidResponseDto(false, "만료된 토큰입니다.", token);
    } catch (UnsupportedJwtException e) {
      return getTokenValidResponseDto(false, "지원하지 않는 토큰입니다.", token);
    } catch (SignatureException e) {
      return getTokenValidResponseDto(false, "시그니처 검증에 실패한 토큰입니다.", token);
    } catch (IllegalArgumentException e) {
      return getTokenValidResponseDto(false, e.getMessage(), token);
    } catch (Exception e) {
      return getTokenValidResponseDto(false, "알 수 없는 이유로 유효하지 않은 토큰입니다.", token);
    }
    return getTokenValidResponseDto(true, "유효한 토큰입니다.", token);
  }

  private String resolveToken(String token) {
    if (token == null || token.isEmpty()) {
      throw new IllegalArgumentException("토큰이 비어있습니다.");
    }
    String[] parts = token.split(" ");
    System.out.println("parts = " + Arrays.toString(parts));
    String type = parts[0];
    if (parts.length != 2 || !type.equals("Bearer")) {
      throw new IllegalArgumentException("토큰 형식이 잘못되었습니다. 'Bearer 토큰' 형식으로 전달되어야 합니다.");
    }
    return parts[1];
  }

  private TokenValidResponseDto getTokenValidResponseDto(boolean isValid, String tokenMsg,
      String token) {
    return TokenValidResponseDto.builder()
        .valid(isValid)
        .tokenMsg(tokenMsg)
        .token(token).build();
  }

  public List<String> getRolesByJWT() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication.getAuthorities().stream().map(String::valueOf)
        .collect(Collectors.toList());
  }

  public Long getPlayerIdByJWT() {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    long playerId;
    try {
      playerId = Long.parseLong(authentication.getName());
    } catch (Exception e) {
      throw new AccessDeniedException("");
    }
    return playerId;
  }

  public PlayerEntity getPlayerEntityWithJWT() {
    Long memberId = getPlayerIdByJWT();
    Optional<PlayerEntity> member = playerRepository.findById(memberId);
    if (member.isEmpty()) {
      throw new AccessDeniedException("");
    }
    return member.get();
  }
}
