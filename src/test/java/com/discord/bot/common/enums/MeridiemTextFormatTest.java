package com.discord.bot.common.enums;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Stream;

@Slf4j
class MeridiemTextFormatTest {

    @DisplayName("MeridiemTextFormat 테스트")
    @ParameterizedTest
    @MethodSource("meridiemTextFormatValues")
    void meridiemTextFormatValuesTest(MeridiemTextFormat meridiemTextFormat) {
        LocalDateTime now = LocalDateTime.now();
        Locale locale = meridiemTextFormat.getLocale();
        String meridiem = now.format(DateTimeFormatter.ofPattern("a", locale));

        log.debug("MeridiemTextFormat: {}", meridiemTextFormat.name());
        log.debug("Locale: {}", locale);
        log.debug("Formatted meridiem: {}", meridiem);

        Assertions.assertTrue(
                meridiemTextFormat.getDocument().contains(meridiem),
                () -> "문자열 '%s'은 document '%s' 내에 포함되어야 합니다.".formatted(
                        meridiem, meridiemTextFormat.getDocument()
                )
        );
    }

    private static Stream<Arguments> meridiemTextFormatValues() {
        return Stream.of(MeridiemTextFormat.values())
                .map(Arguments::of);
    }
}
