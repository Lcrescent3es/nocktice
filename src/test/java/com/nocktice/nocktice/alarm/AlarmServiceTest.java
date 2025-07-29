package com.nocktice.nocktice.alarm;

import com.nocktice.nocktice.alarm.dto.AlarmRequestDto;
import com.nocktice.nocktice.alarm.entity.Alarm;
import com.nocktice.nocktice.alarm.repository.AlarmRepository;
import com.nocktice.nocktice.alarm.service.AlarmService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Slf4j
public class AlarmServiceTest {

    Logger testLogger = LoggerFactory.getLogger("testLogger");

    @Test
    @DisplayName("Test successfully initiated")
    public void test() {
        assertEquals(2, 1 + 1, "1+1은 2입니다.");
        log.error("ERROR log");
        log.trace("TRACE log");
        testLogger.trace("TRACE log");
    }

    @Test
    @DisplayName("알람을 예약하면 알람 리스트에 추가된다.")
    public void AlarmRegisterTest() {
        LocalDateTime time = LocalDateTime.now().plusMinutes(30);
        // given
        AlarmRepository alarmRepository = mock(AlarmRepository.class);
        AlarmService alarmService = new AlarmService(alarmRepository); // 나중에 Mockito로 DI 가능
        AlarmRequestDto dto = AlarmRequestDto.builder()
                .title("Test Alarm")
                .content("after 30 minute from now on")
                .time(time)
                .build();
        // when
        alarmService.registerAlarm(dto);

        // then
        ArgumentCaptor<Alarm> captor = ArgumentCaptor.forClass(Alarm.class);
        verify(alarmRepository, times(1)).save(captor.capture());

        Alarm savedAlarm = captor.getValue();
        assertEquals("Test Alarm", savedAlarm.getTitle());
        assertEquals("after 30 minute from now on", savedAlarm.getContent());
        assertEquals(time, savedAlarm.getTime());

    }
}
