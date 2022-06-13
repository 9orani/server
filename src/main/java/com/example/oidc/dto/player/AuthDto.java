package com.example.oidc.dto.player;

import com.example.oidc.entity.PlayerEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(Include.NON_NULL)
public class AuthDto extends PlayerDto {

  private String token;

  public static AuthDto toDto(PlayerEntity player, String token) {
    return AuthDto.builder()
        .id(player.getId())
        .loginId(player.getLoginId())
        .username(player.getUsername())
        .emailAddress(player.getEmailAddress())
        .token(token)
        .build();
  }
}
