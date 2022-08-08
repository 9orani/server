package com.example.oidc.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.oidc.entity.PlayerEntity;
import com.example.oidc.entity.RoomEntity;
import com.example.oidc.exception.room.CustomEmptyRoomIsNotExistException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoomRepositoryTest extends RepositoryTestHelper {

  @Test
  @DisplayName("Room 저장 성공")
  public void saveRoomSuccess() {
    // given
    PlayerEntity creator = generatePlayerEntity();
    String name = "testRoomName";
    String visitCode = "AB_CD12_34";
    Long maxPlayer = 10L;

    // when

    RoomEntity roomEntity = roomRepository.findFirstByNameIsNull()
        .orElseThrow(CustomEmptyRoomIsNotExistException::new);
    roomEntity.setName(name);
    roomEntity.setVisitCode(visitCode);
    roomEntity.setCreateTime(LocalDateTime.now());
    roomEntity.setMaxPlayer(maxPlayer);
    roomEntity.setCreatorPlayerEntity(creator);
    roomEntity = roomRepository.save(roomEntity);

    // then
    assertThat(roomEntity.getName()).isEqualTo(name);
    assertThat(roomEntity.getVisitCode()).isEqualTo(visitCode);
    assertThat(roomEntity.getMaxPlayer()).isEqualTo(maxPlayer);
    assertThat(roomEntity.getCreatorPlayerEntity().getId()).isEqualTo(creator.getId());
  }
}