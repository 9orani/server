package com.example.oidc.controller.auth;

import com.example.oidc.dto.player.AuthDto;
import com.example.oidc.dto.player.PlayerDto;
import com.example.oidc.dto.response.SingleResult;
import com.example.oidc.service.ResponseService;
import com.example.oidc.service.auth.SignInService;
import com.example.oidc.service.auth.SignUpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/auth/signUp")
public class SignUpController {

  private final ResponseService responseService;
  private final SignUpService signUpService;

  @PostMapping(value = "")
  public SingleResult<AuthDto> signUp(@RequestBody PlayerDto playerDto) {
    return responseService.getSuccessSingleResult(signUpService.signUp(playerDto));
  }
}
