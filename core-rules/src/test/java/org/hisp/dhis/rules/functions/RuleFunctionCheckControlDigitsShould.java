package org.hisp.dhis.rules.functions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RuleFunctionCheckControlDigitsShould {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void throw_illegal_argument_exception_when_is_created() {
        thrown.expect(UnsupportedOperationException.class);
        RuleFunctionCheckControlDigits.create();
    }
}
