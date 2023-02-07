package com.lfl.meteo;

import com.lfl.meteo.extractors.TrExtractor;
import com.lfl.meteo.util.Json;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class HourlyMeteo {

    public static final Comparator<HourlyMeteo> comparator = Comparator.<HourlyMeteo>comparingInt(hourlyMeteo -> hourlyMeteo.getDate().getYear())
            .thenComparingInt(hourlyMeteo -> hourlyMeteo.getDate().getMonthValue())
            .thenComparingInt(hourlyMeteo -> hourlyMeteo.getDate().getDayOfMonth())
            .thenComparingInt(HourlyMeteo::getHour);

    public static final String csvHeader = String.join(",", List.of(
            "YEAR",
            "MONTH",
            "DAY",
            "HOUR",
            "TEMPERATURE",
            "SUNNY_TIME",
            "HUMIDITY",
            "HUMIDEX",
            "WINDCHILL",
            "WIND_DIRECTION",
            "WIND_DIRECTION_DEG",
            "WIND_SPEED",
            "WIND_RAFALE",
            "PRESSURE",
            "RAIN"
    ));


    private LocalDate date;
    private int hour;
    private double temperature;
    private int sunnyTime;
    private int humidity;
    private double humidex;
    private double windchill;
    private String windDirection;
    private int windDirectionDeg;
    private int windSpeed;
    private int windRafale;
    private double pressure;
    private double rain;

    public static HourlyMeteo buildFrom(LocalDate date, Element tr) {
//       log.info("hour = {}", TrExtractor.hourExtractor.extract(tr));
        return HourlyMeteo.builder()
                .date(date)
                .hour(TrExtractor.hourExtractor.extract(tr))
                .temperature(TrExtractor.temperatureExtractor.extract(tr))
                .sunnyTime(TrExtractor.sunnyTimeExtractor.extract(tr))
                .humidity(TrExtractor.humidityExtractor.extract(tr))
                .humidex(TrExtractor.humidexExtractor.extract(tr))
                .windchill(TrExtractor.windchillExtractor.extract(tr))
                .windDirection(TrExtractor.windDirectionExtractor.extract(tr))
                .windDirectionDeg(TrExtractor.windDirectionDegExtractor.extract(tr))
                .windSpeed(TrExtractor.windSpeedExtractor.extract(tr))
                .windRafale(TrExtractor.windRafaleExtractor.extract(tr))
                .pressure(TrExtractor.pressureExtractor.extract(tr))
                .rain(TrExtractor.rainExtractor.extract(tr))
                .build();
    }

    @Override
    public String toString() {
        return Json.write(this);
    }

    public String toCsvString() {
        List<String> values = List.of(
                date.getYear() + "",
                date.getMonthValue() + "",
                date.getDayOfMonth() + "",
                hour + "",
                temperature + "",
                sunnyTime + "",
                humidity + "",
                humidex + "",
                windchill + "",
                windDirection,
                windDirectionDeg + "",
                windSpeed + "",
                windRafale + "",
                pressure + "",
                rain + ""
        );

        return String.join(",", values);
    }
}
