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

package org.hisp.dhis.android.core.settings

import com.google.common.truth.Truth
import java.io.IOException
import java.text.ParseException
import org.hisp.dhis.android.core.common.BaseObjectShould
import org.hisp.dhis.android.core.common.ObjectShould
import org.hisp.dhis.android.core.period.PeriodType
import org.junit.Test

class AnalyticsSettingV2Should : BaseObjectShould("settings/analytics_settings_v2.json"), ObjectShould {

    @Test
    @Throws(IOException::class, ParseException::class)
    override fun map_from_json_string() {
        val analyticsSettings = objectMapper.readValue(jsonStream, AnalyticsSettings::class.java)

        Truth.assertThat(analyticsSettings.tei().size).isEqualTo(3)

        analyticsSettings.tei().forEach { tei ->
            when (tei.uid()) {
                "fqEx2avRp1L" -> {
                    Truth.assertThat(tei.name()).isEqualTo("Height evolution")
                    Truth.assertThat(tei.shortName()).isEqualTo("H. evolution")
                    Truth.assertThat(tei.program()).isEqualTo("IpHINAT79UW")
                    Truth.assertThat(tei.programStage()).isEqualTo("dBwrot7S420")
                    Truth.assertThat(tei.period()).isEquivalentAccordingToCompareTo(PeriodType.Monthly)
                    Truth.assertThat(tei.type()).isEquivalentAccordingToCompareTo(ChartType.LINE)
                }
            }
        }

        Truth.assertThat(analyticsSettings.dhisVisualizations().home().size).isEqualTo(2)
        analyticsSettings.dhisVisualizations().home().forEach { group ->
            when (group.id()) {
                "12345678910" -> {
                    Truth.assertThat(group.name()).isEqualTo("Ejemplo")
                    Truth.assertThat(group.visualizations().size).isEqualTo(2)
                }
                "12345678911" -> {
                    Truth.assertThat(group.name()).isEqualTo("Otro ejemplo")
                    Truth.assertThat(group.visualizations().size).isEqualTo(1)
                }
            }
        }

        Truth.assertThat(analyticsSettings.dhisVisualizations().dataSet().size).isEqualTo(1)
        analyticsSettings.dhisVisualizations().dataSet().forEach { map ->
            when (map.key) {
                "BfMAe6Itzgt" -> {
                    Truth.assertThat(map.value.size).isEqualTo(1)
                    Truth.assertThat(map.value[0].visualizations().size).isEqualTo(1)
                }
            }
        }

        Truth.assertThat(analyticsSettings.dhisVisualizations().program().size).isEqualTo(1)
        analyticsSettings.dhisVisualizations().program().forEach { map ->
            when (map.key) {
                "IpHINAT79UW" -> {
                    Truth.assertThat(map.value.size).isEqualTo(1)
                    Truth.assertThat(map.value[0].visualizations().size).isEqualTo(2)
                }
            }
        }
    }
}
