package com.example.oidc.repository;

import com.example.oidc.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

  Boolean existsRoomEntityByVisitCode(String visitCode);
}
