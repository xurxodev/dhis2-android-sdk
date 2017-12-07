package org.hisp.dhis.rules.functions;

import org.hisp.dhis.rules.RuleVariableValue;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.crypto.IllegalBlockSizeException;

final class RuleFunctionCheckControlDigits extends RuleFunction {
    static final String D2_CHECK_CONTROL_DIGITS = "d2:checkControlDigits";

    @Nonnull
    static RuleFunctionCheckControlDigits create() {
        //return new RuleFunctionCheckControlDigits();
        throw new UnsupportedOperationException("This function is not implemented in dhis2 web core.");
    }

    @Nonnull
    @Override
    public String evaluate(@Nonnull List<String> arguments,
            Map<String, RuleVariableValue> valueMap) {
        if (arguments.size() != 1) {
            throw new IllegalArgumentException("One argument was expected, " +
                    arguments.size() + " were supplied");
        }

        return checkControlDigits(arguments.get(0));
    }

    private static String checkControlDigits(@Nullable final String value) {
        //todo: This function is not implemented in dhis2 web core.
     return value;
    }
}
