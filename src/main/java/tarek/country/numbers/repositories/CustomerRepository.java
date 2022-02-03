package tarek.country.numbers.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tarek.country.numbers.entities.Customer;
import tarek.country.numbers.entities.enums.Country;
import tarek.country.numbers.entities.enums.State;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    List<Customer> findByCountryIsNullOrStateIsNullOrCodeIsNull();

    Page<Customer> findByCountryIn(List<Country> countries, Pageable pageable);

    Page<Customer> findByStateIn(List<State> states, Pageable pageable);

    Page<Customer> findByCountryInAndStateIn(List<Country> countries, List<State> states, Pageable pageable);

}
