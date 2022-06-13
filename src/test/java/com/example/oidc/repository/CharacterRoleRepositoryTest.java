package com.example.oidc.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.oidc.entity.CharacterRoleEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CharacterRoleRepositoryTest extends RepositoryTestHelper {

  @Test
  @DisplayName("Character Role 저장 성공")
  public void saveCharacterRoleSuccess() {

    // given
    String validName = "T";

    // when
    CharacterRoleEntity characterRole = CharacterRoleEntity.builder()
        .name(validName)
        .build();
    characterRole = characterRoleRepository.save(characterRole);

    // then
    assertThat(characterRole.getName()).isEqualTo(validName);
  }
}