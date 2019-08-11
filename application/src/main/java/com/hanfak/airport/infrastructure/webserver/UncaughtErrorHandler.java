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

/**

 All exceptions thrown with the app, that are not dealt with are caught here.

 We send back a standard message, Technical Failure..., and they will contact someone
 to investigate.

 The team investigating this message, will have the stack trace in the logs which is caught here.

 This prevents any sort of sensitive data or how the app works from getting to
 malicious users.

 Any exception that is caught else where will be treated differently and a better message
 will be returned
*/
