package com.example.oidc.repository;

import com.example.oidc.entity.CharacterColorEntity;
import com.example.oidc.entity.CharacterRoleEntity;
import com.example.oidc.entity.PlayerEntity;
import com.example.oidc.entity.RoomEntity;
import java.time.LocalDateTime;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class RepositoryTestHelper {

  @Autowired
  protected PlayerRepository playerRepository;

  @Autowired
  protected CharacterColorRepository characterColorRepository;

  @Autowired
  protected CharacterRoleRepository characterRoleRepository;

  @Autowired
  protected RoomRepository roomRepository;

  @Autowired
  protected RoomPlayerJoinRepository roomPlayerJoinRepository;

  protected PlayerEntity generatePlayerEntity() {
    final long epochTime = System.nanoTime();
    String loginId = "loginId_" + epochTime;
    String password = "password_" + epochTime;
    String username = "username_" + epochTime;
    String emailAddress = "email" + epochTime + "@test.com";

    PlayerEntity playerEntity = PlayerEntity.builder()
        .loginId(loginId)
        .password(password)
        .username(username)
        .emailAddress(emailAddress)
        .recentLoginTime(LocalDateTime.now())
        .registerTime(LocalDateTime.now())
        .build();
    return playerRepository.save(playerEntity);
  }

  protected RoomEntity generateRoomEntity(PlayerEntity creator) {
    final long epochTime = System.nanoTime();
    String name = "roomName_" + epochTime;
    String visitCode = "visitCode";
    Long maxPlayer = 10L;

    RoomEntity roomEntity = RoomEntity.builder()
        .name(name)
        .visitCode(visitCode)
        .createTime(LocalDateTime.now())
        .maxPlayer(maxPlayer)
        .creatorPlayerEntity(creator)
        .build();
    return roomRepository.save(roomEntity);
  }

  protected CharacterColorEntity generateCharacterColorEntity(String validName, String validCode) {

    CharacterColorEntity characterColor = CharacterColorEntity.builder()
        .name(validName)
        .code(validCode)
        .build();
    return characterColorRepository.save(characterColor);
  }

  protected CharacterRoleEntity generateCharacterRoleEntity(String validName) {

    CharacterRoleEntity characterRole = CharacterRoleEntity.builder()
        .name(validName)
        .build();
    return characterRoleRepository.save(characterRole);
  }
}
