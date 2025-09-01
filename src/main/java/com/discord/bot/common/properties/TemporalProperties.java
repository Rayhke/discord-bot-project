package com.discord.bot.common.properties;

import com.discord.bot.common.enums.MeridiemTextFormat;
import com.discord.bot.common.enums.TimeHourFormat;
import com.discord.bot.common.util.TemporalUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code TemporalProperties}는 애플리케이션의 시간대 및 날짜/시간 포맷 설정을 담당하는 구성 클래스입니다.
 *
 * <p>
 * {@code application.yml} 또는 {@code application.properties}에서 {@code temporal.*} 접두어로 정의된 설정 값을 읽어와,
 * 전역 시간대 및 시간 표현 방식, 날짜 패턴을 초기화합니다.
 * </p>
 *
 * <p>
 * 설정된 값들은 {@link TemporalUtil}을 통해 전역 {@link java.time.format.DateTimeFormatter}로 적용되며, <br>
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
 * @version 250712
 * @since 250705
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "temporal")
public class TemporalProperties {

    /**
     * 적용할 시간대 ID (예: Asia/Seoul)
     */
    private String timeZone = TemporalUtil.DEFAULT_TIME_ZONE;

    /**
     * 초기 날짜 패턴 문자열 (예: yyyy-MM-dd)
     */
    private String initialDatePattern = TemporalUtil.DEFAULT_DATE_PATTERN;

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
}
