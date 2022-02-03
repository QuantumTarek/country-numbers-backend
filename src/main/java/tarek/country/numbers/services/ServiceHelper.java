package tarek.country.numbers.services;

import org.springframework.stereotype.Component;
import tarek.country.numbers.entities.enums.Country;
import tarek.country.numbers.entities.enums.State;
import tarek.country.numbers.exceptions.GeneralException;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServiceHelper {
    public static final String DELIMITER = ")";
    public static final int INDEX_OF_FIRST_CHARACTER = 1;

    public String getPhoneNumberCountryCode(String phoneNumber) {
        return phoneNumber.substring(INDEX_OF_FIRST_CHARACTER, phoneNumber.indexOf(DELIMITER));
    }

    public List<Country> toCountryList(List<String> list) {
        try {
            return list.stream().map(Country::valueOf).collect(Collectors.toList());
        } catch (IllegalArgumentException exception) {
            throw new GeneralException("please check country parameter");
        }
    }

    public List<State> toStateList(List<String> list) {
        try {
            return list.stream().map(State::valueOf).collect(Collectors.toList());
        } catch (IllegalArgumentException exception) {
            throw new GeneralException("please check state parameter");
        }
    }
}
