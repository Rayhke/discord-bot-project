package com.discord.bot.common.enums;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Stream;

@Slf4j
class MeridiemTextFormatTest {

    @DisplayName("MeridiemTextFormat 테스트")
    @ParameterizedTest
    @MethodSource("meridiemTextFormatValues")
    void meridiemTextFormatValuesTest(String name, Locale expected) {
        LocalDateTime now = LocalDateTime.now();

        Locale actual = (Locale) ReflectionTestUtils.getField(Locale.class, name);
        Assertions.assertNotNull(actual);

        log.debug("Locale name: {}", name);
        log.debug("Locale value: {}", expected);
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(
                now.format(DateTimeFormatter.ofPattern("a", expected)),
                now.format(DateTimeFormatter.ofPattern("a", actual))
        );
    }

    private static Stream<Arguments> meridiemTextFormatValues() {
        return Stream.of(MeridiemTextFormat.values())
                .map(value ->
                        Arguments.of(
                                value.name(),
                                value.getLocale()
                        )
                );
    }
}
