package testinfrastructure.yatspec;

import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;

import java.util.List;
import java.util.stream.Collectors;

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
