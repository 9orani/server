package com.example.oidc.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RoomPlayerJoinEntityPK.class)
@Table(name = "room_player_join")
public class RoomPlayerJoinEntity {

  @Id
  @ManyToOne(targetEntity = PlayerEntity.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "player_id")
  private PlayerEntity playerEntity;

  @Id
  @ManyToOne(targetEntity = RoomEntity.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "room_id")
  private RoomEntity roomEntity;

  @Column(name = "join_time", nullable = false)
  private LocalDateTime joinTime;

  @Setter
  @Column(name = "recent_enter_time", nullable = false)
  private LocalDateTime recentEnterTime;

  @Column(name = "nickname", length = 100, nullable = false, unique = true)
  private String nickname;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "player_color_id")
  private CharacterColorEntity playerColorEntity;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "player_role_id")
  private CharacterRoleEntity playerRoleEntity;
}
