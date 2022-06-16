package com.example.oidc.service.room;

import com.example.oidc.dto.room.RoomDetailDto;
import com.example.oidc.dto.room.RoomDto;
import com.example.oidc.entity.PlayerEntity;
import com.example.oidc.repository.RoomRepository;
import com.example.oidc.service.util.AuthService;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

  private static final int VISIT_CODE_LENGTH = 10;

  private final AuthService authService;
  private final RoomRepository roomRepository;

  public Page<RoomDto> getRoomPage(Pageable pageable) {
    return roomRepository.findAll(pageable).map(RoomDto::toDto);
  }

  public RoomDetailDto createRoom(RoomDetailDto roomInfo) {

    String visitCode = null;
    do {
      visitCode = generateRandomAuthCode(VISIT_CODE_LENGTH);
    } while (roomRepository.existsRoomEntityByVisitCode(visitCode));

    roomInfo.setVisitCode(visitCode);
    PlayerEntity creator = authService.getPlayerEntityWithJWT();
    return RoomDetailDto.toDto(roomRepository.save(roomInfo.toEntity(creator)));
  }

  private String generateRandomAuthCode(int targetStringLength) {
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

}
