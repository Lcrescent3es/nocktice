package com.nocktice.nocktice.alarm.service;

import com.nocktice.nocktice.alarm.dto.AlarmRequestDto;
import com.nocktice.nocktice.alarm.entity.Alarm;
import com.nocktice.nocktice.alarm.repository.AlarmRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;

    @Transactional
    public UUID registerAlarm(AlarmRequestDto dto) {
        if (dto.getTitle() == null || dto.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (dto.getTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Time cannot be in the past");
        }

        Alarm alarm = Alarm.of(
                dto.getTitle(), dto.getContent(), LocalDateTime.now().plusMinutes(30)
        );
        return alarmRepository.save(alarm).getAlarmId();
    }

    @Transactional
    public List<Alarm> getAlarmList() {
        List<Alarm> alarmList = alarmRepository.findAll();
        if (alarmList.isEmpty()) {
            throw new IllegalArgumentException("No alarms found");
        }
        return alarmList;
    }

    @Transactional
    public Alarm getAlarm(UUID id) {
        return alarmRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("No alarm found with id: " + id)
                );
    }

    @Transactional
    public UUID updateAlarm(UUID id, AlarmRequestDto dto) {
        if (dto.getTitle() == null || dto.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (dto.getTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Time cannot be in the past");
        }
        Alarm alarm = getAlarm(id);
        alarm.update(dto);
        return alarm.getAlarmId();
    }

    @Transactional
    public void deleteAlarm(UUID id) {
        alarmRepository.deleteById(id);
    }
}
