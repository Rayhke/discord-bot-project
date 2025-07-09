package com.discord.bot.common.enums;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Slf4j
class TimeHourFormatTest {

    @DisplayName("TimeHourFormat 테스트")
    @ParameterizedTest
    @MethodSource("timeHourFormatValues")
    void timeHourFormatValuesTest(String pattern, int length) {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter dateTimeFormatter = Assertions.assertDoesNotThrow(
                () -> DateTimeFormatter.ofPattern(pattern),
                "유효하지 않은 패턴: %s".formatted(pattern)
        );

        log.debug("Time hour: {}", now.format(dateTimeFormatter));
        Assertions.assertEquals(length, now.format(dateTimeFormatter).length());
    }

    private static Stream<Arguments> timeHourFormatValues() {
        return Stream.of(TimeHourFormat.values())
                .map(value ->
                        Arguments.of(
                                value.getPattern(),
                                value.getPattern().length() == 5 ? 5 : 8
                        )
                );
    }
}
