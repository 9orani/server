package com.example.oidc.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.oidc.entity.CharacterColorEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CharacterColorRepositoryTest extends RepositoryTestHelper {

  @Test
  @DisplayName("Character Color 저장 성공")
  public void saveCharacterColorSuccess() {

    // given
    String validName = "red";
    String validCode = "Test12";

    // when
    CharacterColorEntity characterColor = CharacterColorEntity.builder()
        .name(validName)
        .code(validCode)
        .build();
    characterColor = characterColorRepository.save(characterColor);

    // then
    assertThat(characterColor.getName()).isEqualTo(validName);
    assertThat(characterColor.getCode()).isEqualTo(validCode);
  }
}