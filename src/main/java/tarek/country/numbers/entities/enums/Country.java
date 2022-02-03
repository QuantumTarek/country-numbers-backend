package tarek.country.numbers.entities.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public enum Country {
    CAMEROON("237", Pattern.compile("\\(237\\)\\ ?[2368]\\d{7,8}$")),
    ETHIOPIA("251", Pattern.compile("\\(251\\)\\ ?[1-59]\\d{8}$")),
    MOROCCO("212", Pattern.compile("\\(212\\)\\ ?[5-9]\\d{8}$")),
    MOZAMBIQUE("258", Pattern.compile("\\(258\\)\\ ?[28]\\d{7,8}$")),
    UGANDA("256", Pattern.compile("\\(256\\)\\ ?\\d{9}$"));

    private final String code;
    private final Pattern regex;
    private static final Map<String, Country> countriesMap = new HashMap<>();

    static {
        for (Country country : Country.values()) {
            countriesMap.put(country.code, country);
        }
    }

    private Country(String code, Pattern regex) {
        this.code = code;
        this.regex = regex;
    }

    public static Country getCountry(String code) {
        return countriesMap.get(code);
    }

    public State matches(String input) {
        return regex.matcher(input).matches() ? State.VALID : State.NOT_VALID;
    }

}
