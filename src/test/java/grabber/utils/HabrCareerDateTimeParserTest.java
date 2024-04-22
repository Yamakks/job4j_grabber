package grabber.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class HabrCareerDateTimeParserTest {
    @Test
    void parseValidStringReturnsDate() {
        String dateString = "2024-04-21T13:45:30";
        HabrCareerDateTimeParser parser = new HabrCareerDateTimeParser();
        LocalDateTime result = parser.parse(dateString);
        assertNotNull(result);
        assertEquals(2024, result.getYear());
        assertEquals(4, result.getMonthValue());
        assertEquals(21, result.getDayOfMonth());
        assertEquals(13, result.getHour());
        assertEquals(45, result.getMinute());
        assertEquals(30, result.getSecond());

    }

    @Test
    void parseInvalidStringReturnsDateTimeParseException() {
        String dateString = "20240-04-21T13:45:30";
        HabrCareerDateTimeParser parser = new HabrCareerDateTimeParser();
        assertThrows(DateTimeParseException.class, () -> parser.parse(dateString));

    }
}