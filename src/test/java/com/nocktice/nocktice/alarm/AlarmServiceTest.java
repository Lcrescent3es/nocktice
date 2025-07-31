package com.nocktice.nocktice.alarm;

import com.nocktice.nocktice.alarm.dto.AlarmRequestDto;
import com.nocktice.nocktice.alarm.entity.Alarm;
import com.nocktice.nocktice.alarm.repository.AlarmRepository;
import com.nocktice.nocktice.alarm.service.AlarmService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class) // 가짜 객체를 사용할 수 있게 해줌
public class AlarmServiceTest {

    @InjectMocks // Mock 객체를 InjectMocks 객체에 주입
    AlarmService alarmService;

    @Mock
    AlarmRepository alarmRepository;

//    Logger testLogger = LoggerFactory.getLogger("testLogger");
//
//    @Test
//    @DisplayName("Test successfully initiated")
//    public void test() {
//        assertEquals(2, 1 + 1, "1+1은 2입니다.");
//        log.error("ERROR log");
//        log.trace("TRACE log");
//        testLogger.trace("TRACE log");
//    }

    public AlarmRequestDto init() {
        LocalDateTime time = LocalDateTime.now().plusMinutes(30);
        return AlarmRequestDto.builder()
                .title("제목1")
                .content("내용1")
                .time(time)
                .build();
    }

    @Test
    @DisplayName("UUID is returned type")
    public void AlarmRegisterTest1() {
        // given
        AlarmRequestDto dto = init();

        Alarm saved = Alarm.of(
                "제목1",
                "내용1",
                LocalDateTime.now().plusMinutes(30));
        UUID alarmId = UUID.randomUUID();
        ReflectionTestUtils.setField(saved, "alarmId", alarmId);
        given(alarmRepository.save(any(Alarm.class))).willReturn(saved);

        // when
        UUID resultId = alarmService.registerAlarm(dto);

        // then
        Assertions.assertInstanceOf(UUID.class, resultId);

        // 실패 이유: repository mock-up 객체를 사용해서 무슨 값을 가져오는지 정확히 모름
        // 그래서 given으로 행위 선언함
    }

    @Test
    @DisplayName("repository.save() method is executed")
    public void AlarmRegisterTest2() {
        // given
        AlarmRequestDto dto = init();

        Alarm saved = Alarm.of(
                "제목1",
                "내용1",
                LocalDateTime.now().plusMinutes(30));
        UUID resultId = UUID.randomUUID();
        ReflectionTestUtils.setField(saved, "alarmId", resultId);
        given(alarmRepository.save(any(Alarm.class))).willReturn(saved);

        // when
        alarmService.registerAlarm(dto);

        // then
        verify(alarmRepository).save(any(Alarm.class));
        // alarmRepository는 mock-up 객체(가짜)기 때문에 실제로 원하는대로 수행이 되지 않음
        // 그래서 수행 과정을 given에서 만들어줘야함
    }

    @Test
    @DisplayName("title이 비어있으면 예외를 던짐, content는 제외")
    public void AlarmRegisterTest3() {
        // given
        AlarmRequestDto dto = AlarmRequestDto.builder()
                .title("")
                .content("")
                .time(LocalDateTime.now().plusMinutes(30))
                .build();
        AlarmRequestDto dto2 = AlarmRequestDto.builder()
                .title("제목1")
                .content("")
                .time(LocalDateTime.now().plusMinutes(30))
                .build();

        when(alarmRepository.save(any(Alarm.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // then
        assertThrows(Exception.class, () -> {
            alarmService.registerAlarm(dto);
        });


        assertDoesNotThrow(() -> alarmService.registerAlarm(dto2));
    }

    @Test
    @DisplayName("time이 현재 시간보다 이전이면 예외를 던짐")
    public void AlarmRegisterTest4() {
        AlarmRequestDto dto = AlarmRequestDto.builder()
                .title("제목1")
                .content("")
                .time(LocalDateTime.now())
                .build();
        assertThrows(Exception.class, () -> {
            alarmService.registerAlarm(dto);
        });
    }

    @Test
    @DisplayName("findAll이 잘 동작하는지 테스트")
    public void AlarmFindTest1() {
        // given
        List<Alarm> savedAlarms = new ArrayList<>();
        AlarmRequestDto dto = init();

        // invocation: 실제로 alarmRepository.save(...)가 호출될 때의 메서드 호출 정보.
        // getArgument(0): 첫 번째 인자 (Alarm 객체)를 그대로 반환.
        when(alarmRepository.save(any(Alarm.class)))
                .thenAnswer(invocation -> {
                    Alarm alarm = invocation.getArgument(0);
                    savedAlarms.add(alarm);
                    return alarm;
                });
        when(alarmRepository.findAll()).thenReturn(savedAlarms);
        alarmService.registerAlarm(dto);
        alarmService.registerAlarm(dto);

        // when
        List<Alarm> alarmList = alarmService.getAlarmList();

        // then
        verify(alarmRepository).findAll();
        assertFalse(alarmList.isEmpty());
        assertEquals(savedAlarms.size(), alarmList.size());
        assertTrue(alarmList.contains(savedAlarms.get(0)));
        assertTrue(alarmList.contains(savedAlarms.get(1)));
    }

    @Test
    @DisplayName("findAll 메서드의 결과가 비어있을땐 예외를 던짐")
    public void AlarmFindTest2() {
        // given
        when(alarmRepository.findAll()).thenReturn(List.of());

        // then
        assertThrows(Exception.class
                , () -> alarmService.getAlarmList());
    }

    @Test
    @DisplayName("findById가 잘 동작하는지 테스트")
    public void AlarmFindTest3() {
        // given
        AlarmRequestDto dto = init();
        List<Alarm> savedAlarm = new ArrayList<>();

        // 알람 객체를 가져오기 위해 리스트에 추가
        when(alarmRepository.save(any()))
                .thenAnswer(invocation -> {
                    Alarm alarm = invocation.getArgument(0);
                    savedAlarm.add(alarm);
                    return alarm;
                });
        UUID savedId = alarmService.registerAlarm(dto);

        // 알람id로 비교해서 동일하면 반환
        when(alarmRepository.findById(eq(savedAlarm.get(0).getAlarmId())))
                .thenReturn(Optional.of(savedAlarm.get(0)));

        // when
        Alarm result = alarmService.getAlarm(savedId);

        // then
        verify(alarmRepository).findById(any());
        assertEquals(savedAlarm.get(0), result);
        assertEquals(savedId, result.getAlarmId());
    }

    @Test
    @DisplayName("findById의 결과가 없을땐 예외를 던짐")
    public void AlarmFindTest4() {
        // given
        AlarmRequestDto dto = init();
        // 랜덤 UUID 값에 새로운 Alarm 객체 매핑
        when(alarmRepository.findById(eq(UUID.randomUUID())))
                .thenReturn(Optional.of(Alarm.of(
                        dto.getTitle(), dto.getContent(), dto.getTime()
                )));

        // then
        // 랜덤 UUID로 조회해서 불일치하게 만듬
        assertThrows(Exception.class, () -> {
            alarmService.getAlarm(UUID.randomUUID());
        });
    }

    @Test
    @DisplayName("update할 알람을 못찾으면 예외를 던짐")
    public void AlarmUpdateTest1() {
        // given
        AlarmRequestDto dto = init();
        List<Alarm> saved = new ArrayList<>();
        when(alarmRepository.save(any(Alarm.class)))
                .thenAnswer(invocation -> {
                    Alarm alarm = invocation.getArgument(0);
                    saved.add(alarm);
                    return alarm;
                });
        alarmService.registerAlarm(dto);
        when(alarmRepository.findById(eq(saved.get(0).getAlarmId())))
                .thenReturn(Optional.of(saved.get(0)));

        // then
        assertThrows(Exception.class, () -> {
            alarmService.updateAlarm(UUID.randomUUID(), dto);
        });
    }

    @Test
    @DisplayName("dto title이 비어있으면 예외를 던짐")
    public void AlarmUpdateTest2() {
        // given
        AlarmRequestDto dto = AlarmRequestDto.builder()
                .title("")
                .content("내용2")
                .time(LocalDateTime.now())
                .build();
        List<Alarm> saved = new ArrayList<>();
        when(alarmRepository.save(any(Alarm.class)))
                .thenAnswer(invocation -> {
                    Alarm alarm = invocation.getArgument(0);
                    saved.add(alarm);
                    return alarm;
                });
        alarmService.registerAlarm(
                AlarmRequestDto.builder()
                        .title("제목1")
                        .content("내용1")
                        .time(LocalDateTime.now().plusSeconds(1))
                        .build()
        );

        // then
        assertThrows(Exception.class, () -> {
            alarmService.updateAlarm(saved.get(0).getAlarmId(), dto);
        });
    }

    @Test
    @DisplayName("time이 현재 이후가 아니면 예외를 던짐")
    public void AlarmUpdateTest3() {
        // given
        AlarmRequestDto dto = AlarmRequestDto.builder()
                .title("제목2")
                .content("")
                .time(LocalDateTime.now())
                .build();
        List<Alarm> saved = new ArrayList<>();
        when(alarmRepository.save(any(Alarm.class)))
                .thenAnswer(invocation -> {
                    Alarm alarm = invocation.getArgument(0);
                    saved.add(alarm);
                    return alarm;
                });
        alarmService.registerAlarm(
                AlarmRequestDto.builder()
                        .title("제목1")
                        .content("내용1")
                        .time(LocalDateTime.now().plusSeconds(1))
                        .build()
        );

        // then
        assertThrows(Exception.class, () -> {
            alarmService.updateAlarm(saved.get(0).getAlarmId(), dto);
        });
    }
}
