package com.example.enums;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

@Slf4j
class BitMaskTest {

    @Test
    void test() {
        EnumSet<BitMask> examples = EnumSet.of(
                BitMask.ZERO,
                BitMask.NINE,
                BitMask.ONE,
                BitMask.FOUR
        );

        BitMask.result(
                BitMask.getRaw(examples)
        );
    }
}
