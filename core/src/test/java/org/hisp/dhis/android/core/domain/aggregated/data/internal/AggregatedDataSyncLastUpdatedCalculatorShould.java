/*
 *  Copyright (c) 2004-2021, University of Oslo
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *  Neither the name of the HISP project nor the names of its contributors may
 *  be used to endorse or promote products derived from this software without
 *  specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.hisp.dhis.android.core.domain.aggregated.data.internal;

import org.hisp.dhis.android.core.data.dataset.DataSetSamples;
import org.hisp.dhis.android.core.data.utils.FillPropertiesTestUtils;
import org.hisp.dhis.android.core.dataset.DataSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

public class AggregatedDataSyncLastUpdatedCalculatorShould {

    private DataSet dataSet = DataSetSamples.getDataSet();

    private int dataElementsHash = 1111111;

    private int organisationUnitsHash = 22222222;

    private int pastPeriods = 5;

    private Date syncLastUpdated = FillPropertiesTestUtils.parseDate("2018-01-01T15:08:27.882");
    private Date expectedLastUpdated = FillPropertiesTestUtils.parseDate("2017-12-31T15:08:27.882");

    @Mock
    private AggregatedDataSyncHashHelper hashHelper;

    private AggregatedDataSync syncValue = AggregatedDataSync.builder()
            .dataSet(dataSet.uid())
            .periodType(dataSet.periodType())
            .pastPeriods(pastPeriods)
            .futurePeriods(dataSet.openFuturePeriods())
            .dataElementsHash(dataElementsHash)
            .organisationUnitsHash(organisationUnitsHash)
            .lastUpdated(syncLastUpdated)
            .build();

    private AggregatedDataSyncLastUpdatedCalculator calculator;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(hashHelper.getDataSetDataElementsHash(dataSet)).thenReturn(dataElementsHash);
        calculator = new AggregatedDataSyncLastUpdatedCalculator(hashHelper);
    }

    @Test
    public void return_null_if_sync_value_null() {
        Date lastUpdated = calculator.getLastUpdated(null, dataSet, 3, 5, 0);
        assertThat(lastUpdated).isNull();
    }

    @Test
    public void return_expected_last_updated_if_same_values() {
        Date lastUpdated = calculator.getLastUpdated(syncValue, dataSet, pastPeriods, dataSet.openFuturePeriods(), organisationUnitsHash);
        assertThat(lastUpdated).isEqualTo(expectedLastUpdated);
    }

    @Test
    public void return_null_if_organisation_units_hash_changed() {
        Date lastUpdated = calculator.getLastUpdated(syncValue, dataSet, pastPeriods, dataSet.openFuturePeriods(), 33333);
        assertThat(lastUpdated).isNull();
    }

    @Test
    public void return_null_if_data_set_elements_hash_changed() {
        when(hashHelper.getDataSetDataElementsHash(dataSet)).thenReturn(77777);
        Date lastUpdated = calculator.getLastUpdated(syncValue, dataSet, pastPeriods, dataSet.openFuturePeriods(), organisationUnitsHash);
        assertThat(lastUpdated).isNull();
    }

    @Test
    public void return_null_if_future_periods_are_increased() {
        Date lastUpdated = calculator.getLastUpdated(syncValue, dataSet, pastPeriods, dataSet.openFuturePeriods() + 1, organisationUnitsHash);
        assertThat(lastUpdated).isNull();
    }

    @Test
    public void return_expected_last_updated_if_future_periods_are_decreased() {
        Date lastUpdated = calculator.getLastUpdated(syncValue, dataSet, pastPeriods, dataSet.openFuturePeriods() - 1, organisationUnitsHash);
        assertThat(lastUpdated).isEqualTo(expectedLastUpdated);
    }

    @Test
    public void return_null_if_past_periods_are_increased() {
        Date lastUpdated = calculator.getLastUpdated(syncValue, dataSet, pastPeriods + 1, dataSet.openFuturePeriods(), organisationUnitsHash);
        assertThat(lastUpdated).isNull();
    }

    @Test
    public void return_expected_past_updated_if_future_periods_are_decreased() {
        Date lastUpdated = calculator.getLastUpdated(syncValue, dataSet, pastPeriods - 1, dataSet.openFuturePeriods(), organisationUnitsHash);
        assertThat(lastUpdated).isEqualTo(expectedLastUpdated);
    }
}