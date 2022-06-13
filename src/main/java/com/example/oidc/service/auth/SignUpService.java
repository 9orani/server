package com.example.oidc.service.auth;

import static com.example.oidc.config.security.JwtTokenProvider.tokenValidMillisecond;

import com.example.oidc.config.security.JwtTokenProvider;
import com.example.oidc.dto.player.AuthDto;
import com.example.oidc.dto.player.PlayerDto;
import com.example.oidc.entity.PlayerEntity;
import com.example.oidc.repository.PlayerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpService {

  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final PlayerRepository playerRepository;

  public AuthDto signUp(PlayerDto playerDto) {

    String hashedPassword = passwordEncoder.encode(playerDto.getPassword());
    PlayerEntity newPlayer = playerRepository.save(playerDto.toEntity(hashedPassword));

    String token = "Bearer " + jwtTokenProvider.createToken(String.valueOf(newPlayer.getId()),
        List.of("ROLE_USER"), tokenValidMillisecond);

    return AuthDto.toDto(newPlayer, token);
  }
}
