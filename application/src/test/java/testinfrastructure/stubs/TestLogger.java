package testinfrastructure.stubs;

import org.slf4j.Logger;
import org.slf4j.Marker;

import java.util.ArrayList;
import java.util.List;

public class TestLogger implements Logger {

    private final List<String> infoLogs = new ArrayList<>();
    private final List<String> debugLogs = new ArrayList<>();
    private final List<String> warnLogs = new ArrayList<>();
    private final List<String> errorLogs = new ArrayList<>();
    private final List<Throwable> errorCauses = new ArrayList<>();
    private final List<Throwable> warnCauses = new ArrayList<>();

    @Override
    public void info(String message) {
        infoLogs.add(message);
    }

    @Override
    public void warn(String message) {
        warnLogs.add(message);
    }

    @Override
    public void warn(String message, Throwable throwable) {
        warnLogs.add(message);
        warnCauses.add(throwable);
    }

    @Override
    public void error(String message) {
        errorLogs.add(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        errorLogs.add(message);
        errorCauses.add(throwable);
    }

    @Override
    public void debug(String message) {
        debugLogs.add(message);
    }


    @Override
    public void info(String s, Object o) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void info(String s, Object o, Object o1) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void info(String s, Object... objects) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void info(String s, Throwable throwable) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void info(Marker marker, String s) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void info(Marker marker, String s, Object o) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void info(Marker marker, String s, Object o, Object o1) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void info(Marker marker, String s, Object... objects) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void info(Marker marker, String s, Throwable throwable) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public boolean isWarnEnabled() {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void warn(String s, Object o) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void warn(String s, Object... objects) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void warn(String s, Object o, Object o1) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void warn(Marker marker, String s) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void warn(Marker marker, String s, Object o) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void warn(Marker marker, String s, Object o, Object o1) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void warn(Marker marker, String s, Object... objects) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void warn(Marker marker, String s, Throwable throwable) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public boolean isErrorEnabled() {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void error(String s, Object o) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void error(String s, Object o, Object o1) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void error(String s, Object... objects) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void error(Marker marker, String s) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void error(Marker marker, String s, Object o) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void error(Marker marker, String s, Object o, Object o1) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void error(Marker marker, String s, Object... objects) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void error(Marker marker, String s, Throwable throwable) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public boolean isTraceEnabled() {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void trace(String s) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void trace(String s, Object o) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void trace(String s, Object o, Object o1) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void trace(String s, Object... objects) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void trace(String s, Throwable throwable) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void trace(Marker marker, String s) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void trace(Marker marker, String s, Object o) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void trace(Marker marker, String s, Object o, Object o1) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void trace(Marker marker, String s, Object... objects) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void trace(Marker marker, String s, Throwable throwable) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public boolean isDebugEnabled() {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void debug(String s, Object o) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void debug(String s, Object o, Object o1) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void debug(String s, Object... objects) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void debug(String s, Throwable throwable) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void debug(Marker marker, String s) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void debug(Marker marker, String s, Object o) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void debug(Marker marker, String s, Object o, Object o1) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void debug(Marker marker, String s, Object... objects) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void debug(Marker marker, String s, Throwable throwable) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public boolean isInfoEnabled() {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    public List<String> infoLogs() {
        return infoLogs;
    }

    public List<String> debugLogs() {
        return debugLogs;
    }

    public List<String> warnLogs() {
        return warnLogs;
    }

    public List<String> errorLogs() {
        return errorLogs;
    }

    public List<Throwable> errorCauses() {
        return errorCauses;
    }

    public List<Throwable> warnCauses() {
        return warnCauses;
    }
}