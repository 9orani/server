package com.example.oidc.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.oidc.entity.PlayerEntity;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PlayerRepositoryTest extends RepositoryTestHelper {

  @Test
  @DisplayName("Player 저장 성공")
  public void savePlayerSuccess() {
    // given
    String loginId = "gusah009";
    String password = "myP@ssw0rd";
    String username = "momomomo";
    String emailAddress = "gusah009@naver.com";

    // when
    PlayerEntity playerEntity = PlayerEntity.builder()
        .loginId(loginId)
        .password(password)
        .username(username)
        .emailAddress(emailAddress)
        .recentLoginTime(LocalDateTime.now())
        .registerTime(LocalDateTime.now())
        .build();
    playerEntity = playerRepository.save(playerEntity);

    // then
    assertThat(playerEntity.getLoginId()).isEqualTo(loginId);
    assertThat(playerEntity.getPassword()).isEqualTo(password);
    assertThat(playerEntity.getUsername()).isEqualTo(username);
    assertThat(playerEntity.getEmailAddress()).isEqualTo(emailAddress);
  }
}