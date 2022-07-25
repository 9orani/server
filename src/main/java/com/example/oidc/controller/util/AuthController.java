package com.example.oidc.controller.util;

import com.example.oidc.dto.auth.TokenValidRequestDto;
import com.example.oidc.dto.auth.TokenValidResponseDto;
import com.example.oidc.dto.response.ListResult;
import com.example.oidc.dto.response.SingleResult;
import com.example.oidc.service.ResponseService;
import com.example.oidc.service.util.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/jwt")
public class AuthController {

  private final AuthService authService;
  private final ResponseService responseService;

  @PostMapping("/check")
  public SingleResult<TokenValidResponseDto> checkJwtValid(
      @RequestBody TokenValidRequestDto token) {
    return responseService.getSuccessSingleResult(authService.checkTokenValid(token.getToken()));
  }

  @GetMapping("/roles")
  public ListResult<String> getRoles() {
    return responseService.getSuccessListResult(authService.getRolesByJWT());
  }
}
