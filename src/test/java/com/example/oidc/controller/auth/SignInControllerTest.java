package com.example.oidc.controller.auth;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.oidc.controller.ApiControllerTestHelper;
import com.example.oidc.dto.player.PlayerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class SignInControllerTest extends ApiControllerTestHelper {

  private final String loginId = "testLoginId";
  private final String password = "testPassword";
  private final String username = "testUsername";
  private final String emailAddress = "testEmailAddress";

  @BeforeEach
  void setUp() {
    signUpService.signUp(PlayerDto.builder()
        .loginId(loginId)
        .password(password)
        .username(username)
        .emailAddress(emailAddress)
        .build());
  }

  @Test
  void signIn() throws Exception {
    String content = "{\n"
        + "    \"loginId\": \"" + loginId + "\",\n"
        + "    \"password\": \"" + password + "\"\n"
        + "}";

    mockMvc.perform(post("/v1/auth/signIn")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(content))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.code").value(0))
        .andExpect(jsonPath("$.msg").exists())
        .andDo(document("sign-in",
            requestFields(
                generateSignInRequestFields()
            ),
            responseFields(
                generateAuthDtoResponseFields(ResponseType.SINGLE,
                    "성공: true +\n실패: false", "성공 시 0을 반환", "성공: 성공하였습니다 +\n실패: 에러 메세지 반환")
            )));
  }
}