package com.example.oidc.service.auth;

import static org.assertj.core.api.Assertions.assertThat;

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
class SignUpServiceTest {

  @Autowired
  private SignUpService signUpService;

  @Value("${spring.jwt.secret}")
  private String secretKey;

  @BeforeEach
  void setUp() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  @Test
  @DisplayName("회원가입 테스트")
  void signUp() {
    // given
    String loginId = "testLoginId";
    String password = "testPassword";
    String username = "testUsername";
    String emailAddress = "testEmailAddress";

    // when
    AuthDto result = signUpService.signUp(PlayerDto.builder()
        .loginId(loginId)
        .password(password)
        .username(username)
        .emailAddress(emailAddress)
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