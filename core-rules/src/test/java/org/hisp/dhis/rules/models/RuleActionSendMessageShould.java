package org.hisp.dhis.rules.models;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

@RunWith(JUnit4.class)
public class RuleActionSendMessageShould {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Test
    public void throw_illegal_argument_exception_when_create_with_null_argument() {
        thrown.expect(IllegalArgumentException.class);
        RuleActionSendMessage.create(null);
    }

    @Test
    public void createForIndicatorsMustThrowWhenBothArgumentsNull() {
        RuleActionSendMessage ruleActionSendMessage = RuleActionSendMessage.create("notification");

        assertThat(ruleActionSendMessage.notification()).isEqualTo("notification");
    }

    @Test
    public void conform_to_contract_equals_and_hashcode() {
        EqualsVerifier.forClass(RuleActionSendMessage.create("").getClass())
                .suppress(Warning.NULL_FIELDS)
                .verify();
    }
}
