package org.hisp.dhis.rules.functions;

import org.hisp.dhis.rules.RuleVariableValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class RuleFunctionAddControlDigits extends RuleFunction {
    static final String D2_ADD_CONTROL_DIGITS = "d2:addControlDigits";

    @Nonnull
    static RuleFunctionAddControlDigits create() {
        return new RuleFunctionAddControlDigits();
    }

    @Nonnull
    @Override
    public String evaluate(@Nonnull List<String> arguments,
            Map<String, RuleVariableValue> valueMap) {
        if (arguments.size() != 1) {
            throw new IllegalArgumentException("One argument was expected, " +
                    arguments.size() + " were supplied");
        }

        return addControlDigits(arguments.get(0));
    }

    private static String addControlDigits(@Nullable final String str) {
        String baseNumber = str;
        String[] baseDigits = baseNumber.split("");

        boolean error = false;

        int firstDigit = 0;
        int secondDigit = 0;

        if (baseDigits.length < 10) {
            int firstSum = 0;
            int baseNumberLength = baseDigits.length;

            //weights support up to 9 base digits:
            int[] firstWeights = {3, 7, 6, 1, 8, 9, 4, 5, 2};
            for (int i = 0; i < baseNumberLength && !error; i++) {
                firstSum += Integer.parseInt(baseDigits[i]) * firstWeights[i];
            }

            firstDigit = firstSum % 11;

            //Push the first digit to the array before continuing, as the second digit is a result of the
            //base digits and the first control digit.
            baseDigits=addFirstDigitIntoArray(baseDigits, String.valueOf(firstDigit));
            //Weights support up to 9 base digits plus first control digit:
            int[] secondWeights = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
            int secondSum = 0;
            for (int i = 0; i < baseNumberLength + 1 && !error; i++) {
                secondSum += Integer.parseInt(baseDigits[i]) * secondWeights[i];
            }
            secondDigit = secondSum % 11;

            if (firstDigit == 10) {
                System.out.println("First control digit became 10, replacing with 0");
                firstDigit = 0;
            }
            if (secondDigit == 10) {
                System.out.println("Second control digit became 10, replacing with 0");
                secondDigit = 0;
            }
        }
        if (!error) {
            //Replace the end evaluation of the dhis function:
            return String.valueOf(baseNumber +""+ firstDigit +""+ secondDigit);
        } else {
            //Replace the end evaluation of the dhis function:
            return baseNumber;
        }
    }

    public static String[] addFirstDigitIntoArray(String[] theArray, String nextDigit)
    {
        String[] newArray = new String[theArray.length+1];
        for(int i=0;i<newArray.length;i++)
        {
            if(i == newArray.length-1){
                newArray[i] = nextDigit;
            }else {
                newArray[i] = theArray[i];
            }
        }
        return newArray;
    }
}
