package scra.qnaboard.web.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalDateTimeUtils {

    public static final String STRING_DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm a";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(STRING_DATE_TIME_FORMAT);

    public static DateTimeFormatter localeFormatter(Locale locale) {
        return DateTimeFormatter.ofPattern(STRING_DATE_TIME_FORMAT, locale);
    }
}
