package testinfrastructure.yatspec;

import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;
import com.mashape.unirest.http.Headers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class YatspecFormatters {

    public static String toYatspecString(Request wireMockRequest) {
        return String.format("%s %s HTTP/1.1%s", wireMockRequest.getMethod().getName(), wireMockRequest.getUrl(), "\n")
                + adaptHeaders(wireMockRequest.getHeaders())
                + "\r\n\r\n"
                + adaptBody(wireMockRequest.getBodyAsString());
    }

    public static String toYatspecString(Response wiremockResponse) {
        return String.format("%s %s%n%s%n%n%s", "HTTP/1.1", wiremockResponse.getStatus(),
                fixWiremockListenerProblem(adaptHeaders(wiremockResponse.getHeaders())),
                adaptBody(wiremockResponse.getBodyAsString()));
    }

    public static String requestOutput(String uri, Map<String, String> headers, String body) {
        return format("%s %s HTTP/1.1%s", "POST", uri, "\n") + headersFormatter(headers) + "\r\n\r\n" + body;
    }

    public static String responseOutput(int responseStatus, Headers responseHeaders, String responseBody) {
        return format("%s %s%n%s%n%n%s", "HTTP", responseStatus, headersFormatter(responseHeaders), responseBody);
    }

    private static String headersFormatter(Map<String, String> headers) {
        return headers.entrySet().stream()
                .map(s -> format("%s: %s", s.getKey(), s.getValue()))
                .collect(joining(lineSeparator()));
    }

    private static String headersFormatter(Headers headers) {
        return headers.entrySet().stream()
                .map(s -> format("%s: %s", s.getKey(), s.getValue()))
                .collect(joining(lineSeparator()));
    }

    private static String adaptBody(String wiremockBody) {
        if (wiremockBody == null) {
            return "";
        } else {
            return wiremockBody;
        }
    }

    private static Iterable<Header> fixWiremockListenerProblem(List<Header> headers) {
        return headers.stream().map(YatspecFormatters::appendGzipToEtag).collect(Collectors.toList());
    }

    private static Header appendGzipToEtag(Header header) {
        return header.key.toLowerCase().equals("etag") ? new Header(header.key, header.value + "--gzip")  : header;
    }

    private static List<Header> adaptHeaders(HttpHeaders headers) {
        return headers.all().stream()
                .map(httpHeader -> new Header(httpHeader.key(), httpHeader.values()))
                .collect(Collectors.toList());
    }
}
