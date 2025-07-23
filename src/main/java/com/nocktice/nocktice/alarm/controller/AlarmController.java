package com.nocktice.nocktice.alarm.controller;

import com.nocktice.nocktice.alarm.dto.AlarmRequestDto;
import com.nocktice.nocktice.alarm.service.AlarmService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/alarm")
public class AlarmController {
    private final AlarmService alarmService;

    public AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @PostMapping("/register")
    public String register(@ModelAttribute AlarmRequestDto dto) {
        alarmService.registerAlarm(dto);
        return "alarm/success";
    }

    @GetMapping("/form")
    public String alarmForm() {
        return "alarm/form";
    }
}
