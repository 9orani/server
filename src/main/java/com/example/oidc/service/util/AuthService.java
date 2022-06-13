package com.example.oidc.service.util;

import com.example.oidc.config.security.JwtTokenProvider;
import com.example.oidc.entity.PlayerEntity;
import com.example.oidc.repository.PlayerRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final PlayerRepository playerRepository;

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
