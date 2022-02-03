package tarek.country.numbers.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tarek.country.numbers.entities.enums.Country;
import tarek.country.numbers.entities.enums.State;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Customer {
    @Id
    private Integer id;
    @Column(length = 50)
    private String name;
    @Column(length = 50)
    private String phone;
    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private Country country;
    @Column(length = 10)
    private String code;
    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private State state;

}