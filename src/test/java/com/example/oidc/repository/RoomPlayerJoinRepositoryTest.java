package com.example.oidc.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.oidc.entity.CharacterColorEntity;
import com.example.oidc.entity.CharacterRoleEntity;
import com.example.oidc.entity.PlayerEntity;
import com.example.oidc.entity.RoomEntity;
import com.example.oidc.entity.RoomPlayerJoinEntity;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RoomPlayerJoinRepositoryTest extends RepositoryTestHelper {

  @Autowired
  EntityManager em;

  @Test
  @DisplayName("RoomPlayerJoin Player Join 저장 성공")
  public void saveRoomPlayerJoinPlayerJoinSuccess() {
    // given
    CharacterColorEntity characterColor = generateCharacterColorEntity("red", "r");
    CharacterRoleEntity characterRole = generateCharacterRoleEntity("T");
    PlayerEntity creator = generatePlayerEntity();
    PlayerEntity member1 = generatePlayerEntity();
    PlayerEntity member2 = generatePlayerEntity();
    RoomEntity room = generateRoomEntity(creator);
    String creatorNickname = "creatorNickname";
    String nickname1 = "nickname1";
    String nickname2 = "nickname2";

    // when
    RoomPlayerJoinEntity roomPlayerJoin1 = RoomPlayerJoinEntity.builder()
        .playerEntity(creator)
        .roomEntity(room)
        .joinTime(LocalDateTime.now())
        .recentEnterTime(LocalDateTime.now())
        .nickname(creatorNickname)
        .playerColorEntity(characterColor)
        .playerRoleEntity(characterRole)
        .build();
    roomPlayerJoin1 = roomPlayerJoinRepository.save(roomPlayerJoin1);
    creator.getCreateRoomList().add(room);
    creator.getJoinRoomList().add(roomPlayerJoin1);
    room.getJoinPlayerList().add(roomPlayerJoin1);

    RoomPlayerJoinEntity roomPlayerJoin2 = RoomPlayerJoinEntity.builder()
        .playerEntity(member1)
        .roomEntity(room)
        .joinTime(LocalDateTime.now())
        .recentEnterTime(LocalDateTime.now())
        .nickname(nickname1)
        .playerColorEntity(characterColor)
        .playerRoleEntity(characterRole)
        .build();
    roomPlayerJoin2 = roomPlayerJoinRepository.save(roomPlayerJoin2);
    member1.getJoinRoomList().add(roomPlayerJoin1);
    room.getJoinPlayerList().add(roomPlayerJoin1);

    RoomPlayerJoinEntity roomPlayerJoin3 = RoomPlayerJoinEntity.builder()
        .playerEntity(member2)
        .roomEntity(room)
        .joinTime(LocalDateTime.now())
        .recentEnterTime(LocalDateTime.now())
        .nickname(nickname2)
        .playerColorEntity(characterColor)
        .playerRoleEntity(characterRole)
        .build();
    roomPlayerJoin3 = roomPlayerJoinRepository.save(roomPlayerJoin3);
    member2.getJoinRoomList().add(roomPlayerJoin1);
    room.getJoinPlayerList().add(roomPlayerJoin1);

    em.flush();

    // then
    assertThat(roomPlayerJoin1.getNickname()).isEqualTo(creatorNickname);
    assertThat(roomPlayerJoin1.getPlayerEntity().getId()).isEqualTo(creator.getId());
    assertThat(roomPlayerJoin1.getPlayerColorEntity().getId()).isEqualTo(characterColor.getId());
    assertThat(roomPlayerJoin1.getPlayerRoleEntity().getId()).isEqualTo(characterRole.getId());

    assertThat(roomPlayerJoin2.getNickname()).isEqualTo(nickname1);
    assertThat(roomPlayerJoin2.getPlayerEntity().getId()).isEqualTo(member1.getId());
    assertThat(roomPlayerJoin2.getPlayerColorEntity().getId()).isEqualTo(characterColor.getId());
    assertThat(roomPlayerJoin2.getPlayerRoleEntity().getId()).isEqualTo(characterRole.getId());

    assertThat(roomPlayerJoin3.getNickname()).isEqualTo(nickname2);
    assertThat(roomPlayerJoin3.getPlayerEntity().getId()).isEqualTo(member2.getId());
    assertThat(roomPlayerJoin3.getPlayerColorEntity().getId()).isEqualTo(characterColor.getId());
    assertThat(roomPlayerJoin3.getPlayerRoleEntity().getId()).isEqualTo(characterRole.getId());

    assertThat(creator.getJoinRoomList().size()).isEqualTo(1);
    assertThat(creator.getJoinRoomList().get(0).getRoomEntity().getId()).isEqualTo(room.getId());
    assertThat(member1.getJoinRoomList().size()).isEqualTo(1);
    assertThat(member1.getJoinRoomList().get(0).getRoomEntity().getId()).isEqualTo(room.getId());
    assertThat(member2.getJoinRoomList().size()).isEqualTo(1);
    assertThat(member2.getJoinRoomList().get(0).getRoomEntity().getId()).isEqualTo(room.getId());

    assertThat(room.getJoinPlayerList().size()).isEqualTo(3);
  }
}