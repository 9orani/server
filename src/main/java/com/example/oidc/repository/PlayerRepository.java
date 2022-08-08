package com.example.oidc.repository;

import com.example.oidc.entity.PlayerEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

  long VIRTUAL_MEMBER_ID = 1L;

  Optional<PlayerEntity> findByLoginId(String loginId);
}
