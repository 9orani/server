package com.example.oidc.repository;

import com.example.oidc.entity.PlayerEntity;
import com.example.oidc.entity.RoomPlayerJoinEntity;
import com.example.oidc.entity.RoomPlayerJoinEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomPlayerJoinRepository extends
    JpaRepository<RoomPlayerJoinEntity, RoomPlayerJoinEntityPK> {

}
