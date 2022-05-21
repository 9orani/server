package com.example.oidc.service.util;

import com.example.oidc.config.security.JwtTokenProvider;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final JwtTokenProvider jwtTokenProvider;

  public List<String> getRolesByJWT() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication.getAuthorities().stream().map(String::valueOf)
        .collect(Collectors.toList());
  }

}
