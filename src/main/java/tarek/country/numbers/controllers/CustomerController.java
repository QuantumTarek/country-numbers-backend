package tarek.country.numbers.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tarek.country.numbers.controllers.model.RequestParameters;
import tarek.country.numbers.dto.CustomerDto;
import tarek.country.numbers.services.CustomerService;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @CrossOrigin
    @GetMapping
    public ResponseEntity<Page<CustomerDto>> getCustomers(@Valid RequestParameters request) {
        log.debug("page param: " + request.getPage());
        log.debug("size param: " + request.getSize());
        if (request.getFilters() != null)
            log.debug("filter params: " + request.getFilters().toString());
        return ResponseEntity.ok(customerService.listCustomers(PageRequest.of(request.getPage(), request.getSize()),
                request.getFilters()));
    }

}