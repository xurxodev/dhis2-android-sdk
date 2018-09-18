/*
 * Copyright (c) 2016, University of Oslo
 *
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.hisp.dhis.client.sdk.android.program;

import static org.hisp.dhis.client.sdk.android.api.network.NetworkUtils.call;
import static org.hisp.dhis.client.sdk.android.api.network.NetworkUtils.unwrap;

import org.hisp.dhis.client.sdk.core.common.Fields;
import org.hisp.dhis.client.sdk.core.common.network.ApiException;
import org.hisp.dhis.client.sdk.core.common.utils.CollectionUtils;
import org.hisp.dhis.client.sdk.core.program.ProgramStageDataElementApiClient;
import org.hisp.dhis.client.sdk.models.program.ProgramStageDataElement;
import org.hisp.dhis.client.sdk.models.program.Program;
import org.hisp.dhis.client.sdk.models.program.ProgramStage;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProgramStageDataElementApiClientImpl implements ProgramStageDataElementApiClient {
    private final ProgramStageDataElementApiClientRetrofit apiClientRetrofit;

    public ProgramStageDataElementApiClientImpl(
            ProgramStageDataElementApiClientRetrofit retrofitClient) {
        this.apiClientRetrofit = retrofitClient;
    }

    @Override
    public List<ProgramStageDataElement> getProgramStageDataElements(
            Fields fields, DateTime lastUpdated, Set<String> uids) throws ApiException {


        Map<String, String> queryMap = new HashMap<>();
        List<String> filters = new ArrayList<>();

        /* disable paging */
        queryMap.put("paging", "false");

        /* filter programs by lastUpdated field */
        if (lastUpdated != null) {
            filters.add("lastUpdated:gt:" + lastUpdated.toString());
        }

        addFields(fields, queryMap);

        List<ProgramStageDataElement> models = new ArrayList<>();
        List<Program> programs = new ArrayList<>();
        if (uids != null && !uids.isEmpty()) {

            // splitting up request into chunks
            List<String> idFilters = buildIdFilter(uids);
            for (String idFilter : idFilters) {
                List<String> combinedFilters = new ArrayList<>(filters);
                combinedFilters.add(idFilter);

                // downloading subset of models
                programs.addAll(unwrap(call(
                        apiClientRetrofit.getProgramStageDataElements(queryMap,combinedFilters)),
                        "programs"));
            }
        } else {
            programs.addAll(unwrap(call(
                    apiClientRetrofit.getProgramStageDataElements(queryMap,filters)),
                    "programs"));

        }

        for (Program program:programs) {
            if (program.getProgramStages() != null){
                for (ProgramStage programStage:program.getProgramStages()) {
                    if (programStage.getProgramStageDataElements() != null){
                        for (ProgramStageDataElement programStageDataElement:programStage.getProgramStageDataElements()){
                            models.add(programStageDataElement);
                        }
                    }
                }
            }
        }

        return models;
    }

    private void addFields(Fields fields, Map<String, String> queryMap) {
        switch (fields) {
            case BASIC: {
                queryMap.put("fields", "programStages[programStageDataElements[id]]");
                break;
            }
            case ALL: {
                queryMap.put("fields", "programStages[programStageDataElements[" +
                        "id,created,lastUpdated,access," +
                        "programStage[id],dataElement[id],allowFutureDate," +
                        "sortOrder,displayInReports,allowProvidedElsewhere,compulsory]]");
                break;
            }
        }
    }

    private static List<String> buildIdFilter(Set<String> ids) {
        List<String> idFilters = new ArrayList<>();

        if (ids != null && !ids.isEmpty()) {
            List<List<String>> splittedIds = CollectionUtils.slice(new ArrayList<>(ids), 64);
            for (List<String> listOfIds : splittedIds) {
                idFilters.add(CollectionUtils.join(listOfIds, ";"));
            }
        }

        return idFilters;
    }
}
