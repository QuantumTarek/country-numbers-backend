package tarek.country.numbers.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tarek.country.numbers.entities.enums.Country;
import tarek.country.numbers.entities.enums.State;
import tarek.country.numbers.exceptions.GeneralException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ServiceHelperTest {

    private ServiceHelper serviceHelper;

    @BeforeEach
    void setUp() {
        serviceHelper = new ServiceHelper();
    }

    @Test
    void whenPhoneNumberProvided_shouldReturnPhoneNumberCountryCode() {
        String phoneNumber = "(212) 698054317";
        String expectedCountryCode = "212";
        assertEquals(serviceHelper.getPhoneNumberCountryCode(phoneNumber), expectedCountryCode);
    }

    @Test
    void whenListOfCountryStringsProvided_shouldReturnValidCountryList() {
        Country morocco = Country.MOROCCO;
        Country cameroon = Country.CAMEROON;
        Country ethiopia = Country.ETHIOPIA;
        List<String> listOfCountryStrings = List.of(morocco.name(), cameroon.name(), ethiopia.name());
        List<Country> listOfCountries = List.of(morocco, cameroon, ethiopia);
        assertEquals(serviceHelper.toCountryList(listOfCountryStrings), listOfCountries);
    }

    @Test
    void whenListOfInvalidCountryStringsProvided_shouldReturnThrowGeneralException() {
        List<String> listOfInvalidCountryStrings = List.of("egypt", "italy");
        assertThrows(GeneralException.class, () -> serviceHelper.toCountryList(listOfInvalidCountryStrings));
    }

    @Test
    void whenListOfStateStringsProvided_shouldReturnValidStateList() {
        State valid = State.VALID;
        State notValid = State.NOT_VALID;
        List<String> listOfStateStrings = List.of(valid.name(), notValid.name());
        List<State> listOfStates = List.of(valid, notValid);
        assertEquals(serviceHelper.toStateList(listOfStateStrings), listOfStates);
    }

    @Test
    void whenListOfInvalidStateStringsProvided_shouldReturnThrowGeneralException() {
        List<String> listOfInvalidStateStrings = List.of("invalid", "valid2");
        assertThrows(GeneralException.class, () -> serviceHelper.toStateList(listOfInvalidStateStrings));
    }
}