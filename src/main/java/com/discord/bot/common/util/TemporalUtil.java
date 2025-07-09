package com.discord.bot.common.util;

import com.discord.bot.common.enums.MeridiemTextFormat;
import com.discord.bot.common.enums.TimeHourFormat;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
public class TemporalUtil {

    private static final List<String> DATE_PATTERNS = new ArrayList<>();

    private static DateTimeFormatter minuteFormatter;

    private static DateTimeFormatter secondFormatter;

    private TemporalUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static void add(String datePattern) {
        DATE_PATTERNS.add(datePattern);
    }

    public static List<String> get() {
        return DATE_PATTERNS;
    }

    public static void update(
            String datePattern,
            TimeHourFormat timeHourFormat,
            MeridiemTextFormat meridiemTextFormat
    ) {
        String timePattern = timeHourFormat.getPattern();
        Locale locale = meridiemTextFormat.getLocale();

        minuteFormatter = DateTimeFormatter.ofPattern(
                "%s %s".formatted(datePattern, timePattern),
                locale
        );

        secondFormatter = DateTimeFormatter.ofPattern(
                "%s %s:ss".formatted(datePattern, timePattern),
                locale
        );
    }

    public static String now() {
        return LocalDateTime.now().format(minuteFormatter);
    }

    public static String nowWithSeconds() {
        return LocalDateTime.now().format(secondFormatter);
    }
}
