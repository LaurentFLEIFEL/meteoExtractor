package com.lfl.meteo;

import com.lfl.meteo.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.collector.Collectors2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
@Slf4j
public class MeteoExtractorApplication implements CommandLineRunner {

    public MeteoExtractorApplication(ApplicationContext context) {

    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MeteoExtractorApplication.class);
        app.addListeners(new ApplicationPidFileWriter());
        app.run(args);
    }

    @Override
    public void run(String... args) {
        Instant start = Instant.now();
        log.info("Start run");

        //recordMeteos();

        analyze();

        log.info("End run");
        Instant end = Instant.now();
        log.info("Elapsed time = {}ms", Duration.between(start, end).toMillis());
    }

    private static void analyze() {
        try {
            MutableList<HourlyMeteo> hourlyMeteos = Files.readAllLines(Paths.get("result.txt"))
                    .stream()
                    .map(line -> Json.read(line, HourlyMeteo.class))
                    .collect(Collectors2.toList());

            log.info("number = {}", hourlyMeteos.size());

//            Map<String, Integer> collect = hourlyMeteos.stream()
//                    .collect(Collectors.groupingBy(hourlyMeteo -> hourlyMeteo.getDate().getYear() + "=" + hourlyMeteo.getDate().getMonthValue(), Collectors.summingInt(HourlyMeteo::getSunnyTime)));
//
//            log.info("collect = \n{}", collect.entrySet().stream().map(Object::toString).collect(Collectors.joining("\n")));

            try {
                List<String> toWrite = hourlyMeteos
                        .reject(hourlyMeteo -> hourlyMeteo.getDate().getYear() == 2011)
                        .reject(hourlyMeteo -> hourlyMeteo.getDate().getYear() == 2012)
                        .reject(hourlyMeteo -> hourlyMeteo.getDate().getYear() == 2013)
                        .sortThis(HourlyMeteo.comparator)
                        .stream()
                        .collect(Collectors.groupingBy(HourlyMeteo::getDate, Collectors.averagingDouble(HourlyMeteo::getTemperature)))
                        .entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByKey())
                        .map(entry -> entry.getKey().format(DateTimeFormatter.ISO_LOCAL_DATE) + "," + entry.getValue())
                        .collect(Collectors.toList());
                toWrite.add(0, "DATE,TEMPERATURE");
                Files.write(Paths.get("resultCsvTemp.csv"), toWrite);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void recordMeteos() {
        LocalDate from = LocalDate.of(2011, 1, 1);
        LocalDate to = LocalDate.of(2023, 1, 25);

        MutableList<HourlyMeteo> hourlyMeteos = DayExtractor.extractFromTo(from, to);
        try {
            Files.write(Paths.get("result.txt"), hourlyMeteos.collect(HourlyMeteo::toString));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
