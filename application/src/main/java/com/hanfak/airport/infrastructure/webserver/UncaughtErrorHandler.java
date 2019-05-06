package com.hanfak.airport.infrastructure.webserver;

import org.eclipse.jetty.server.handler.ErrorHandler;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;

import static java.lang.String.format;

public class UncaughtErrorHandler extends ErrorHandler {
    private final Logger logger;

    public UncaughtErrorHandler(Logger logger) {
        this.logger = logger;
    }

    @Override
    protected void writeErrorPage(HttpServletRequest request, Writer writer, int code, String message, boolean showStacks) throws IOException {
        Throwable uncaught = (Throwable) request.getAttribute("javax.servlet.error.exception");
        if (uncaught == null) {
            logger.error("Fatal error. Uncaught exception was not found.");
        } else {
            logger.error(format("Uncaught exception: '%s'", uncaught.getMessage()), uncaught);
        }
        writer.append("Technical Failure. Please contact the system administrator.");
    }
}
