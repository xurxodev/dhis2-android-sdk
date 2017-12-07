package org.hisp.dhis.rules.functions;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;

import org.hisp.dhis.rules.RuleVariableValue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@RunWith(JUnit4.class)
public class RuleFunctionAddControlDigitsShould {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void evaluate_correct_add_control_digits_value() {
        RuleFunction ceil = RuleFunctionAddControlDigits.create();

        String ceiledNumber  = ceil.evaluate(Arrays.asList("1"),
                new HashMap<String, RuleVariableValue>());
        assertThat(ceiledNumber).isEqualTo("136");

        ceiledNumber  = ceil.evaluate(Arrays.asList("12"),
                new HashMap<String, RuleVariableValue>());
        assertThat(ceiledNumber).isEqualTo("1269");

        ceiledNumber  = ceil.evaluate(Arrays.asList("123"),
                new HashMap<String, RuleVariableValue>());
        assertThat(ceiledNumber).isEqualTo("12324");

        ceiledNumber  = ceil.evaluate(Arrays.asList("1234"),
                new HashMap<String, RuleVariableValue>());
        assertThat(ceiledNumber).isEqualTo("123466");

        ceiledNumber  = ceil.evaluate(Arrays.asList("1234567890"),
                new HashMap<String, RuleVariableValue>());
        assertThat(ceiledNumber).isEqualTo("123456789000");

        ceiledNumber  = ceil.evaluate(Arrays.asList("99999"),
                new HashMap<String, RuleVariableValue>());
        assertThat(ceiledNumber).isEqualTo("9999950");
        ceiledNumber  = ceil.evaluate(Arrays.asList("000"),
                new HashMap<String, RuleVariableValue>());
        assertThat(ceiledNumber).isEqualTo("00000");
    }

    @Test
    public void throw_unsupported_operation_exception_at_least_two_digits_expected() {
        thrown.expect(UnsupportedOperationException.class);
        RuleFunctionAddControlDigits.create().evaluate(Arrays.asList("1"),
                new HashMap<String, RuleVariableValue>());
    }

    @Test
    public void throw_illegal_argument_exception_on_more_arguments_count_than_expected() {
        thrown.expect(IllegalArgumentException.class);
        RuleFunctionAddControlDigits.create().evaluate(Arrays.asList("5.9", "6.8"),
                new HashMap<String, RuleVariableValue>());
    }

    @Test
    public void throw_illegal_argument_exception_on_less_arguments_count_than_expected() {
        thrown.expect(IllegalArgumentException.class);
        RuleFunctionAddControlDigits.create().evaluate(new ArrayList<String>(),
                new HashMap<String, RuleVariableValue>());
    }
}
