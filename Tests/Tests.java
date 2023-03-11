import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {

    @Test
    @DisplayName("Regular user inputs work")
    void testUserInput() {
        InputProcessor testProcessor = new InputProcessor();
        int[][] expectedResult = {{5-1,5-1},{7-1,7-1}};

        assertArrayEquals(expectedResult, testProcessor.processInput("5,5-7,7"),
                "Regular user inputs work");
    }
}
