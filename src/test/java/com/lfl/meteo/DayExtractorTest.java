package com.lfl.meteo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Slf4j
class DayExtractorTest {

    @Test
    void name2() {
        LocalDate from = LocalDate.of(2015  , 11, 26);
        LocalDate to = LocalDate.of(2015, 11, 26);
        Instant start = Instant.now();
        log.info("Start run");
        List<HourlyMeteo> extract = DayExtractor.extractFromTo(from, to);
        System.out.println("extract.size() = " + extract.size());
        log.info("End run");
        Instant end = Instant.now();
        log.info("Elapsed time = {}ms", Duration.between(start, end).toMillis());
    }
}