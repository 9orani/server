package com.example.oidc.controller.util;

import static com.example.oidc.service.util.AuthService.JwtMessage.EMPTY;
import static com.example.oidc.service.util.AuthService.JwtMessage.EXPIRED;
import static com.example.oidc.service.util.AuthService.JwtMessage.MALFORMED;
import static com.example.oidc.service.util.AuthService.JwtMessage.VALID;
import static com.example.oidc.service.util.AuthService.JwtMessage.WRONG_FORMAT;
import static com.example.oidc.service.util.AuthService.JwtMessage.WRONG_SIGNATURE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.oidc.dto.auth.TokenValidRequestDto;
import com.example.oidc.entity.PlayerEntity;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

//@Transactional
class AuthControllerTest extends AuthControllerTestSetup {

  private final Long userPk = 1L;
  private final List<String> roles = new ArrayList<>(List.of("ROLE_USER", "ROLE_TEST"));

  @Test
  @DisplayName("JWT 유효성 검사 성공")
  public void checkJwtSuccess() throws Exception {
    PlayerEntity playerEntity = generatePlayerEntity();
    long validTime = 1000L * 60 * 60;
    String validToken = jwtTokenProvider.
        createToken(String.valueOf(playerEntity.getId()), roles, validTime);
    TokenValidRequestDto content = TokenValidRequestDto.builder().token(validToken).build();

    String docSuccess = "성공: true +\nDB 상에 보낸 JWT에 해당하는 Player가 없으면 실패: false";
    String docCode = "성공할 경우 0\nDB 상에 보낸 JWT에 해당하는 Player가 없으면 실패: -1000";
    String docMsg = "";
    mockMvc.perform(post("/v1/jwt/check")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(asJsonString(content)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.code").value(0))
        .andExpect(jsonPath("$.data.valid").value(true))
        .andExpect(jsonPath("$.data.tokenMsg").value(VALID.name()))
        .andExpect(jsonPath("$.data.token").value(validToken))
//        .andExpect(jsonPath("$.data.playerInfo").value(validToken))
        .andDo(document("check-jwt",
            requestFields(
                fieldWithPath("token").description("유저 토큰 ('Bearer'를 붙여야 합니다)")
            ),
            responseFields(
                generateTokenValidResponseFields(ResponseType.SINGLE, docSuccess, docCode, docMsg)
            )
        ));
  }

  @Test
  @DisplayName("JWT 유효성 검사 실패 - 토큰 없음")
  public void checkJwtFailedByNoToken() throws Exception {
    TokenValidRequestDto content = TokenValidRequestDto.builder().token("").build();

    mockMvc.perform(post("/v1/jwt/check")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(asJsonString(content)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.code").value(0))
        .andExpect(jsonPath("$.data.valid").value(false))
        .andExpect(jsonPath("$.data.tokenMsg").value(EMPTY.name()))
        .andExpect(jsonPath("$.data.token").value(""));
  }

  @Test
  @DisplayName("JWT 유효성 검사 실패 - 형식이 잘못된 토큰")
  public void checkJwtFailedByInvalidTypeToken() throws Exception {
    long validTime = 1000L * 60 * 60;
    String validToken = jwtTokenProvider
        .createToken(String.valueOf(userPk), roles, validTime);

    String invalidToken = validToken.replace("Bearer", "OIDC");
    TokenValidRequestDto content = TokenValidRequestDto.builder().token(invalidToken).build();

    mockMvc.perform(post("/v1/jwt/check")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(asJsonString(content)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.code").value(0))
        .andExpect(jsonPath("$.data.valid").value(false))
        .andExpect(jsonPath("$.data.tokenMsg").value(WRONG_FORMAT.name()))
        .andExpect(jsonPath("$.data.token").value(invalidToken));
  }

  @Test
  @DisplayName("JWT 유효성 검사 실패 - 손상된 토큰")
  public void checkJwtFailedByInvalidToken() throws Exception {
    long validTime = 1000L * 60 * 60;
    String validToken = jwtTokenProvider
        .createToken(String.valueOf(userPk), roles, validTime);

    String invalidToken = validToken.replace(".", "");
    TokenValidRequestDto content = TokenValidRequestDto.builder().token(invalidToken).build();

    mockMvc.perform(post("/v1/jwt/check")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(asJsonString(content)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.code").value(0))
        .andExpect(jsonPath("$.data.valid").value(false))
        .andExpect(jsonPath("$.data.tokenMsg").value(MALFORMED.name()))
        .andExpect(jsonPath("$.data.token").value(invalidToken));
  }

  @Test
  @DisplayName("JWT 유효성 검사 실패 - 잘못된 시그니처 토큰")
  public void checkJwtFailedByInvalidSignatureToken() throws Exception {
    long validTime = 1000L * 60 * 60;
    String validToken = jwtTokenProvider
        .createToken(String.valueOf(userPk), roles, validTime);

    String invalidToken = validToken.substring(0, validToken.length() - 3);
    TokenValidRequestDto content = TokenValidRequestDto.builder().token(invalidToken).build();

    mockMvc.perform(post("/v1/jwt/check")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(asJsonString(content)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.code").value(0))
        .andExpect(jsonPath("$.data.valid").value(false))
        .andExpect(jsonPath("$.data.tokenMsg").value(WRONG_SIGNATURE.name()))
        .andExpect(jsonPath("$.data.token").value(invalidToken));
  }

  @Test
  @DisplayName("JWT 유효성 검사 실패 - 만료된 토큰")
  public void checkJwtFailedByExpiredToken() throws Exception {
    long invalidTime = 0L;
    String invalidToken = jwtTokenProvider
        .createToken(String.valueOf(userPk), roles, invalidTime);
    TokenValidRequestDto content = TokenValidRequestDto.builder().token(invalidToken).build();

    mockMvc.perform(post("/v1/jwt/check")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(asJsonString(content)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.code").value(0))
        .andExpect(jsonPath("$.data.valid").value(false))
        .andExpect(jsonPath("$.data.tokenMsg").value(EXPIRED.name()))
        .andExpect(jsonPath("$.data.token").value(invalidToken));
  }

  @Test
  @DisplayName("JWT로 권한 가져오기 성공")
  public void getRolesSuccess() throws Exception {
    long validTime = 1000L * 60 * 60;
    String validToken =
        jwtTokenProvider.createToken(String.valueOf(userPk), roles, validTime);

    String docSuccess = "성공: true +\n실패: false";
    String docCode =
        "잘못된 토큰을 header에 기입한 경우: " + exceptionAdvice.getMessage("entryPointException.code") + "\n";
    String docMsg = "잘못된 토큰을 입력하면 /exception/entrypoint로 redirect 후 401 UnAuthorization을 return합니다.";
    mockMvc.perform(get("/v1/jwt/roles")
            .header("Authorization", validToken))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.code").value(0))
        .andExpect(jsonPath("$.list[0]").value("ROLE_USER"))
        .andExpect(jsonPath("$.list[1]").value("ROLE_TEST"))
        .andDo(document("get-roles",
            responseFields(
                generateGetRolesResponseFields(ResponseType.LIST, docSuccess, docCode, docMsg)
            )
        ));
  }

  @Test
  @DisplayName("JWT로 권한 가져오기 실패 - 토큰 없음")
  public void getRolesFailedByNoToken() throws Exception {
    mockMvc.perform(get("/v1/jwt/roles"))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/exception/entrypoint"));
  }

  @Test
  @DisplayName("JWT로 권한 가져오기 실패 - 잘못된 토큰")
  public void getRolesFailedByInvalidToken() throws Exception {
    long validTime = 1000L * 60 * 60;
    String validToken =
        jwtTokenProvider.createToken(String.valueOf(userPk), roles, validTime);
    String invalidToken1 = validToken.replace("Bearer", "OIDC");
    String invalidToken2 = validToken.substring(0, validToken.length() - 3);

    mockMvc.perform(get("/v1/jwt/roles")
            .header("Authorization", invalidToken1))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/exception/entrypoint"));

    mockMvc.perform(get("/v1/jwt/roles")
            .header("Authorization", invalidToken2))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/exception/entrypoint"));
  }

  @Test
  @DisplayName("JWT로 권한 가져오기 실패 - 만료된 토큰")
  public void getRolesFailedByExpiredToken() throws Exception {
    long invalidTime = 0L;
    String invalidToken =
        jwtTokenProvider.createToken(String.valueOf(userPk), roles, invalidTime);

    mockMvc.perform(get("/v1/jwt/roles")
            .header("Authorization", invalidToken))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/exception/entrypoint"));
  }

  @Test
  @DisplayName("unAuthorization redirect 성공")
  public void unAuthorization() throws Exception {
    mockMvc.perform(get("/exception/entrypoint"))
        .andDo(print())
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(
            jsonPath("$.code").value(exceptionAdvice.getMessage("entryPointException.code")))
        .andExpect(
            jsonPath("$.msg").value(exceptionAdvice.getMessage("entryPointException.msg")));
  }
}