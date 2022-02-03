package tarek.country.numbers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(scripts = "/clean-up.sql", executionPhase = AFTER_TEST_METHOD)
class NumbersApplicationTests {

    @Test
    void contextLoads() {
    }

}
