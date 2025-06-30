package com.discord.bot.common.enums;

import lombok.Getter;

import java.util.Locale;

/**
 * {@code MeridiemTextFormat}은 시간 포맷의 오전/오후(Meridiem) 표현 형식을 정의하는 열거형입니다.
 * <p>
 * {@link java.time.format.DateTimeFormatter}에서 패턴 문자열 내 {@code a} (AM/PM) 항목을 사용할 때 <br>
 * Locale 설정에 따라 출력되는 텍스트가 달라지며, 이 열거형은 그런 Locale 기반의 표현 형식을 지정합니다.
 *
 * @author nink2458
 * @version 250630
 * @since 250630
 */
@Getter
public enum MeridiemTextFormat {

    /**
     * 미국식 오전/오후 표현.
     * <p>
     * 예시 출력: {@code AM / PM}
     */
    US(Locale.US, "AM/PM"),

    /**
     * 한국식 오전/오후 표현.
     * <p>
     * 예시 출력: {@code 오전 / 오후}
     */
    KOREA(Locale.KOREA, "오전/오후");

    /**
     * {@link java.time.format.DateTimeFormatter}에 Meridiem 표현을 지정하기 위한 Locale 객체
     */
    private final Locale locale;

    /**
     * 사용자에게 표시할 Meridiem 문자열 형식
     */
    private final String document;

    MeridiemTextFormat(Locale locale, String document) {
        this.locale = locale;
        this.document = document;
    }
}
