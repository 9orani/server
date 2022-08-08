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
import lombok.Setter;
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

  @Column(name = "name", length = 100, unique = true)
  @Setter
  private String name;

  @Column(name = "visit_code", length = 10, unique = true)
  @Setter
  private String visitCode;

  @CreationTimestamp
  @Column(name = "create_time")
  @Setter // TODO: 샘플 예시 끝나면 삭제 요망
  private LocalDateTime createTime;

  @Column(name = "max_player")
  @Setter
  private Long maxPlayer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "creator_player_id", nullable = false)
  @Setter
  private PlayerEntity creatorPlayerEntity;

  @Column(name = "visit_url", nullable = false, length = 500)
  private String visitUrl;

  @Column(name = "visit_port", nullable = false)
  private Integer visitPort;

  @Column(name = "current_player", nullable = false)
  private Long currentPlayer;

  @Builder.Default
  @OneToMany(mappedBy = "roomEntity")
  List<RoomPlayerJoinEntity> joinPlayerList = new ArrayList<>();

  public void decreaseCurrentPlayer() {
    this.currentPlayer--;
  }

  public void increaseCurrentPlayer() {
    this.currentPlayer++;
  }
}
