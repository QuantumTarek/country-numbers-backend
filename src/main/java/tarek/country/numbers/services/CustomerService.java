package tarek.country.numbers.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tarek.country.numbers.dto.CustomerDto;
import tarek.country.numbers.entities.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerService {

    public Page<CustomerDto> listCustomers(Pageable pageable, Map<String, List<String>> filters);

    public List<Customer> updateCustomersCountryAndState();
}
