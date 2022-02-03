package tarek.country.numbers.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import tarek.country.numbers.dto.CustomerDto;
import tarek.country.numbers.entities.Customer;
import tarek.country.numbers.entities.enums.Country;
import tarek.country.numbers.entities.enums.State;
import tarek.country.numbers.exceptions.GeneralException;
import tarek.country.numbers.mappers.CustomerMapper;
import tarek.country.numbers.repositories.CustomerRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ServiceHelper serviceHelper;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerDto validCustomerDto;

    private Customer validCustomer;

    private List<Customer> validCustomers;

    private Pageable defaultPagination;


    @BeforeEach
    void setup() {
        int randomId = new Random().nextInt();
        validCustomerDto = new CustomerDto();
        validCustomerDto.setId(randomId);
        validCustomerDto.setName("test 1");
        validCustomerDto.setPhone("(212) 698054317");
        validCustomerDto.setCode("212");
        validCustomerDto.setCountry(Country.MOROCCO);
        validCustomerDto.setState(State.VALID);
        validCustomer = new Customer();
        validCustomer.setId(randomId);
        validCustomer.setName("test 1");
        validCustomer.setPhone("(212) 698054317");
        validCustomer.setCode("212");
        validCustomer.setCountry(Country.MOROCCO);
        validCustomer.setState(State.VALID);
        validCustomers = List.of(validCustomer);
        defaultPagination = PageRequest.of(0, 10);
    }

    @Test
    void whenNoFiltersProvided_shouldReturnPagedCustomers() {
        // given
        given(customerRepository.findAll(any(Pageable.class))).willReturn(new PageImpl<>(validCustomers));
        given(customerMapper.customerToCustomerDto(any())).willReturn(validCustomerDto);
        // when
        Page<CustomerDto> pageOfCustomers = customerService.listCustomers(defaultPagination, any());
        List<CustomerDto> customers = pageOfCustomers.getContent();
        // then
        assertThat(pageOfCustomers.getSize(), is(1));
        assertThat(pageOfCustomers.getNumber(), is(0));
        assertThat(customers, notNullValue());
        assertThat(customers, hasSize(1));
        assertThat(customers.get(0).getId(), is(validCustomerDto.getId()));
        assertThat(customers.get(0).getName(), is(validCustomerDto.getName()));
        assertThat(customers.get(0).getPhone(), is(validCustomerDto.getPhone()));
        assertThat(customers.get(0).getCode(), is(validCustomerDto.getCode()));
        assertThat(customers.get(0).getCountry().name(), is(validCustomerDto.getCountry().name()));
        assertThat(customers.get(0).getState().name(), is(validCustomerDto.getState().name()));
        then(customerRepository).should().findAll(any(Pageable.class));
        then(customerMapper).should().customerToCustomerDto(any());
    }

    @Test
    void whenOnlyCountryFilterProvided_shouldReturnPagedCustomers() {
        // given
        Map<String, List<String>> filters = new HashMap<>();
        String validCountry = Country.MOROCCO.name();
        filters.put("country", List.of(validCountry));
        given(customerRepository.findByCountryIn(any(), any(Pageable.class))).willReturn(new PageImpl<>(validCustomers));
        given(customerMapper.customerToCustomerDto(any())).willReturn(validCustomerDto);
        // when
        Page<CustomerDto> pageOfCustomers = customerService.listCustomers(defaultPagination, filters);
        List<CustomerDto> customers = pageOfCustomers.getContent();
        // then
        assertThat(pageOfCustomers.getSize(), is(1));
        assertThat(pageOfCustomers.getNumber(), is(0));
        assertThat(customers, notNullValue());
        assertThat(customers, hasSize(1));
        assertThat(customers.get(0).getId(), is(validCustomerDto.getId()));
        assertThat(customers.get(0).getName(), is(validCustomerDto.getName()));
        assertThat(customers.get(0).getPhone(), is(validCustomerDto.getPhone()));
        assertThat(customers.get(0).getCode(), is(validCustomerDto.getCode()));
        assertThat(customers.get(0).getCountry().name(), is(validCountry));
        assertThat(customers.get(0).getState().name(), is(validCustomerDto.getState().name()));
        then(customerRepository).should().findByCountryIn(any(), any(Pageable.class));
        then(customerMapper).should().customerToCustomerDto(any());
    }

    @Test
    void whenOnlyStateFilterProvided_shouldReturnPagedCustomers() {
        // given
        Map<String, List<String>> filters = new HashMap<>();
        String validState = State.VALID.name();
        filters.put("state", List.of(validState));
        given(customerRepository.findByStateIn(any(), any(Pageable.class))).willReturn(new PageImpl<>(validCustomers));
        given(customerMapper.customerToCustomerDto(any())).willReturn(validCustomerDto);
        // when
        Page<CustomerDto> pageOfCustomers = customerService.listCustomers(defaultPagination, filters);
        List<CustomerDto> customers = pageOfCustomers.getContent();
        // then
        assertThat(pageOfCustomers.getSize(), is(1));
        assertThat(pageOfCustomers.getNumber(), is(0));
        assertThat(customers, notNullValue());
        assertThat(customers, hasSize(1));
        assertThat(customers.get(0).getId(), is(validCustomerDto.getId()));
        assertThat(customers.get(0).getName(), is(validCustomerDto.getName()));
        assertThat(customers.get(0).getPhone(), is(validCustomerDto.getPhone()));
        assertThat(customers.get(0).getCode(), is(validCustomerDto.getCode()));
        assertThat(customers.get(0).getCountry().name(), is(validCustomerDto.getCountry().name()));
        assertThat(customers.get(0).getState().name(), is(validState));
        then(customerRepository).should().findByStateIn(any(), any(Pageable.class));
        then(customerMapper).should().customerToCustomerDto(any());
    }

    @Test
    void whenCountryAndStateFiltersProvided_shouldReturnPagedCustomers() {
        // given
        Map<String, List<String>> filters = new HashMap<>();
        String validState = State.VALID.name();
        String validCountry = Country.MOROCCO.name();
        filters.put("state", List.of(validState));
        filters.put("country", List.of(validCountry));
        given(customerRepository.findByCountryInAndStateIn(any(), any(), any(Pageable.class))).willReturn(new PageImpl<>(validCustomers));
        given(customerMapper.customerToCustomerDto(any())).willReturn(validCustomerDto);
        // when
        Page<CustomerDto> pageOfCustomers = customerService.listCustomers(defaultPagination, filters);
        List<CustomerDto> customers = pageOfCustomers.getContent();
        // then
        assertThat(pageOfCustomers.getSize(), is(1));
        assertThat(pageOfCustomers.getNumber(), is(0));
        assertThat(customers, notNullValue());
        assertThat(customers, hasSize(1));
        assertThat(customers.get(0).getId(), is(validCustomerDto.getId()));
        assertThat(customers.get(0).getName(), is(validCustomerDto.getName()));
        assertThat(customers.get(0).getPhone(), is(validCustomerDto.getPhone()));
        assertThat(customers.get(0).getCode(), is(validCustomerDto.getCode()));
        assertThat(customers.get(0).getCountry().name(), is(validCountry));
        assertThat(customers.get(0).getState().name(), is(validState));
        then(customerRepository).should().findByCountryInAndStateIn(any(), any(), any(Pageable.class));
        then(customerMapper).should().customerToCustomerDto(any());
    }

    @Test
    void whenNoFiltersAndNoPagination_shouldThrowGeneralException() {
        //given when then
        assertThrows(
                GeneralException.class,
                () -> customerService.listCustomers(null, null));
        then(customerRepository).shouldHaveNoInteractions();
    }

    @Test
    void whenCustomersAlreadyUpdated_shouldReturnEmptyList() {
        // given
        given(customerRepository.findByCountryIsNullOrStateIsNullOrCodeIsNull()).willReturn(List.of());

        // when
        List<Customer> customers = customerService.updateCustomersCountryAndState();

        // then
        assertThat(customers, notNullValue());
        assertTrue(customers.isEmpty());
        then(customerRepository).should().findByCountryIsNullOrStateIsNullOrCodeIsNull();
    }

    @Test
    void whenCustomersNotUpdated_shouldReturnListIncludesCountryAndStateAndCode() {
        // given
        Country validCountry = Country.MOROCCO;
        given(customerRepository.findByCountryIsNullOrStateIsNullOrCodeIsNull()).willReturn(validCustomers);
        given(customerRepository.saveAllAndFlush(any())).willReturn(validCustomers);
        given(serviceHelper.getPhoneNumberCountryCode(any())).willReturn(validCustomer.getCode());
        // when
        List<Customer> customers = customerService.updateCustomersCountryAndState();
        // then
        assertThat(customers, notNullValue());
        assertThat(customers, hasSize(1));
        assertThat(customers.get(0).getCode(), is(validCustomer.getCode()));
        assertThat(customers.get(0).getCountry().name(), is(validCustomer.getCountry().name()));
        assertThat(customers.get(0).getState().name(), is(validCustomer.getState().name()));
        assertEquals(validCountry.matches(customers.get(0).getPhone()), validCustomer.getState());
        then(customerRepository).should().findByCountryIsNullOrStateIsNullOrCodeIsNull();
        then(customerRepository).should().saveAllAndFlush(any());
        then(serviceHelper).should().getPhoneNumberCountryCode(any());
    }

    @Test
    void whenCustomerWithNoPhoneNumber_shouldThrowGeneralException() {
        // given
        validCustomer.setPhone("");
        given(customerRepository.findByCountryIsNullOrStateIsNullOrCodeIsNull()).willReturn(validCustomers);
        // when then
        assertThrows(
                GeneralException.class,
                () -> customerService.updateCustomersCountryAndState());
        then(customerRepository).should().findByCountryIsNullOrStateIsNullOrCodeIsNull();
    }

}
