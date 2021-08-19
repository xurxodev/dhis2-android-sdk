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

package org.hisp.dhis.android.core.program.programindicatorengine.internal.dataitem;

import org.hisp.dhis.android.core.common.AggregationType;
import org.hisp.dhis.android.core.dataelement.DataElement;
import org.hisp.dhis.android.core.event.Event;
import org.hisp.dhis.android.core.parser.internal.expression.CommonExpressionVisitor;
import org.hisp.dhis.android.core.program.programindicatorengine.internal.ProgramExpressionItem;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityDataValue;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"PMD.CyclomaticComplexity"})
public class ProgramItemStageElement
        extends ProgramExpressionItem {

    @Override
    public Object evaluate(ExpressionParser.ExprContext ctx, CommonExpressionVisitor visitor) {
        String stageId = ctx.uid0.getText();
        String dataElementId = ctx.uid1.getText();

        List<Event> eventList = visitor.getProgramIndicatorContext().events().get(stageId);

        String value = null;

        if (eventList != null) {
            List<TrackedEntityDataValue> candidates = getCandidates(eventList, dataElementId);

            AggregationType aggregationType = visitor.getProgramIndicatorContext().programIndicator().aggregationType();

            if (!candidates.isEmpty()) {
                if (AggregationType.LAST.equals(aggregationType) ||
                        AggregationType.LAST_AVERAGE_ORG_UNIT.equals(aggregationType)) {
                    value = candidates.get(candidates.size() - 1).value();
                } else if (AggregationType.AVERAGE.equals(aggregationType)) {
                    double average = avg(extractValues(candidates));

                    value = String.valueOf(average);
                } else if (AggregationType.SUM.equals(aggregationType)) {
                    double sum = sum(extractValues(candidates));

                    value = String.valueOf(sum);
                } else {
                    value = candidates.get(candidates.size() - 1).value();
                }
            }
        }

        DataElement dataElement = visitor.getDataElementStore().selectByUid(dataElementId);

        Object handledValue = visitor.handleNulls(value);
        String strValue = handledValue == null ? null : handledValue.toString();

        return formatValue(strValue, dataElement.valueType());
    }

    private Number[] extractValues(List<TrackedEntityDataValue> teDataValues) {
        List<Number> values = new ArrayList<>();

        for (TrackedEntityDataValue teDataValue : teDataValues) {
            values.add(Double.parseDouble(teDataValue.value()));
        }

        return values.toArray(new Number[0]);
    }

    public Double sum(Number... values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Argument is null or empty");
        }

        double sum = 0.0;

        for (Number number : values) {
            sum = sum + number.doubleValue();
        }

        return sum;
    }

    public Double avg(Number... values) {
        double sum = sum(values);

        return sum/values.length;
    }

    private List<TrackedEntityDataValue> getCandidates(List<Event> events, String dataElement) {
        List<TrackedEntityDataValue> candidates = new ArrayList<>();
        for (Event event : events) {
            if (event.trackedEntityDataValues() == null) {
                continue;
            }
            for (TrackedEntityDataValue dataValue : event.trackedEntityDataValues()) {
                if (dataElement.equals(dataValue.dataElement())) {
                    candidates.add(dataValue);
                }
            }
        }
        return candidates;
    }

}
