package grabber;

import grabber.utils.DateTimeParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Connection;

public class HabrCareerParse implements Parse {
    private static final String SOURCE_LINK = "https://career.habr.com";
    public static final String PREFIX = "/vacancies?page=";
    public static final String SUFFIX = "&q=Java%20developer&type=all";
    public static final int PAGES = 5;

    public final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    private String retrieveDescription(String link) throws IOException {
        Connection connection = Jsoup.connect(link);
        Document retrieve = connection.get();
        StringBuilder vacancy = new StringBuilder();
        Elements rows = retrieve.select(".faded-content__container");
        rows.forEach(row -> {

            Element text = row.select(".vacancy-description__text").first();
            for (int i = 0; i < 6; i++) {
                Element text1 = text.child(i);
                vacancy.append(text1.text()).append("\n");

            }
        }
        );
        return vacancy.toString();
    }

    public static void main(String[] args) throws IOException {
        HabrCareerParse hh = new HabrCareerParse();
        List<Document> documentList = new ArrayList<>(PAGES);
        for (int pageNumber = 1; pageNumber <= PAGES; pageNumber++) {
            String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, pageNumber, SUFFIX);
            Connection connection = Jsoup.connect(fullLink);
            documentList.add(connection.get());
        }
        for (Document document : documentList) {
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                Element dateElement = row.select(".vacancy-card__date").first().child(0);
                String vacancyName = titleElement.text();
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                String date = dateElement.attr("datetime");
                String text = null;
                try {
                    text = hh.retrieveDescription(link);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.printf("%s %s %s%n \n %s", date, vacancyName, link, text);

            });
        }
    }

    @Override
    public List<Post> list(String link) throws IOException {
        List<Post> posts = new ArrayList<>();
        List<Document> documentList = new ArrayList<>(PAGES);
        final int id = 0;
        for (int pageNumber = 1; pageNumber <= PAGES; pageNumber++) {
            String fullLink = "%s%s%d%s".formatted(link, PREFIX, pageNumber, SUFFIX);
            Connection connection = Jsoup.connect(fullLink);
            documentList.add(connection.get());
        }
        for (Document document : documentList) {
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                Element dateElement = row.select(".vacancy-card__date").first().child(0);
                String vacancyName = titleElement.text();
                String link1 = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                String date = dateElement.attr("datetime");
                String text = null;
                try {
                    text = new HabrCareerParse(dateTimeParser).retrieveDescription(link1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                posts.add(new Post(id, vacancyName, link1, text, new HabrCareerParse(dateTimeParser).getDateTimeParser().parse(date)));

    });
}
        return posts;
    }

    public DateTimeParser getDateTimeParser() {
        return dateTimeParser;
    }
}
