package com.example.oidc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "character_color")
public class CharacterColorEntity {

  @Id // pk
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", length = 20, nullable = false, unique = true)
  private String name;

  @Column(name = "code", length = 6, nullable = false, unique = true)
  private String code;
}
