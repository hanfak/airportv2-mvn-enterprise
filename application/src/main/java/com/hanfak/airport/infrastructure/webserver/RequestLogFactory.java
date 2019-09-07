package com.hanfak.airport.infrastructure.webserver;

import org.eclipse.jetty.server.CustomRequestLog;
import org.eclipse.jetty.server.Slf4jRequestLogWriter;

import static com.hanfak.airport.infrastructure.logging.LoggingCategory.ACCESS;

public class RequestLogFactory {

    public static CustomRequestLog createRequestLog() {
        Slf4jRequestLogWriter slf4jRequestLogWriter = new Slf4jRequestLogWriter();
        slf4jRequestLogWriter.setLoggerName(ACCESS.name());
        String requestLogFormat = CustomRequestLog.EXTENDED_NCSA_FORMAT;
        return new CustomRequestLog(slf4jRequestLogWriter, requestLogFormat);
    }
}
