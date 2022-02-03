package tarek.country.numbers.controllers.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class RequestParameters {

    @NotNull
    @PositiveOrZero
    private Integer page = 0;

    @NotNull
    @Positive
    private Integer size = 10;
    private Map<String, @NotEmpty List<String>> filters = new HashMap<>();
}
