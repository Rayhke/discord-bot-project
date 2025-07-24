package com.discord.bot.common.util;

import com.discord.bot.common.enums.MeridiemTextFormat;
import com.discord.bot.common.enums.TimeHourFormat;
import com.discord.bot.common.properties.TemporalProperties;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * {@code TemporalUtil}은 애플리케이션 전역에서 사용할 날짜 및 시간 포맷을 관리하는 유틸리티 클래스입니다.
 *
 * <p>
 * 사용자 설정에 따라 포맷 문자열을 등록하고, {@link java.time.format.DateTimeFormatter}를 생성 및 저장하여 <br>
 * 현재 시각을 포맷된 문자열로 출력하는 기능을 제공합니다.
 * </p>
 *
 * <p>
 * {@link TemporalProperties} 클래스에서 초기화 시 호출되며,
 * 사용자 정의 날짜 패턴, 시간 표현 방식(12/24시간), Meridiem(오전/오후) 로케일 설정에 따라 포맷터를 구성합니다.
 * </p>
 *
 * <p><b>기능 요약:</b></p>
 * <ul>
 *     <li>날짜 패턴 등록: {@link #addPatterns(String)}</li>
 *     <li>현재 시각 출력: {@link #now()}, {@link #nowWithSeconds()}</li>
 *     <li>포맷 갱신: {@link #update(String, TimeHourFormat, MeridiemTextFormat)}</li>
 * </ul>
 *
 * <p>이 클래스는 인스턴스를 생성할 수 없습니다.</p>
 *
 * @author Rayhke
 * @version 250710
 * @since 250705
 */
@Slf4j
public class TemporalUtil {

    /**
     * 등록된 날짜 패턴 목록
     */
    private static final Set<String> DATE_PATTERNS = new HashSet<>();

    /**
     * 분 단위까지 포맷하는 DateTimeFormatter
     */
    private static DateTimeFormatter minuteFormatter;

    /**
     * 초 단위까지 포함하는 DateTimeFormatter
     */
    private static DateTimeFormatter secondFormatter;

    /**
     * 유틸리티 클래스이므로 인스턴스 생성을 방지합니다. <br>
     * 호출 시, {@link IllegalStateException}을 발생시킵니다.
     */
    private TemporalUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 새로운 날짜 패턴을 등록합니다.
     * <p>중복된 패턴은 무시됩니다.</p>
     *
     * @param datePattern 등록할 날짜 패턴 문자열 (예: {@code yyyy-MM-dd})
     */
    public static void addPatterns(String datePattern) {
        DATE_PATTERNS.add(datePattern);
    }

    /**
     * 현재까지 등록된 모든 날짜 패턴을 반환합니다.
     *
     * @return 등록된 날짜 패턴 문자열의 집합
     */
    public static Set<String> getPatterns() {
        return DATE_PATTERNS;
    }

    /**
     * 지정된 날짜 패턴, 시간 형식, Meridiem 설정을 기반으로 <br>
     * 내부 {@link DateTimeFormatter} 인스턴스를 초기화합니다.
     *
     * @param datePattern        날짜 패턴 (예: {@code yyyy-MM-dd})
     * @param timeHourFormat     시간 형식 (예: 12시간/24시간 구분)
     * @param meridiemTextFormat 오전/오후 출력 형식을 결정하는 로케일 정보
     */
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

    /**
     * 현재 시각을 분 단위까지만 포함하여 문자열로 반환합니다.
     * <p>예시: {@code 2025-07-10 오후 02:20}</p>
     *
     * @return 포맷된 현재 시각 문자열 (초 제외)
     */
    public static String now() {
        return LocalDateTime.now().format(minuteFormatter);
    }

    /**
     * 현재 시각을 초 단위까지 포함하여 문자열로 반환합니다.
     * <p>예시: {@code 2025-07-10 오후 02:20:30}</p>
     *
     * @return 포맷된 현재 시각 문자열 (초 포함)
     */
    public static String nowWithSeconds() {
        return LocalDateTime.now().format(secondFormatter);
    }
}
