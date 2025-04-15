package com.standingash.jettystick.common;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;

import static ch.qos.logback.classic.Level.*;

public class JettyStickColorConverter extends ForegroundCompositeConverterBase<ILoggingEvent> {

    @Override
    protected String getForegroundColorCode(ILoggingEvent iLoggingEvent) {
        return switch (iLoggingEvent.getLevel().toInt()) {
            case ERROR_INT -> "31;1";   // red + bold
            case WARN_INT -> "33;1";    // yellow + bold
            case INFO_INT -> "32;1";    // green + bold
            case DEBUG_INT -> "34;1";   // blue + bold
            default -> "37";            // white
        };
    }
}
