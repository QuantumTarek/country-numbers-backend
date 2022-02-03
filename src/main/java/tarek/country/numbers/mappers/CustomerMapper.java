package tarek.country.numbers.mappers;

import org.mapstruct.Mapper;
import tarek.country.numbers.dto.CustomerDto;
import tarek.country.numbers.entities.Customer;

@Mapper
public interface CustomerMapper {

    CustomerDto customerToCustomerDto(Customer customer);

    Customer customerDtoToCustomer(CustomerDto customerDto);
}
