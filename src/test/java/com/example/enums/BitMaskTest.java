package com.example.enums;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
class BitMaskTest {

    @ParameterizedTest
    @MethodSource("source")
    void test(EnumSet<BitMask> expected) {
        int raw = BitMask.getRaw(expected);
        log.debug("raw: {}", raw);

        Set<BitMask> actual = BitMask.getBitMasks(raw);
        log.debug("actual: {}", actual);

        Assertions.assertEquals(expected, actual);
    }

    private static Stream<Arguments> source() {
        return Stream.of(
                Arguments.of(
                        EnumSet.of(
                                BitMask.ZERO,
                                BitMask.NINE,
                                BitMask.ONE,
                                BitMask.FOUR
                        )
                )
        );
    }
}
