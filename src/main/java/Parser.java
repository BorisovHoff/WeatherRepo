import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public static Document getPage() throws IOException {
        String url = "http://www.pogoda.spb.ru/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;

    }

    //25.11 Воскресенье погода сегодня
    //25.11
    // \d{2}\.\d{2}

    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");

    private static String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Не могу получить дату из строки");
    }

    private static int printPartValues(Elements values, int index) {
        int iterationCount = 4;
        if (index == 0) {
            Element valueLn = values.get(3);
            boolean isMorning = valueLn.text().contains("Утро");
            boolean isDay = valueLn.text().contains("День");
            boolean isEvening = valueLn.text().contains("Вечер");
            boolean isNight = valueLn.text().contains("Ночь");
            if (isMorning) {
                iterationCount = 3;
            } else {
                if (isDay) {
                    iterationCount = 2;
                } else {
                    if (isEvening) {
                        iterationCount = 1;
                    } else {
                        if (isNight) {
                            iterationCount = 0;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < iterationCount; i++) {
            Element valueLine = values.get(index + i);
            for (Element td : valueLine.select("td")) {
                System.out.print(td.text() + "  ");
            }
            System.out.println();
        }
        return iterationCount;

    }

    public static void main(String[] args) throws Exception {
        Document page = getPage();
        // css quere language
        Element tableWth = page.select("table[class=wt]").first();
        Elements names = tableWth.select("tr[class=wth]");
        int index = 0;
        Elements values = tableWth.select("tr[valign=top]");

        for (Element name : names) {
            String dateString = name.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date + " Явления Температура Давление Влажность Ветер");
            System.out.println();
            int iterationCount = printPartValues(values, index);
            index = index + iterationCount;
        }


    }
}
