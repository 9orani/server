package com.example.oidc.dto.player;

import com.example.oidc.entity.PlayerEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.time.LocalDateTime;
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
public class PlayerDto {

  @JsonProperty(access = Access.WRITE_ONLY)
  protected String password;

  protected String loginId;
  protected String username;
  protected String emailAddress;

  @JsonProperty(access = Access.READ_ONLY)
  protected Long id;

  public static PlayerDto toDto(PlayerEntity player) {
    return PlayerDto.builder()
        .id(player.getId())
        .loginId(player.getLoginId())
        .username(player.getUsername())
        .emailAddress(player.getEmailAddress())
        .build();
  }

  public PlayerEntity toEntity(String hashedPassword) {
    LocalDateTime toEntityTime = LocalDateTime.now();
    return PlayerEntity.builder()
        .loginId(loginId)
        .password(hashedPassword)
        .username(username)
        .emailAddress(emailAddress)
        .registerTime(toEntityTime)
        .recentLoginTime(toEntityTime)
        .build();
  }
}
