package com.example.oidc.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "player")
public class PlayerEntity {

  @Id // pk
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "login_id", length = 100, nullable = false, unique = true)
  private String loginId;

  @Column(name = "password", length = 600, nullable = false)
  private String password;

  @Column(name = "username", length = 100, nullable = false, unique = true)
  private String username;

  @Column(name = "email", length = 320, nullable = false, unique = true)
  private String emailAddress;

  @CreationTimestamp
  @Column(name = "register_time", nullable = false)
  private LocalDateTime registerTime;

  @Column(name = "recent_login_time", nullable = false)
  private LocalDateTime recentLoginTime;

  @Builder.Default
  @OneToMany(mappedBy = "creatorPlayerEntity")
  List<RoomEntity> createRoomList = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "playerEntity")
  List<RoomPlayerJoinEntity> joinRoomList = new ArrayList<>();
}
