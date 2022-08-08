package com.example.oidc.controller;

import static com.example.oidc.config.security.JwtTokenProvider.tokenValidMillisecond;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

import com.example.oidc.entity.PlayerEntity;
import com.example.oidc.entity.RoomEntity;
import com.example.oidc.exception.room.CustomEmptyRoomIsNotExistException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;

public class ApiControllerTestHelper extends ApiControllerTestSetUp {

  public enum ResponseType {
    SINGLE("data"),
    LIST("list[]"),
    PAGE("page.content[]");

    private final String responseFieldPrefix;

    ResponseType(String responseFieldPrefix) {
      this.responseFieldPrefix = responseFieldPrefix;
    }

    public String getResponseFieldPrefix() {
      return responseFieldPrefix;
    }
  }

  protected String asJsonString(final Object obj) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      final String jsonContent = mapper.writeValueAsString(obj);
      return jsonContent;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public PlayerEntity generatePlayerEntity() {
    String uuid = UUID.randomUUID().toString().substring(0, 10);
    return playerRepository.save(
        PlayerEntity.builder()
            .loginId("loginId_" + uuid)
            .password("password_" + uuid)
            .username("username_" + uuid)
            .emailAddress("emailAddress_" + uuid)
            .registerTime(LocalDateTime.now())
            .recentLoginTime(LocalDateTime.now())
            .build());
  }

  public String getToken(PlayerEntity player) {
    return jwtTokenProvider.createToken(String.valueOf(player.getId()),
        List.of("ROLE_USER"), tokenValidMillisecond);
  }

  public RoomEntity generateRoomEntity(PlayerEntity creator, Long maxPlayer) {
    String uuid = UUID.randomUUID().toString().substring(0, 10);
    String name = "roomName_" + uuid;
    String visitCode = uuid;

    RoomEntity roomEntity = roomRepository.findFirstByNameIsNull()
        .orElseThrow(CustomEmptyRoomIsNotExistException::new);
    roomEntity.setName(name);
    roomEntity.setVisitCode(visitCode);
    roomEntity.setCreateTime(LocalDateTime.now());
    roomEntity.setMaxPlayer(maxPlayer);
    roomEntity.setCreatorPlayerEntity(creator);
    return roomRepository.save(roomEntity);
  }

  public List<FieldDescriptor> generateCommonResponseFields(String docSuccess, String docCode,
      String docMsg) {
    List<FieldDescriptor> commonFields = new ArrayList<>();
    commonFields.addAll(Arrays.asList(
        fieldWithPath("success").description(docSuccess),
        fieldWithPath("code").description(docCode),
        fieldWithPath("msg").description(docMsg)));
    return commonFields;
  }

  public List<ParameterDescriptor> generateCommonPagingParameters(String sizeDescription,
      ParameterDescriptor... descriptors) {
    List<ParameterDescriptor> commonFields = new ArrayList<>();
    commonFields.addAll(Arrays.asList(
        parameterWithName("page").optional().description("페이지 번호(default = 0)"),
        parameterWithName("size").optional().description(sizeDescription)
    ));
    if (descriptors.length > 0) {
      commonFields.addAll(Arrays.asList(descriptors));
    }
    return commonFields;
  }

  public List<FieldDescriptor> generateSignUpRequestFields(FieldDescriptor... addDescriptors) {
    List<FieldDescriptor> commonFields = new ArrayList<>();
    commonFields.addAll(Arrays.asList(
        fieldWithPath("loginId").description("로그인 아이디"),
        fieldWithPath("emailAddress").description("이메일 주소"),
        fieldWithPath("password").description("로그인 비밀번호"),
        fieldWithPath("username").description("닉네임")
    ));
    if (addDescriptors.length > 0) {
      commonFields.addAll(Arrays.asList(addDescriptors));
    }
    return commonFields;
  }

  public List<FieldDescriptor> generateSignInRequestFields(FieldDescriptor... addDescriptors) {
    List<FieldDescriptor> commonFields = new ArrayList<>();
    commonFields.addAll(Arrays.asList(
        fieldWithPath("loginId").description("로그인 아이디"),
        fieldWithPath("password").description("로그인 비밀번호")
    ));
    if (addDescriptors.length > 0) {
      commonFields.addAll(Arrays.asList(addDescriptors));
    }
    return commonFields;
  }

  public List<FieldDescriptor> generatePlayerDtoResponseFields(ResponseType type,
      String success, String code, String msg, FieldDescriptor... addDescriptors) {
    String prefix = type.getResponseFieldPrefix();
    List<FieldDescriptor> commonFields = new ArrayList<>();
    commonFields.addAll(generateCommonResponseFields(success, code, msg));
    commonFields.addAll(Arrays.asList(
        fieldWithPath(prefix + ".id").description("유저 고유 ID"),
        fieldWithPath(prefix + ".loginId").description("로그인 아이디"),
        fieldWithPath(prefix + ".emailAddress").description("이메일 주소"),
        fieldWithPath(prefix + ".username").description("닉네임")
    ));
    if (addDescriptors.length > 0) {
      commonFields.addAll(Arrays.asList(addDescriptors));
    }
    return commonFields;
  }

  public List<FieldDescriptor> generateAuthDtoResponseFields(ResponseType type,
      String success, String code, String msg, FieldDescriptor... addDescriptors) {
    String prefix = type.getResponseFieldPrefix();
    List<FieldDescriptor> commonFields = new ArrayList<>();
    commonFields.addAll(generateCommonResponseFields(success, code, msg));
    commonFields.addAll(Arrays.asList(
        fieldWithPath(prefix + ".id").description("유저 고유 ID"),
        fieldWithPath(prefix + ".loginId").description("로그인 아이디"),
        fieldWithPath(prefix + ".emailAddress").description("이메일 주소"),
        fieldWithPath(prefix + ".username").description("닉네임"),
        fieldWithPath(prefix + ".token").description("JWT 토큰")
    ));
    if (addDescriptors.length > 0) {
      commonFields.addAll(Arrays.asList(addDescriptors));
    }
    return commonFields;
  }

  public List<FieldDescriptor> generatePlayerDetailDtoResponseFields(ResponseType type,
      String success, String code, String msg, String dtoName, FieldDescriptor... addDescriptors) {
    String prefix = type.getResponseFieldPrefix();
    List<FieldDescriptor> commonFields = new ArrayList<>();
    commonFields.addAll(generateCommonResponseFields(success, code, msg));
    commonFields.addAll(Arrays.asList(
        fieldWithPath(prefix + dtoName + ".id").description("유저 고유 ID"),
        fieldWithPath(prefix + dtoName + ".loginId").description("로그인 아이디"),
        fieldWithPath(prefix + dtoName + ".emailAddress").description("이메일 주소"),
        fieldWithPath(prefix + dtoName + ".username").description("닉네임"),
        fieldWithPath(prefix + dtoName + ".registerTime").description("가입 시간"),
        fieldWithPath(prefix + dtoName + ".recentLoginTime").description("최근 로그인 시간"),
        fieldWithPath(prefix + dtoName + ".createRoomList").description("생성한 방 목록"),
        fieldWithPath(prefix + dtoName + ".joinRoomList").description("접속한 방 목록")
    ));
    if (addDescriptors.length > 0) {
      commonFields.addAll(Arrays.asList(addDescriptors));
    }
    return commonFields;
  }

  public List<FieldDescriptor> generatePlayerDetailDtoResponseFields(ResponseType type,
      String success, String code, String msg, FieldDescriptor... addDescriptors) {
    return generatePlayerDetailDtoResponseFields(type, success, code, msg, "", addDescriptors);
  }

  public List<FieldDescriptor> generateRoomDtoResponseFields(ResponseType type,
      String success, String code, String msg, FieldDescriptor... addDescriptors) {
    String prefix = type.getResponseFieldPrefix();
    List<FieldDescriptor> commonFields = new ArrayList<>();
    commonFields.addAll(generateCommonResponseFields(success, code, msg));
    commonFields.addAll(Arrays.asList(
        fieldWithPath(prefix + ".id").description("방 고유 ID"),
        fieldWithPath(prefix + ".name").description("방 이름"),
        fieldWithPath(prefix + ".visitCode").description("방 접속 코드"),
        fieldWithPath(prefix + ".maxPlayer").description("최대 플레이어 수"),
        fieldWithPath(prefix + ".currentPlayer").description("현재 플레이어 수")
    ));
    if (type.equals(ResponseType.PAGE)) {
      commonFields.addAll(Arrays.asList(
          fieldWithPath("page.empty").description("페이지가 비었는 지 여부"),
          fieldWithPath("page.first").description("첫 페이지 인지"),
          fieldWithPath("page.last").description("마지막 페이지 인지"),
          fieldWithPath("page.number").description("요소를 가져 온 페이지 번호 (0부터 시작)"),
          fieldWithPath("page.numberOfElements").description("요소 개수"),
          subsectionWithPath("page.pageable").description("해당 페이지에 대한 DB 정보"),
          fieldWithPath("page.size").description("요청한 페이지 크기"),
          subsectionWithPath("page.sort").description("정렬에 대한 정보"),
          fieldWithPath("page.totalElements").description("총 요소 개수"),
          fieldWithPath("page.totalPages").description("총 페이지")));
    }
    if (addDescriptors.length > 0) {
      commonFields.addAll(Arrays.asList(addDescriptors));
    }
    return commonFields;
  }

  public List<FieldDescriptor> generateRoomDetailDtoResponseFields(ResponseType type,
      String success, String code, String msg, FieldDescriptor... addDescriptors) {
    String prefix = type.getResponseFieldPrefix();
    List<FieldDescriptor> commonFields = new ArrayList<>();
    commonFields.addAll(generateCommonResponseFields(success, code, msg));
    commonFields.addAll(generateRoomDtoResponseFields(type, success, code, msg));
    commonFields.addAll(Arrays.asList(
        fieldWithPath(prefix + ".createTime").description("생성한 시간"),
        fieldWithPath(prefix + ".visitUrl").description("방 입장 URL"),
        fieldWithPath(prefix + ".visitPort").description("방 입장 Port"),
        subsectionWithPath(prefix + ".creatorPlayer").description("생성한 플레이어"),
        subsectionWithPath(prefix + ".joinPlayerList").description("참가한 플레이어 리스트")
    ));
    if (addDescriptors.length > 0) {
      commonFields.addAll(Arrays.asList(addDescriptors));
    }
    return commonFields;
  }

  public List<FieldDescriptor> generateEnterUrlResponseFields(ResponseType type,
      String success, String code, String msg, FieldDescriptor... addDescriptors) {
    String prefix = type.getResponseFieldPrefix();
    List<FieldDescriptor> commonFields = new ArrayList<>();
    commonFields.addAll(generateCommonResponseFields(success, code, msg));
    commonFields.addAll(Arrays.asList(
        fieldWithPath(prefix).description("redirect 할 URL\r\n"
            + "visitCode가 일치하지 않을 경우 빈 문자열을 return합니다.")
    ));
    if (addDescriptors.length > 0) {
      commonFields.addAll(Arrays.asList(addDescriptors));
    }
    return commonFields;
  }
}