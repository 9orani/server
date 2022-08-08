package com.example.oidc.controller.room;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.oidc.controller.ApiControllerTestHelper;
import com.example.oidc.dto.room.RoomDetailDto;
import com.example.oidc.entity.PlayerEntity;
import com.example.oidc.entity.RoomEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class RoomControllerTest extends ApiControllerTestHelper {

  @Test
  void getRoomPage() throws Exception {
    assignAllRoom();

    PlayerEntity creator = generatePlayerEntity();

    String token = getToken(creator);
    mockMvc.perform(get("/v1/rooms")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", token)
            .param("page", "0")
            .param("size", "10"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.code").value(0))
        .andExpect(jsonPath("$.msg").exists())
        .andDo(document("get-room-list",
            pathParameters(
                generateCommonPagingParameters("페이지 사이즈(default = 10)")
            ),
            responseFields(
                generateRoomDtoResponseFields(ResponseType.PAGE,
                    "성공: true +\n실패: false", "성공 시 0을 반환", "성공: 성공하였습니다 +\n실패: 에러 메세지 반환")
            )));

  }

  @Test
  void createRoom() throws Exception {
    PlayerEntity creator = generatePlayerEntity();
    String token = getToken(creator);

    RoomDetailDto content = RoomDetailDto.builder()
        .name("newRoom")
        .maxPlayer(10L)
        .build();

    mockMvc.perform(post("/v1/rooms")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", token)
            .content(asJsonString(content)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.code").value(0))
        .andExpect(jsonPath("$.msg").exists())
//        .andExpect(jsonPath("$.page.content.length()").value(10))
        .andDo(document("create-room",
            requestFields(
                fieldWithPath("name").description("방 이름"),
                fieldWithPath("maxPlayer").description("최대 플레이어 수")
            ),
            responseFields(
                generateRoomDetailDtoResponseFields(ResponseType.SINGLE,
                    "성공: true \r\n 빈 방이 없어서 방 생성이 불가능할 경우: 204 status code를 반환합니다.",
                    "성공 시 0을 반환\r\n 빈 방이 없어서 방 생성이 불가능할 경우: " + messageSource.getMessage(
                        "emptyRoomIsNotExist.code", null, LocaleContextHolder.getLocale()),
                    "성공: 성공하였습니다 +\r\n빈 방이 없어서 방 생성이 불가능할 경우: " + messageSource.getMessage(
                        "emptyRoomIsNotExist.msg", null, LocaleContextHolder.getLocale()))
            )));
  }

  @Test
  void createRoomFailedByEmptyRoomIsNotExist() throws Exception {
    assignAllRoom();

    PlayerEntity creator = generatePlayerEntity();
    String token = getToken(creator);

    RoomDetailDto content = RoomDetailDto.builder()
        .name("newRoom")
        .maxPlayer(10L)
        .build();

    mockMvc.perform(post("/v1/rooms")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", token)
            .content(asJsonString(content)))
        .andDo(print())
        .andExpect(status().isNoContent())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.code").value(messageSource.getMessage(
            "emptyRoomIsNotExist.code", null, LocaleContextHolder.getLocale())))
        .andExpect(jsonPath("$.msg").value(messageSource.getMessage(
            "emptyRoomIsNotExist.msg", null, LocaleContextHolder.getLocale())));
  }

  private void assignAllRoom() {
    List<RoomEntity> allRoom = roomRepository.findAll();
    allRoom.forEach(roomEntity -> {
      roomEntity.setName(String.valueOf(roomEntity.getId()));
      roomEntity.setVisitCode(String.valueOf(roomEntity.getId()));
      roomEntity.setMaxPlayer(5L);
      roomEntity.setCreateTime(LocalDateTime.now());
    });
    roomRepository.saveAll(allRoom);
  }
}