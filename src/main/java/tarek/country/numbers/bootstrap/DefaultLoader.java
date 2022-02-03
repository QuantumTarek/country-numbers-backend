package tarek.country.numbers.bootstrap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tarek.country.numbers.services.CustomerService;

/**
 * DefaultLoader
 */
@Component
@RequiredArgsConstructor
public class DefaultLoader implements CommandLineRunner {

    private final CustomerService customerService;

    @Override
    public void run(String... args) throws Exception {
        customerService.updateCustomersCountryAndState();
    }
}