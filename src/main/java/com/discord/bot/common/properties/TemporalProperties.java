package com.discord.bot.common.properties;

import com.discord.bot.common.enums.MeridiemTextFormat;
import com.discord.bot.common.enums.TimeHourFormat;
import com.discord.bot.common.util.TemporalUtil;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

/**
 * {@code TemporalProperties}는 애플리케이션의 시간대 및 날짜/시간 포맷 설정을 담당하는 구성 클래스입니다.
 *
 * <p>
 * {@code application.yml} 또는 {@code application.properties}에서 {@code temporal.*} 접두어로 정의된 설정 값을 읽어와,
 * 전역 시간대 및 시간 표현 방식, 날짜 패턴을 초기화합니다.
 * </p>
 *
 * <p>
 * 설정된 값들은 {@link TemporalUtil}을 통해 전역 {@link java.time.format.DateTimeFormatter}로 적용되며,
 * 애플리케이션 전체에서 일관된 시간 출력 형식을 제공합니다.
 * </p>
 *
 * <p><b>예시 설정 (application.yml):</b></p>
 * <pre>{@code
 * temporal:
 *   time-zone: Asia/Seoul
 *   initial-date-pattern: yyyy-MM-dd
 *   date-patterns:
 *     - yyyy/MM/dd
 *     - MM-dd
 *
 *   time-hour-format: HOUR_12
 *   meridiem-text-format: KR
 * }</pre>
 *
 * @author Rayhke
 * @version 250710
 * @since 250705
 */
@Slf4j
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "temporal")
public class TemporalProperties {

    /**
     * 유효하지 않은 시간대 설정 시, 사용되는 기본 시간대
     */
    private static final String DEFAULT_TIME_ZONE = "Asia/Seoul";

    /**
     * 유효하지 않은 날짜 패턴 설정 시, 사용되는 기본 날짜 패턴
     */
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 적용할 시간대 ID (예: Asia/Seoul)
     */
    private String timeZone = DEFAULT_TIME_ZONE;

    /**
     * 초기 날짜 패턴 문자열 (예: yyyy-MM-dd)
     */
    private String initialDatePattern = DEFAULT_DATE_PATTERN;

    /**
     * 12시간 또는 24시간 형식 설정 (기본값: 12시간 형식)
     */
    private TimeHourFormat timeHourFormat = TimeHourFormat.HOUR_12;

    /**
     * 오전/오후(Meridiem) 표현 방식 (예: AM/PM 또는 오전/오후)
     */
    private MeridiemTextFormat meridiemTextFormat = MeridiemTextFormat.KR;

    /**
     * 추가로 허용할 사용자 정의 날짜 패턴 목록
     */
    private List<String> datePatterns = new ArrayList<>();

    /**
     * 애플리케이션 초기화 시점에 실행되며, <br>
     * 설정된 시간대와 날짜/시간 포맷을 기반으로 {@link TemporalUtil}을 구성합니다.
     * <p>
     * - 시간대(TimeZone)를 시스템 전역에 설정하고 <br>
     * - {@code initialDatePattern}과 {@code datePatterns}에 유효한 포맷이 있을 경우 등록하며 <br>
     * - {@link TemporalUtil#update(String, TimeHourFormat, MeridiemTextFormat)}를 통해 포맷터를 초기화합니다.
     * </p>
     */
    @PostConstruct
    private void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(zoneId()));

        boolean isInitialPatternValid = valid(initialDatePattern);
        if (isInitialPatternValid) {
            TemporalUtil.addPatterns(initialDatePattern);
        }

        TemporalUtil.update(
                isInitialPatternValid ?
                        initialDatePattern
                        : DEFAULT_DATE_PATTERN,
                timeHourFormat,
                meridiemTextFormat
        );

        for (String datePattern : datePatterns) {
            if (valid(datePattern)) {
                TemporalUtil.addPatterns(datePattern);
            }
        }
    }

    /**
     * 주어진 날짜 패턴 문자열이 유효한 {@link DateTimeFormatter} 형식인지 검사합니다.
     *
     * @param datePattern 검사할 날짜 패턴 문자열
     * @return 유효하면 {@code true}, 그렇지 않으면 {@code false}
     */
    private boolean valid(String datePattern) {
        try {
            DateTimeFormatter.ofPattern(datePattern);
            return true;
        } catch (Exception e) {
            log.warn("잘못된 pattern('{}') 양식입니다: {}", datePattern, e.getMessage());
            return false;
        }
    }

    /**
     * 설정된 시간대가 유효한지 확인한 뒤, <br>
     * 유효하지 않으면 기본 시간대({@link #DEFAULT_TIME_ZONE})를 반환합니다.
     *
     * @return 유효한 {@link ZoneId} 객체
     */
    private ZoneId zoneId() {
        Set<String> validZoneIds = ZoneId.getAvailableZoneIds();
        String timeZone =
                validZoneIds.contains(this.timeZone) ?
                        this.timeZone
                        : DEFAULT_TIME_ZONE;
        return ZoneId.of(timeZone);
    }
}
