package tarek.country.numbers.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import tarek.country.numbers.entities.enums.Country;
import tarek.country.numbers.entities.enums.State;

@Data
@NoArgsConstructor
public class CustomerDto {
    private Integer id;
    private String name;
    private String phone;
    private Country country;
    private String code;
    private State state;
}
