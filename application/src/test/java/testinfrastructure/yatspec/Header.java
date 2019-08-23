package testinfrastructure.yatspec;

import com.hanfak.airport.domain.helper.ValueType;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class Header extends ValueType {

    final String key;
    public final String value;

    Header(String key, String value) {
        this.key = key;
        this.value = value;
    }

    Header(String key, List<String> values) {
        this.key = key;
        this.value = values.stream().collect(joining(","));
    }

    @Override
    public String toString() {
        return key + ": " + value;
    }
}
