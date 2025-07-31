package com.nocktice.nocktice.alarm.entity;

import com.nocktice.nocktice.alarm.dto.AlarmRequestDto;
import com.nocktice.nocktice.common.support.SecurityStrategies;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "alarm")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private UUID alarmId;

    @Setter
    private String title;
    @Setter
    private String content;

    @Setter
    @Column(name = "time")
    private LocalDateTime time;

    @Enumerated(EnumType.STRING)
    @Setter
    private Status status;

    @Column(name = "security_strategy")
    private SecurityStrategies securityStrategy;

    public enum Status {
        PENDING, SENT, FAILED
    }

    @PrePersist
    public void generateUUID() {
        if (this.alarmId == null) {
            this.alarmId = UUID.randomUUID();
        }
    }

    public static Alarm of(String title, String content, LocalDateTime time) {
        if (time.isBefore(LocalDateTime.now())) {
            // TODO: 전역 예외처리
            throw new IllegalArgumentException("알람 시간은 현재보다 미래여야 합니다.");
        }

        Alarm alarm = new Alarm();
        alarm.setTitle(title != null && !title.isEmpty() ? title : "");
        alarm.setContent(content != null && !content.isEmpty() ? content : "");
        alarm.setTime(time);
        alarm.setStatus(Status.PENDING);
        return alarm;
    }

    public Alarm update(AlarmRequestDto dto) {
        if (dto.getTime().isBefore(LocalDateTime.now())) {
            // TODO: 전역 예외처리
            throw new IllegalArgumentException("알람 시간은 현재보다 미래여야 합니다.");
        }
        this.setTitle(dto.getTitle());
        this.setContent(dto.getContent());
        this.setTime(dto.getTime());
        this.setStatus(Status.PENDING);
        return this;
    }
}
