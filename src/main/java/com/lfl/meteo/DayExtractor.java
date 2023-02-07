package com.lfl.meteo;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.collector.Collectors2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Stream;

@Slf4j
@Service
public class DayExtractor {
    private static Stream<HourlyMeteo> extract(LocalDate date) {
        log.info("Extracting for date = {}", date);
        Document document;
        try {
            document = Jsoup.connect("https://www.meteociel.fr/temps-reel/obs_villes.php?code2=6484&jour2=" + date.getDayOfMonth() +
                            "&mois2=" + (date.getMonthValue() - 1) +
                            "&annee2=" + date.getYear())
                    .get();
            Elements elements = document.selectXpath("/html/body/table[1]/tbody/tr[2]/td[2]/table/tbody/tr[2]/td/table/tbody/tr/td");

            Element parent1 = elements.get(0).lastElementChild();
            Elements elements1 = parent1.selectXpath("table[2]/tbody/tr");
            return elements1.stream().skip(1)
                    .map(element -> HourlyMeteo.buildFrom(date, element));
        } catch (Throwable t) {
            log.error("Error for date = " + date, t);
            return Stream.empty();
        }
    }

    public static MutableList<HourlyMeteo> extractFromTo(LocalDate from, LocalDate to) {
        return from.datesUntil(to.plusDays(1L))
                .parallel()
                .flatMap(DayExtractor::extract)
                .collect(Collectors2.toList());
    }
}
