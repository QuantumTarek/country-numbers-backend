package tarek.country.numbers.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tarek.country.numbers.dto.CustomerDto;
import tarek.country.numbers.entities.enums.Country;
import tarek.country.numbers.entities.enums.State;
import tarek.country.numbers.exceptions.GeneralException;
import tarek.country.numbers.services.CustomerService;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    @MockBean
    private CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;

    @Captor
    private ArgumentCaptor<PageRequest> pageRequestArgumentCaptor;

    @Captor
    private ArgumentCaptor<Map<String, List<String>>> filterArgumentCaptor;

    private CustomerDto validCustomer;

    private Page<CustomerDto> validCustomers;

    @BeforeEach
    void setup() {
        validCustomer = new CustomerDto();
        validCustomer.setId(new Random().nextInt());
        validCustomer.setName("test 1");
        validCustomer.setPhone("(212) 698054317");
        validCustomer.setCode("212");
        validCustomer.setCountry(Country.MOROCCO);
        validCustomer.setState(State.VALID);
        validCustomers = new PageImpl<CustomerDto>(List.of(validCustomer));
    }

    @AfterEach
    void tearDown() {
        reset(customerService);
    }

    @Test
    void givenDefaultPagination_whenGetCustomers_shouldPass() throws Exception {
        // when
        mockMvc.perform(get("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // then
        then(customerService).should().listCustomers(pageRequestArgumentCaptor.capture(), any());
        PageRequest pageRequest = pageRequestArgumentCaptor.getValue();
        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(10, pageRequest.getPageSize());
        assertEquals(Sort.unsorted(), pageRequest.getSort());
    }

    @Test
    void givenCertainPagination_whenGetCustomers_shouldEvaluateValidPagination() throws Exception {
        //given
        String page = "2";
        String size = "5";
        // when
        mockMvc.perform(get("/api/v1/customers").param("page", page)
                        .param("size", size)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // then
        then(customerService).should().listCustomers(pageRequestArgumentCaptor.capture(), any());
        PageRequest pageRequest = pageRequestArgumentCaptor.getValue();
        assertEquals(2, pageRequest.getPageNumber());
        assertEquals(5, pageRequest.getPageSize());
        assertEquals(Sort.unsorted(), pageRequest.getSort());
    }

    @Test
    void givenNullPagination_whenGetCustomers_shouldFail() throws Exception {
        //given
        String page = null;
        String size = null;
        // when
        mockMvc.perform(get("/api/v1/customers").param("page", page)
                        .param("size", size)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        // then
        then(customerService).shouldHaveNoInteractions();
    }

    @Test
    void givenValidCountryFilter_whenGetCustomers_shouldReturnOnlySpecifiedCountry() throws Exception {
        // given
        String filters = "filters[country]=CAMEROON";
        // when
        mockMvc.perform(get("/api/v1/customers?" + filters)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // then
        then(customerService).should().listCustomers(any(), filterArgumentCaptor.capture());
        Map<String, List<String>> capturedFilters = filterArgumentCaptor.getValue();
        List<String> countryFilter = capturedFilters.get("country");
        assertThat(capturedFilters, hasKey("country"));
        assertThat(countryFilter, hasSize(1));
        assertThat(countryFilter.get(0), is(Country.CAMEROON.name()));
    }

    @Test
    void givenNonExistingCountryFilter_whenGetCustomers_shouldThrowGeneralException() throws Exception {
        // given
        String errorMessage = "please check country parameter";
        String filters = "filters[country]=EGYPT";
        given(customerService.listCustomers(any(), any())).willThrow(new GeneralException(errorMessage));
        // when
        mockMvc.perform(get("/api/v1/customers?" + filters)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.message", not(emptyString())))
                .andExpect(jsonPath("$.message", is(errorMessage)));
        // then
        then(customerService).should().listCustomers(any(), filterArgumentCaptor.capture());
    }

    @Test
    void givenValidStateFilter_whenGetCustomers_shouldReturnOnlySpecifiedState() throws Exception {
        // given
        String filters = "filters[state]=VALID";
        // when
        mockMvc.perform(get("/api/v1/customers?" + filters)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // then
        then(customerService).should().listCustomers(any(), filterArgumentCaptor.capture());
        Map<String, List<String>> capturedFilters = filterArgumentCaptor.getValue();
        List<String> countryFilter = capturedFilters.get("state");
        assertThat(capturedFilters, hasKey("state"));
        assertThat(countryFilter, hasSize(1));
        assertThat(countryFilter.get(0), is(State.VALID.name()));
    }

    @Test
    void givenNonExistingStateFilter_whenGetCustomers_shouldThrowGeneralException() throws Exception {
        // given
        String errorMessage = "please check state parameter";
        String filters = "filters[state]=TEST";
        given(customerService.listCustomers(any(), any())).willThrow(new GeneralException(errorMessage));
        // when
        mockMvc.perform(get("/api/v1/customers?" + filters)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.message", not(emptyString())))
                .andExpect(jsonPath("$.message", is(errorMessage)));
        // then
        then(customerService).should().listCustomers(any(), filterArgumentCaptor.capture());
    }

    @Test
    void givenValidFilters_whenGetCustomers_shouldReturnOnlySpecifiedCountryAndState() throws Exception {
        // given
        String filters = "filters[country]=CAMEROON&filters[state]=VALID";
        // when
        mockMvc.perform(get("/api/v1/customers?" + filters)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // then
        then(customerService).should().listCustomers(any(), filterArgumentCaptor.capture());
        Map<String, List<String>> capturedFilters = filterArgumentCaptor.getValue();
        List<String> countryFilter = capturedFilters.get("country");
        List<String> stateFilter = capturedFilters.get("state");
        assertThat(capturedFilters, hasKey("country"));
        assertThat(countryFilter, hasSize(1));
        assertThat(countryFilter.get(0), is(Country.CAMEROON.name()));
        assertThat(capturedFilters, hasKey("state"));
        assertThat(stateFilter, hasSize(1));
        assertThat(stateFilter.get(0), is(State.VALID.name()));
    }

    @Test
    void whenGetCustomer_shouldReturnValidJsonArray() throws Exception {
        // given
        given(customerService.listCustomers(any(), any())).willReturn(validCustomers);

        // when
        mockMvc.perform(get("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", is(0)))
                .andExpect(jsonPath("$.size", is(1)))
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.content", notNullValue()))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(validCustomer.getId())))
                .andExpect(jsonPath("$.content[0].name", is(validCustomer.getName())))
                .andExpect(jsonPath("$.content[0].phone", is(validCustomer.getPhone())))
                .andExpect(jsonPath("$.content[0].code", is(validCustomer.getCode())))
                .andExpect(jsonPath("$.content[0].country", is(validCustomer.getCountry().name())))
                .andExpect(jsonPath("$.content[0].state", is(validCustomer.getState().name())));

        // then
        then(customerService).should().listCustomers(any(), any());
    }

}
