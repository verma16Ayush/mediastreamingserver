package com.avr.mediastreamingserver.Utils;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class UtilsTest {

    private static Stream<Arguments> isValidVIdeoFileTestParams() {
        return Stream.of(
            Arguments.arguments("adfegnfr.mkv", Boolean.TRUE),
            Arguments.arguments("adfegnfr.mp4", Boolean.TRUE),
            Arguments.arguments("adfegnfr.flv", Boolean.TRUE),
            Arguments.arguments("adfegnfr.3gp", Boolean.FALSE),
            Arguments.arguments("adfegnfr", Boolean.FALSE)
        );
    }
    
    @ParameterizedTest
    @MethodSource("isValidVIdeoFileTestParams")
    void givenilesWhenIsValidVideoFileIsCalledThenVerifyTheFunctionReturnsAppropirateValue(String fileName, Boolean expectedReturnVal) {
        Assertions.assertEquals(expectedReturnVal, Utils.isValidVideoFile(fileName));
    }
}
