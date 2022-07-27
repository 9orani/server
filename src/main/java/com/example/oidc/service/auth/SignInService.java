package com.example.oidc.service.auth;

import static com.example.oidc.config.security.JwtTokenProvider.tokenValidMillisecond;

import com.example.oidc.config.security.JwtTokenProvider;
import com.example.oidc.dto.player.AuthDto;
import com.example.oidc.dto.player.PlayerDto;
import com.example.oidc.entity.PlayerEntity;
import com.example.oidc.exception.player.CustomPlayerNotFoundException;
import com.example.oidc.exception.sign.CustomLoginIdSigninFailedException;
import com.example.oidc.repository.PlayerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInService {

  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final PlayerRepository playerRepository;

  public AuthDto signIn(PlayerDto playerDto) {
    PlayerEntity signInPlayer = playerRepository.findByLoginId(playerDto.getLoginId())
        .orElseThrow(CustomPlayerNotFoundException::new);

    if (!passwordEncoder.matches(playerDto.getPassword(), signInPlayer.getPassword())) {
      throw new CustomLoginIdSigninFailedException();
    }

    String token = jwtTokenProvider.createToken(String.valueOf(signInPlayer.getId()),
        List.of("ROLE_USER"), tokenValidMillisecond);

    return AuthDto.toDto(signInPlayer, token);
  }
}
