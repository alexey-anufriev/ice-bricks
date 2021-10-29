package ice.bricks.reflection.tests.fixtures;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TestPojo {

    private final String strValue = "test";
    private int intValue;

}
