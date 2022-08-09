package com.example.oidc.repository;

import com.example.oidc.entity.RoomEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

  Boolean existsRoomEntityByVisitCode(String visitCode);

  Optional<RoomEntity> findFirstByNameIsNull();

  Page<RoomEntity> findAllByNameIsNotNull(Pageable pageable);

  Optional<RoomEntity> findByVisitPort(Integer visitPort);

  Optional<RoomEntity> findByVisitCode(String visitCode);
}
