package com.example.oidc.dto.auth;

import com.example.oidc.dto.player.PlayerDetailDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TokenValidResponseDto {

  private boolean valid;
  private String tokenMsg;
  private String token;
  private PlayerDetailDto playerInfo;
}
