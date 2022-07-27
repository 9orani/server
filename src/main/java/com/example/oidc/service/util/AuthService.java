package com.example.oidc.service.util;

import static com.example.oidc.service.util.AuthService.JwtMessage.EMPTY;
import static com.example.oidc.service.util.AuthService.JwtMessage.EXPIRED;
import static com.example.oidc.service.util.AuthService.JwtMessage.MALFORMED;
import static com.example.oidc.service.util.AuthService.JwtMessage.UNKNOWN;
import static com.example.oidc.service.util.AuthService.JwtMessage.UNSUPPORTED;
import static com.example.oidc.service.util.AuthService.JwtMessage.VALID;
import static com.example.oidc.service.util.AuthService.JwtMessage.WRONG_FORMAT;
import static com.example.oidc.service.util.AuthService.JwtMessage.WRONG_SIGNATURE;

import com.example.oidc.dto.auth.TokenValidResponseDto;
import com.example.oidc.dto.player.PlayerDetailDto;
import com.example.oidc.entity.PlayerEntity;
import com.example.oidc.exception.player.CustomPlayerNotFoundException;
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

  private final PlayerRepository playerRepository;

  @Value("${spring.jwt.secret}")
  private String secretKey;

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public TokenValidResponseDto checkTokenValid(String token) {
    Long jwtMemberId = null;
    try {
      String tokenInfo = resolveToken(token);
      jwtMemberId = Long.valueOf(Jwts.parser()
          .setSigningKey(secretKey)
          .parseClaimsJws(tokenInfo)
          .getBody()
          .getSubject());
    } catch (MalformedJwtException e) {
      return getTokenValidResponseDto(false, MALFORMED.name(), token);
    } catch (ExpiredJwtException e) {
      return getTokenValidResponseDto(false, EXPIRED.name(), token);
    } catch (UnsupportedJwtException e) {
      return getTokenValidResponseDto(false, UNSUPPORTED.name(), token);
    } catch (SignatureException e) {
      return getTokenValidResponseDto(false, WRONG_SIGNATURE.name(), token);
    } catch (IllegalArgumentException e) {
      return getTokenValidResponseDto(false, e.getMessage(), token);
    } catch (Exception e) {
      return getTokenValidResponseDto(false, UNKNOWN.name(), token);
    }
    return getTokenValidResponseDto(true, VALID.name(), token,
        getPlayerDetailDtoById(jwtMemberId));
  }

  private PlayerDetailDto getPlayerDetailDtoById(Long memberId) {
    PlayerEntity member = playerRepository.findById(memberId)
        .orElseThrow(CustomPlayerNotFoundException::new);
    return PlayerDetailDto.toDto(member);
  }

  private String resolveToken(String token) {
    if (token == null || token.isEmpty()) {
      throw new IllegalArgumentException(EMPTY.name());
    }
    String[] parts = token.split(" ");
    System.out.println("parts = " + Arrays.toString(parts));
    String type = parts[0];
    if (parts.length != 2 || !type.equals("Bearer")) {
      throw new IllegalArgumentException(WRONG_FORMAT.name());
    }
    return parts[1];
  }

  private TokenValidResponseDto getTokenValidResponseDto(boolean isValid, String tokenMsg,
      String token, PlayerDetailDto playerDetailDto) {
    return TokenValidResponseDto.builder()
        .valid(isValid)
        .tokenMsg(tokenMsg)
        .token(token)
        .playerInfo(playerDetailDto)
        .build();
  }

  private TokenValidResponseDto getTokenValidResponseDto(boolean isValid, String tokenMsg,
      String token) {
    return getTokenValidResponseDto(isValid, tokenMsg, token, null);
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

  public enum JwtMessage {
    MALFORMED("손상된 토큰입니다."),
    EXPIRED("만료된 토큰입니다."),
    UNSUPPORTED("지원하지 않는 토큰입니다."),
    WRONG_SIGNATURE("시그니처 검증에 실패한 토큰입니다."),
    UNKNOWN("알 수 없는 이유로 유효하지 않은 토큰입니다."),
    EMPTY("토큰이 비어있습니다."),
    WRONG_FORMAT("토큰 형식이 잘못되었습니다. 'Bearer 토큰' 형식으로 전달되어야 합니다."),
    VALID("유효한 토큰입니다.");

    JwtMessage(String msg) {
    }
  }
}
