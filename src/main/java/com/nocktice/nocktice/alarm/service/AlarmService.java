package com.nocktice.nocktice.alarm.service;

import com.nocktice.nocktice.alarm.dto.AlarmRequestDto;
import com.nocktice.nocktice.alarm.entity.Alarm;
import com.nocktice.nocktice.alarm.repository.AlarmRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;

    @Transactional
    public void registerAlarm(AlarmRequestDto dto) {
        Alarm alarm = Alarm.of(dto.getTitle(), dto.getContent(), dto.getTime());

        alarmRepository.save(alarm);
    }
}
