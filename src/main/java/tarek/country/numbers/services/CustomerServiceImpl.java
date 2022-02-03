package tarek.country.numbers.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tarek.country.numbers.dto.CustomerDto;
import tarek.country.numbers.entities.Customer;
import tarek.country.numbers.entities.enums.Country;
import tarek.country.numbers.exceptions.GeneralException;
import tarek.country.numbers.mappers.CustomerMapper;
import tarek.country.numbers.repositories.CustomerRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final ServiceHelper serviceHelper;

    @Override
    public Page<CustomerDto> listCustomers(Pageable pageable, Map<String, List<String>> filters) {
        Page<Customer> pagedCustomers = Page.empty();
        if (pageable == null) {
            throw new GeneralException("pageable property does not exist");
        }
        if (filters == null || filters.isEmpty()) {
            pagedCustomers = customerRepository.findAll(pageable);
        } else if (filters.containsKey("country") && !filters.containsKey("state")) {
            pagedCustomers = customerRepository.findByCountryIn(serviceHelper.toCountryList(filters.get("country")),
                    pageable);
        } else if (!filters.containsKey("country") && filters.containsKey("state")) {
            pagedCustomers = customerRepository.findByStateIn(serviceHelper.toStateList(filters.get("state")),
                    pageable);
        } else if (filters.containsKey("country") && filters.containsKey("state")) {
            pagedCustomers = customerRepository.findByCountryInAndStateIn(
                    serviceHelper.toCountryList(filters.get("country")),
                    serviceHelper.toStateList(filters.get("state")),
                    pageable);
        }
        List<CustomerDto> customersDtoList = pagedCustomers.stream().map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
        return new PageImpl<>(customersDtoList, pagedCustomers.getPageable(), pagedCustomers.getTotalElements());
    }

    @Transactional
    @Override
    public List<Customer> updateCustomersCountryAndState() {
        List<Customer> customers = customerRepository.findByCountryIsNullOrStateIsNullOrCodeIsNull();
        if (customers.isEmpty()) {
            log.info("customers data already updated");
            return customers;
        }
        log.info("updating database customers country and state");
        for (Customer customer : customers) {
            String phone = customer.getPhone();
            if (phone == null || phone.isEmpty()) {
                throw new GeneralException("please make sure all customers phone numbers exists in db");
            }
            String code = serviceHelper.getPhoneNumberCountryCode(phone);
            Country country = Country.getCountry(code);
            customer.setCode(code);
            customer.setCountry(country);
            customer.setState(country.matches(phone));
        }
        return customerRepository.saveAllAndFlush(customers);
    }

}
