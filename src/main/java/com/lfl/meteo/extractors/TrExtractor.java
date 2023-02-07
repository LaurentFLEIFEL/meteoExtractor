package com.lfl.meteo.extractors;

import com.lfl.meteo.util.StringHelper;
import org.eclipse.collections.api.list.primitive.ImmutableIntList;
import org.jsoup.nodes.Element;

public interface TrExtractor<T> {
    T extract(Element tr);

    TrExtractor<Integer> hourExtractor = tr -> StringHelper.ints(tr.selectXpath("td[1]").text()).get(0);
    TrExtractor<Double> temperatureExtractor = tr -> {
        String s = tr.selectXpath("td[5]").text().strip().split(" ")[0];
        if (s.isBlank()) {
            return -1d;
        }
        return Double.parseDouble(s);
    };
    TrExtractor<Integer> sunnyTimeExtractor = tr -> {
        String html = tr.selectXpath("td[5]").html();
        if (!html.contains("</i>")) {
            return 0;
        }
        return StringHelper.ints(html.substring(html.indexOf("</i>"), html.indexOf("<br>")).strip()).get(0);
    };
    TrExtractor<Integer> humidityExtractor = tr -> {
        ImmutableIntList ints = StringHelper.ints(tr.selectXpath("td[6]").text());
        if (ints.isEmpty()) {
            return -1;
        }
        return ints.get(0);
    };
    TrExtractor<Double> humidexExtractor = tr -> {
        String text = tr.selectXpath("td[7]").text();
        if (text.isBlank()) {
            return -1d;
        }
        return Double.parseDouble(text);
    };
    TrExtractor<Double> windchillExtractor = tr -> {
        String text = tr.selectXpath("td[8]").text();
        if (text.isBlank()) {
            return -1d;
        }
        return Double.parseDouble(text);
    };
    TrExtractor<String> windDirectionExtractor = tr -> {
        String html = tr.selectXpath("td[9]").html();
        if (!html.contains("</i>")) {
            return "";
        }
        if (html.contains("Variable")) {
            return "Variable";
        }
        return html.substring(html.indexOf("</i>"), html.indexOf("<small>")).strip().substring(4);
    };
    TrExtractor<Integer> windDirectionDegExtractor = tr -> {
        String html = tr.selectXpath("td[9]").html();
        if (!html.contains("</i>")) {
            return -1;
        }
        if (html.contains("Variable")) {
            return -1;
        }
        return StringHelper.ints(html.substring(html.indexOf("<small>"), html.indexOf("</small>")).strip()).get(0);
    };
    TrExtractor<Integer> windSpeedExtractor = tr -> {
        ImmutableIntList ints = StringHelper.ints(tr.selectXpath("td[10]").text());
        if (ints.isEmpty()) {
            return -1;
        }
        return ints.get(0);
    };
    TrExtractor<Integer> windRafaleExtractor = tr -> {
        ImmutableIntList ints = StringHelper.ints(tr.selectXpath("td[10]").text());
        if (ints.size() > 1) {
            return ints.get(1);
        }
        return -1;
    };
    TrExtractor<Double> pressureExtractor = tr -> {
        String text = tr.selectXpath("td[11]").text().strip().split(" ")[0];
        if (text.isBlank()) {
            return -1d;
        }
        return Double.parseDouble(text);
    };
    TrExtractor<Double> rainExtractor = tr -> {
        String text = tr.selectXpath("td[12]").text().strip();
        if (text.isBlank() || "aucune".equals(text.split(" ")[0])) {
            return 0d;
        }
        return Double.parseDouble(text.split(" ")[0]);
    };
}
