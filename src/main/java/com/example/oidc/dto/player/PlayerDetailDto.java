package com.example.oidc.dto.player;

import com.example.oidc.entity.PlayerEntity;
import com.example.oidc.entity.RoomEntity;
import com.example.oidc.entity.RoomPlayerJoinEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.time.LocalDateTime;
import java.util.List;
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
public class PlayerDetailDto extends PlayerDto {

  @JsonProperty(access = Access.READ_ONLY)
  private LocalDateTime registerTime;
  @JsonProperty(access = Access.READ_ONLY)
  private LocalDateTime recentLoginTime;
  @JsonProperty(access = Access.READ_ONLY)
  List<RoomEntity> createRoomList;
  @JsonProperty(access = Access.READ_ONLY)
  List<RoomPlayerJoinEntity> joinRoomList;

  public static PlayerDetailDto toDto(PlayerEntity player) {
    return PlayerDetailDto.builder()
        .id(player.getId())
        .loginId(player.getLoginId())
        .username(player.getUsername())
        .emailAddress(player.getEmailAddress())
        .registerTime(player.getRegisterTime())
        .recentLoginTime(player.getRecentLoginTime())
        .createRoomList(player.getCreateRoomList())
        .joinRoomList(player.getJoinRoomList())
        .build();
  }
}
