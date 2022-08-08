package com.example.oidc.dto.room;

import com.example.oidc.dto.player.PlayerDto;
import com.example.oidc.entity.RoomEntity;
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
public class RoomDetailDto extends RoomDto {

  @JsonProperty(access = Access.READ_ONLY)
  protected String visitUrl;
  @JsonProperty(access = Access.READ_ONLY)
  protected Integer visitPort;
  @JsonProperty(access = Access.READ_ONLY)
  protected LocalDateTime createTime;
  @JsonProperty(access = Access.READ_ONLY)
  protected PlayerDto creatorPlayer;
  @JsonProperty(access = Access.READ_ONLY)
  protected List<PlayerDto> joinPlayerList;

  public static RoomDetailDto toDto(RoomEntity room) {
    return RoomDetailDto.builder()
        .id(room.getId())
        .name(room.getName())
        .visitCode(room.getVisitCode())
        .createTime(room.getCreateTime())
        .maxPlayer(room.getMaxPlayer())
        .currentPlayer(room.getCurrentPlayer())
        .visitUrl(room.getVisitUrl())
        .visitPort(room.getVisitPort())
        .creatorPlayer(PlayerDto.toDto(room.getCreatorPlayerEntity()))
        .joinPlayerList(room.getJoinPlayerList().stream().map(
            roomPlayerJoinInfo -> PlayerDto.toDto(roomPlayerJoinInfo.getPlayerEntity())).toList())
        .build();
  }
}
