package com.discord.bot.common.enums;

import lombok.Getter;

/**
 * {@code TimeHourFormat}은 시간 표현 방식(12시간 또는 24시간)을 정의하는 열거형입니다.
 * <p>
 * {@link java.time.format.DateTimeFormatter}에서 시간 관련 패턴을 구성할 때 사용됩니다.
 * <ul>
 *     <li>{@code HH:mm} → 24시간 형식 (예: 14:30)</li>
 *     <li>{@code a hh:mm} → 오전/오후를 포함한 12시간 형식 (예: 오후 02:30)</li>
 * </ul>
 * 이 enum은 사용자 정의 시간 포맷 설정 시 패턴을 선택적으로 조립하는 데 활용됩니다.
 *
 * @author nink2458
 * @version 250630
 * @since 250630
 */
@Getter
public enum TimeHourFormat {

    /**
     * 24시간 형식
     * <p>
     * 예시 출력: {@code 14:30}
     */
    HOUR_24("HH:mm"),

    /**
     * 12시간 형식
     * <p>
     * 예시 출력: {@code 오후 02:30}
     */
    HOUR_12("a hh:mm");

    /**
     * {@link java.time.format.DateTimeFormatter}에서 사용되는 시간 포맷 패턴 문자열
     */
    private final String pattern;

    TimeHourFormat(String pattern) {
        this.pattern = pattern;
    }
}
