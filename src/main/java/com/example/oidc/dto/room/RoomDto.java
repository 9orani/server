package com.example.oidc.dto.room;

import com.example.oidc.entity.RoomEntity;
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
public class RoomDto {

  protected Long id;
  protected String name;
  protected String visitCode;
  protected Long maxPlayer;
  protected Long currentPlayer;

  public static RoomDto toDto(RoomEntity room) {
    return RoomDto.builder()
        .id(room.getId())
        .name(room.getName())
        .visitCode(room.getVisitCode())
        .maxPlayer(room.getMaxPlayer())
        .currentPlayer(room.getCurrentPlayer())
        .build();
  }
}
