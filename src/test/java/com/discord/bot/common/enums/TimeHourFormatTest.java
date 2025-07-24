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
class TimeHourFormatTest {

    @DisplayName("TimeHourFormat 테스트")
    @ParameterizedTest
    @MethodSource("timeHourFormatValues")
    void timeHourFormatValuesTest(TimeHourFormat timeHourFormat) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = Assertions.assertDoesNotThrow(
                () -> DateTimeFormatter.ofPattern(timeHourFormat.getPattern(), Locale.KOREA),
                () -> "유효하지 않은 패턴: %s".formatted(timeHourFormat.getPattern())
        );
        String timeHour = now.format(dateTimeFormatter);

        log.debug("TimeHourFormat: {}", timeHourFormat.name());
        log.debug("Formatted Hour: {}", timeHour);

        switch (timeHourFormat) {
            case HOUR_24 -> Assertions.assertTrue(
                    timeHour.matches("\\d{2}:\\d{2}$"),
                    () -> "24시간 형식은 숫자 2자리로 시작해야 합니다. 실제: %s".formatted(timeHour)
            );
            case HOUR_12 -> Assertions.assertTrue(
                    timeHour.matches("^(오전|오후|AM|PM)\\s\\d{2}:\\d{2}$"),
                    () -> "12시간 형식은 Meridiem과 숫자 조합이어야 합니다. 실제: %s".formatted(timeHour)
            );
        }
    }

    private static Stream<Arguments> timeHourFormatValues() {
        return Stream.of(TimeHourFormat.values())
                .map(Arguments::of);
    }
}
