package com.example.oidc.service.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.oidc.dto.player.AuthDto;
import com.example.oidc.dto.player.PlayerDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SignInServiceTest {

  @Value("${spring.jwt.secret}")
  private String secretKey;

  @Autowired
  private SignInService signInService;

  @Autowired
  private SignUpService signUpService;

  private final String loginId = "testLoginId";
  private final String password = "testPassword";
  private final String username = "testUsername";
  private final String emailAddress = "testEmailAddress";

  @BeforeEach
  void setup() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());

    signUpService.signUp(PlayerDto.builder()
        .loginId(loginId)
        .password(password)
        .username(username)
        .emailAddress(emailAddress)
        .build());
  }

  @Test
  @DisplayName("로그인 테스트 - 성공")
  void signInSuccess() {

    // when
    AuthDto result = signInService.signIn(PlayerDto.builder()
        .loginId(loginId)
        .password(password)
        .build());

    // then
    assertThat(result.getLoginId()).isEqualTo(loginId);
    assertThat(result.getUsername()).isEqualTo(username);
    assertThat(result.getEmailAddress()).isEqualTo(emailAddress);
    assertThat(Long.valueOf(getClaim(result.getToken().split(" ")[1]).getSubject()))
        .isEqualTo(result.getId());
  }

  private Claims getClaim(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
  }
}