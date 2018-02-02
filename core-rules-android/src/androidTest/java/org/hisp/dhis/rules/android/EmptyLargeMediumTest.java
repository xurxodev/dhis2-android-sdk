package org.hisp.dhis.rules.android;

import android.support.test.filters.MediumTest;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This class is made to not fail when executing android test with size medium and large.
 */
@RunWith(AndroidJUnit4.class)
public class EmptyLargeMediumTest {

    @Test
    @SmallTest
    public void emptyMediumTest() {

    }

    @Test
    @MediumTest
    public void emptyLargeTest() {

    }
}
