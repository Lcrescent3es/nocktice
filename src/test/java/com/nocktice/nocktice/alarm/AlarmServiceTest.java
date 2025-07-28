package com.nocktice.nocktice.alarm;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class AlarmServiceTest {

    Logger testLogger = LoggerFactory.getLogger("testLogger");

    @Test
    @DisplayName("Test successfully initiated")
    public void test() {
        assertEquals(2, 1+1, "1+1은 2입니다.");
        log.trace("TRACE log");
        testLogger.trace("TRACE log");
    }
}
