package com.example.enums;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public enum BitMask {

    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    UNKNOWN(-1);

    /**
     * 원본 숫자
     */
    private final int offset;

    /**
     * 원본 숫자를 비트 위치로 대입
     */
    private final int rawValue;

    BitMask(int offset) {
        this.offset = offset;
        this.rawValue = 1 << offset;
    }

    public static int getRaw(@Nonnull Collection<BitMask> set) {
        int raw = 0;
        log.debug("=================");
        for (BitMask intent : set) {
            log.debug("offset: {} ({})", intent.offset, intent);
            log.debug("rawValue: {}", intent.rawValue);
            log.debug("=================");
            raw |= intent.rawValue;
        }
        return raw;
    }

    public static BitMask valueOf(int offset) {
        for (BitMask bitMask : BitMask.values()) {
            if (bitMask.offset != offset) continue;
            return bitMask;
        }
        return UNKNOWN;
    }

    public static Set<BitMask> getBitMasks(int raw) {
        Set<BitMask> bitMasks = new HashSet<>();
        for (int n = 0; n < Integer.SIZE; n++) {
            if ((raw & (1 << n)) < 1) continue;
            bitMasks.add(BitMask.valueOf(n));
        }
        return bitMasks;
    }
}
