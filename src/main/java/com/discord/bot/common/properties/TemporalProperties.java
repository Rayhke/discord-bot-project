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

@Slf4j
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "temporal")
public class TemporalProperties {

    private static final String DEFAULT_TIME_ZONE = "Asia/Seoul";

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    private String timeZone = DEFAULT_TIME_ZONE;

    private String initialDateFormat = DEFAULT_DATE_FORMAT;

    private MeridiemTextFormat meridiemText = MeridiemTextFormat.KOREA;

    private List<String> datePatterns = new ArrayList<>();

    @PostConstruct
    private void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(zoneId()));
        TemporalUtil.update(
                initialDateFormat,
                TimeHourFormat.HOUR_12,
                MeridiemTextFormat.KOREA
        );
        for (String datePattern : datePatterns) {
            try {
                DateTimeFormatter.ofPattern(datePattern);
                TemporalUtil.add(datePattern);
            } catch (Exception e) {
                log.warn("잘못된 format('{}') 양식입니다: {}", datePattern, e.getMessage());
            }
        }
    }

    private ZoneId zoneId() {
        Set<String> validZoneIds = ZoneId.getAvailableZoneIds();
        String timeZone =
                validZoneIds.contains(this.timeZone) ?
                        this.timeZone
                        : DEFAULT_TIME_ZONE;
        return ZoneId.of(timeZone);
    }
}
