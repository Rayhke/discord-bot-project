package com.discord.bot.common.util;

import com.discord.bot.common.enums.MeridiemTextFormat;
import com.discord.bot.common.enums.TimeHourFormat;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

/**
 * {@code TemporalUtil}은 애플리케이션 전역에서 일관된 날짜 및 시간 형식을 처리하기 위한 유틸리티 클래스입니다.
 *
 * <p>
 * 사용자 설정에 따라 시간대(TimeZone)를 설정하고, 다양한 날짜 및 시간 포맷을 구성합니다. <br>
 * 내부적으로 {@link java.time.format.DateTimeFormatter}를 구성 및 저장하며, <br>
 * 현재 시각을 포맷된 문자열로 반환하는 기능도 제공합니다.
 * </p>
 *
 * <p><b>주요 기능:</b></p>
 * <ul>
 *     <li>기본 또는 사용자 정의 시간대 설정</li>
 *     <li>날짜 패턴 유효성 검사 및 등록</li>
 *     <li>12/24시간제, Meridiem 설정을 반영한 DateTimeFormatter 초기화</li>
 *     <li>초 또는 분 단위의 현재 시각 문자열 반환</li>
 * </ul>
 *
 * <p><b>사용 예시:</b></p>
 * <pre>{@code
 * TemporalUtil.updateTimeZone("Asia/Seoul");
 * TemporalUtil.updateDateTimeFormatter(
 *         "yyyy-MM-dd",
 *         TimeHourFormat.HOUR_12,
 *         MeridiemTextFormat.KR
 * );
 * String now = TemporalUtil.now(); // 2025-07-24 오전 10:30
 * }</pre>
 *
 * <p>이 클래스는 정적 유틸리티 클래스로, 인스턴스를 생성할 수 없습니다.</p>
 *
 * @author Rayhke
 * @version 250712
 * @since 250705
 */
@Slf4j
public class TemporalUtil {

    /**
     * 유효하지 않은 시간대 설정 시, 사용되는 기본 시간대
     */
    public static final String DEFAULT_TIME_ZONE = "Asia/Seoul";

    /**
     * 유효하지 않은 날짜 패턴 설정 시, 사용되는 기본 날짜 패턴
     */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 등록된 날짜 패턴 목록
     */
    private static final Set<String> DATE_PATTERNS = new HashSet<>(List.of(DEFAULT_DATE_PATTERN));

    /**
     * 분 단위까지 포맷하는 DateTimeFormatter
     */
    private static DateTimeFormatter minuteFormatter = minuteFormatter();

    /**
     * 초 단위까지 포함하는 DateTimeFormatter
     */
    private static DateTimeFormatter secondFormatter = secondFormatter();

    /**
     * 유틸리티 클래스이므로 인스턴스 생성을 방지합니다. <br>
     * 호출 시, {@link IllegalStateException}을 발생시킵니다.
     */
    private TemporalUtil() {
        throw new IllegalStateException("Utility class");
    }

    // =================================================================================================================

    /**
     * 시스템 전역 시간대를 설정합니다.
     * <p>지정한 {@code timeZone}이 유효하지 않으면 기본값({@code Asia/Seoul})을 사용합니다.</p>
     *
     * @param timeZone 적용할 시간대 ID (예: {@code Asia/Seoul}, {@code UTC} 등)
     */
    public static void updateTimeZone(String timeZone) {
        ZoneId zoneId = zoneId(timeZone);
        TimeZone.setDefault(
                TimeZone.getTimeZone(zoneId)
        );
    }

    /**
     * 주어진 문자열이 유효한 시간대 ID인지 확인하고, 적절한 {@link ZoneId}를 반환합니다.
     * <p>유효하지 않은 경우 기본 시간대({@link #DEFAULT_TIME_ZONE})를 반환합니다.</p>
     *
     * @param timeZone 검증할 시간대 ID
     * @return 유효한 시간대 객체
     */
    public static ZoneId zoneId(String timeZone) {
        Set<String> validZoneIds = ZoneId.getAvailableZoneIds();
        return ZoneId.of(
                validZoneIds.contains(timeZone) ?
                        timeZone
                        : DEFAULT_TIME_ZONE
        );
    }

    // =================================================================================================================

    /**
     * 등록된 모든 날짜 패턴을 반환합니다.
     *
     * @return 현재까지 등록된 날짜 패턴 문자열들의 집합
     */
    public static Set<String> getPatterns() {
        return DATE_PATTERNS;
    }

    /**
     * 사용자 정의 날짜 패턴을 등록합니다.
     * <p>유효성 검사를 통과한 패턴만 등록되며, 중복은 무시됩니다.</p>
     *
     * @param datePattern 등록할 날짜 패턴 (예: {@code yyyy.MM.dd})
     */
    public static void addPatterns(String datePattern) {
        if (validatePattern(datePattern)) {
            DATE_PATTERNS.add(datePattern);
        }
    }

    /**
     * 주어진 날짜 패턴 문자열이 유효한 {@link DateTimeFormatter} 형식인지 검사합니다.
     *
     * @param datePattern 검사할 날짜 패턴
     * @return 유효하면 {@code true}, 그렇지 않으면 {@code false}
     */
    public static boolean validatePattern(String datePattern) {
        try {
            DateTimeFormatter.ofPattern(datePattern);
            return true;
        } catch (Exception e) {
            log.warn("잘못된 pattern('{}') 양식입니다: {}", datePattern, e.getMessage());
            return false;
        }
    }

    // =================================================================================================================

    /**
     * 주어진 날짜 패턴과 시간 형식 및 Meridiem 설정을 바탕으로 <br>
     * 내부 {@link DateTimeFormatter} 인스턴스를 재구성합니다.
     *
     * @param datePattern        날짜 패턴 (예: {@code yyyy-MM-dd})
     * @param timeHourFormat     12/24시간제 설정
     * @param meridiemTextFormat Meridiem 형식 (예: {@code AM/PM}, {@code 오전/오후})
     */
    public static void updateDateTimeFormatter(
            String datePattern,
            TimeHourFormat timeHourFormat,
            MeridiemTextFormat meridiemTextFormat
    ) {
        Locale locale = meridiemTextFormat.getLocale();
        minuteFormatter = minuteFormatter(datePattern, timeHourFormat, locale);
        secondFormatter = secondFormatter(datePattern, timeHourFormat, locale);
    }

    /**
     * 기본 설정(KR, 12시간제, 기본 날짜 포맷)을 기반으로 분 단위 포맷터를 생성합니다.
     *
     * @return {@link DateTimeFormatter} 인스턴스
     */
    public static DateTimeFormatter minuteFormatter() {
        return minuteFormatter(
                DEFAULT_DATE_PATTERN,
                TimeHourFormat.HOUR_12,
                Locale.KOREA
        );
    }

    /**
     * 지정된 설정을 기반으로 분 단위 포맷터를 생성합니다.
     *
     * @param pattern        날짜 포맷
     * @param timeHourFormat 시간 형식
     * @param locale         로케일
     * @return {@link DateTimeFormatter} 인스턴스
     */
    public static DateTimeFormatter minuteFormatter(
            String pattern, TimeHourFormat timeHourFormat, Locale locale
    ) {
        return DateTimeFormatter.ofPattern(
                "%s %s".formatted(pattern, timeHourFormat.getPattern()),
                locale
        );
    }

    /**
     * 기본 설정(KR, 12시간제, 기본 날짜 포맷)을 기반으로 분 단위 포맷터를 생성합니다.
     *
     * @return {@link DateTimeFormatter} 인스턴스
     */
    public static DateTimeFormatter secondFormatter() {
        return secondFormatter(
                DEFAULT_DATE_PATTERN,
                TimeHourFormat.HOUR_12,
                Locale.KOREA
        );
    }

    /**
     * 지정된 설정을 기반으로 분 단위 포맷터를 생성합니다.
     *
     * @param pattern        날짜 포맷
     * @param timeHourFormat 시간 형식
     * @param locale         로케일
     * @return {@link DateTimeFormatter} 인스턴스
     */
    public static DateTimeFormatter secondFormatter(
            String pattern, TimeHourFormat timeHourFormat, Locale locale
    ) {
        return DateTimeFormatter.ofPattern(
                "%s %s:ss".formatted(pattern, timeHourFormat.getPattern()),
                locale
        );
    }

    /**
     * 현재 시각을 분 단위까지만 포함하여 문자열로 반환합니다.
     * <p>예시 출력: {@code 2025-07-12 오전 10:35}</p>
     *
     * @return 현재 시각 문자열 (초 제외)
     */
    public static String now() {
        return LocalDateTime.now().format(minuteFormatter);
    }

    /**
     * 현재 시각을 초 단위까지 포함하여 문자열로 반환합니다.
     * <p>예시 출력: {@code 2025-07-12 오전 10:35:12}</p>
     *
     * @return 현재 시각 문자열 (초 포함)
     */
    public static String nowWithSeconds() {
        return LocalDateTime.now().format(secondFormatter);
    }
}
