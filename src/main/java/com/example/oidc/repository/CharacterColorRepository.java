package com.example.oidc.repository;

import com.example.oidc.entity.CharacterColorEntity;
import com.example.oidc.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterColorRepository extends JpaRepository<CharacterColorEntity, Long> {

}
