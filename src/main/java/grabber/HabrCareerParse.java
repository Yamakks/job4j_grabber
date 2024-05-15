package grabber;

import grabber.utils.DateTimeParser;
import grabber.utils.HabrCareerDateTimeParser;
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

    private final DateTimeParser dateTimeParser;

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
        HabrCareerParse hh = new HabrCareerParse(new HabrCareerDateTimeParser());
        List<Post> vacancies = new ArrayList<>(hh.list(SOURCE_LINK));
        vacancies.forEach(System.out::println);

    }

    @Override
    public List<Post> list(String link) throws IOException {
        List<Post> posts = new ArrayList<>();
        List<Document> documentList = new ArrayList<>(PAGES);
        final int id = 0;
        for (int pageNumber = 1; pageNumber <= PAGES; pageNumber++) {
            Connection connection = Jsoup.connect(link + PREFIX + pageNumber + SUFFIX);
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
                posts.add(new Post(id, vacancyName, link1, text, dateTimeParser.parse(date)));

    });
}
        return posts;
    }

    public DateTimeParser getDateTimeParser() {
        return dateTimeParser;
    }
}
