package com.example.oidc.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "room")
public class RoomEntity {

  @Id // pk
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", length = 100, nullable = false, unique = true)
  private String name;

  @Column(name = "visit_code", length = 10, nullable = false)
  private String visitCode;

  @CreationTimestamp
  @Column(name = "create_time", nullable = false)
  private LocalDateTime createTime;

  @Column(name = "max_player", nullable = false)
  private Long maxPlayer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "creator_player_id", nullable = false)
  private PlayerEntity creatorPlayerEntity;

  @Builder.Default
  @OneToMany(mappedBy = "roomEntity")
  List<RoomPlayerJoinEntity> joinPlayerList = new ArrayList<>();
}
