package com.example.oidc.service.room;

import static com.example.oidc.repository.PlayerRepository.VIRTUAL_MEMBER_ID;

import com.example.oidc.dto.response.CommonResult;
import com.example.oidc.dto.room.RoomDetailDto;
import com.example.oidc.dto.room.RoomDto;
import com.example.oidc.entity.PlayerEntity;
import com.example.oidc.entity.RoomEntity;
import com.example.oidc.exception.room.CustomEmptyRoomIsNotExistException;
import com.example.oidc.exception.room.CustomRoomNotFoundException;
import com.example.oidc.exception.room.CustomRoomOvercapacityException;
import com.example.oidc.repository.PlayerRepository;
import com.example.oidc.repository.RoomRepository;
import com.example.oidc.service.ResponseService;
import com.example.oidc.service.util.AuthService;
import java.time.LocalDateTime;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomService {

  private static final int VISIT_CODE_LENGTH = 10;

  private final AuthService authService;
  private final RoomRepository roomRepository;
  private final PlayerRepository playerRepository;
  private final ResponseService responseService;

  public Page<RoomDto> getRoomPage(Pageable pageable) {
    return roomRepository.findAllByNameIsNotNull(pageable).map(RoomDto::toDto);
  }

  @Transactional
  public RoomDetailDto createRoom(RoomDetailDto roomInfo) {

    RoomEntity emptyRoom = roomRepository.findFirstByNameIsNull()
        .orElseThrow(CustomEmptyRoomIsNotExistException::new);
    fillRoomInfoInEmptyRoom(roomInfo, emptyRoom);
    return RoomDetailDto.toDto(roomRepository.save(emptyRoom));
  }

  private void fillRoomInfoInEmptyRoom(RoomDetailDto roomInfo, RoomEntity emptyRoom) {
    String visitCode;
    do {
      visitCode = generateRandomVisitCode(VISIT_CODE_LENGTH);
    } while (roomRepository.existsRoomEntityByVisitCode(visitCode));

    PlayerEntity creator = authService.getPlayerEntityWithJWT();
    emptyRoom.setName(roomInfo.getName());
    emptyRoom.setVisitCode(visitCode);
    emptyRoom.setMaxPlayer(roomInfo.getMaxPlayer());
    emptyRoom.setCreatorPlayerEntity(creator);
    emptyRoom.setCreateTime(LocalDateTime.now());
  }

  private String generateRandomVisitCode(int targetStringLength) {
    int leftLimit = 48; // numeral '0'
    int rightLimit = 122; // letter 'z'
    Random random = new Random();

    return random.ints(leftLimit, rightLimit + 1)
        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
        .limit(targetStringLength)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
    // 출처: https://www.baeldung.com/java-random-string
  }

  public String getEnterUrl(Long roomId, String visitCode) {
    RoomEntity roomEntity = roomRepository.findById(roomId)
        .orElseThrow(CustomRoomNotFoundException::new);
    if (visitCode.equals(roomEntity.getVisitCode())) {
      return roomEntity.getVisitUrl() + ":" + roomEntity.getVisitPort();
    }
    return "";
  }

  public CommonResult joinRoom(Integer port) {
    RoomEntity room = roomRepository.findByVisitPort(port)
        .orElseThrow(CustomRoomNotFoundException::new);
    if (room.getName() == null) {
      throw new CustomRoomNotFoundException();
    }
    room.increaseCurrentPlayer();
    if (room.getMaxPlayer() < room.getCurrentPlayer()) {
      throw new CustomRoomOvercapacityException();
    }
    roomRepository.save(room);
    return responseService.getSuccessResult();
  }

  public CommonResult leaveRoom(Integer port) {
    RoomEntity room = roomRepository.findByVisitPort(port)
        .orElseThrow(CustomRoomNotFoundException::new);
    if (room.getName() == null) {
      throw new CustomRoomNotFoundException();
    }
    if (room.getCurrentPlayer() == 0) {
      throw new CustomRoomNotFoundException();
    }
    room.decreaseCurrentPlayer();
    if (room.getCurrentPlayer() == 0) {
      PlayerEntity virtualPlayer = playerRepository.findById(VIRTUAL_MEMBER_ID)
          .orElseThrow(() -> new RuntimeException("가상 플레이어가 없습니다. 전산관리자에게 문의하세요."));
      room.setName(null);
      room.setVisitCode(null);
      room.setCreateTime(null);
      room.setMaxPlayer(null);
      room.setCreatorPlayerEntity(virtualPlayer);
    }
    roomRepository.save(room);
    return responseService.getSuccessResult();
  }
}
