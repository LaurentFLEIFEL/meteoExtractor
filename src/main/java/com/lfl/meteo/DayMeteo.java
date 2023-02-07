package com.lfl.meteo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collection;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;

@Data
@Builder
public class DayMeteo {

    private LocalDate date;
    private DoubleSummaryStatistics temperature;
    private IntSummaryStatistics sunnyTime;
    private IntSummaryStatistics humidity;
    private IntSummaryStatistics windDirectionDeg;
    private IntSummaryStatistics windSpeed;
    private IntSummaryStatistics windRafale;
    private DoubleSummaryStatistics pressure;
    private DoubleSummaryStatistics rain;

    public static DayMeteo of(Collection<HourlyMeteo> hourlyMeteos) {
        return DayMeteo.builder()
                .temperature(extractDoubleSummaryStatistics(hourlyMeteos, HourlyMeteo::getTemperature))
                .sunnyTime(extractIntSummaryStatistics(hourlyMeteos, HourlyMeteo::getSunnyTime))
                .humidity(extractIntSummaryStatistics(hourlyMeteos, HourlyMeteo::getHumidity))
                .windDirectionDeg(extractIntSummaryStatistics(hourlyMeteos, HourlyMeteo::getWindDirectionDeg))
                .windSpeed(extractIntSummaryStatistics(hourlyMeteos, HourlyMeteo::getWindSpeed))
                .windRafale(extractIntSummaryStatistics(hourlyMeteos, HourlyMeteo::getWindRafale))
                .pressure(extractDoubleSummaryStatistics(hourlyMeteos, HourlyMeteo::getPressure))
                .rain(extractDoubleSummaryStatistics(hourlyMeteos, HourlyMeteo::getRain))
                .build();
    }

    private static DoubleSummaryStatistics extractDoubleSummaryStatistics(Collection<HourlyMeteo> hourlyMeteos, ToDoubleFunction<HourlyMeteo> extractor) {
        return hourlyMeteos.stream().mapToDouble(extractor).summaryStatistics();
    }

    private static IntSummaryStatistics extractIntSummaryStatistics(Collection<HourlyMeteo> hourlyMeteos, ToIntFunction<HourlyMeteo> extractor) {
        return hourlyMeteos.stream().mapToInt(extractor).summaryStatistics();
    }
}
