package learning.webserver.example;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class EndPoint {
    final String method;
    public final String path;
    final List<String> parameterNames;

    private EndPoint(String method, String path, List<String> parameterNames) {
        this.method = method;
        this.path = path;
        this.parameterNames = parameterNames;
    }

    public static EndPoint get(String path, String... parameterNames) {
        return new EndPoint("GET", path, asList(parameterNames));
    }

    public static EndPoint post(String path) {
        return new EndPoint("POST", path, emptyList());
    }
}
