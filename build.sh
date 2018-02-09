#!/bin/bash
set -xe

# You can run it from any directory.
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_DIR=$DIR/

if [ "$TEST_SUITE" == "integration" ]
then
./execute_emulator.sh
"$PROJECT_DIR"/gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.size=small
fi
if [ "$TEST_SUITE" == "integration_medium" ]
then
./execute_emulator.sh
"$PROJECT_DIR"/gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.size=medium
fi
if [ "$TEST_SUITE" == "integration_large17" ]
then
./execute_emulator.sh
"$PROJECT_DIR"/gradlew -Pandroid.testInstrumentationRunnerArguments.class=org.hisp.dhis.android.core.trackedentity.TrackedEntityInstanceCallRealIntegrationShould#download_tei_enrollments_and_events core:connectedAndroidTest
fi
if [ "$TEST_SUITE" == "integration_large18" ]
then
./execute_emulator.sh
"$PROJECT_DIR"/gradlew -Pandroid.testInstrumentationRunnerArguments.class=org.hisp.dhis.android.core.trackedentity.TrackedEntityInstanceCallRealIntegrationShould#response_true_when_data_sync core:connectedAndroidTest
fi
if [ "$TEST_SUITE" == "integration_large19" ]
then
./execute_emulator.sh
"$PROJECT_DIR"/gradlew -Pandroid.testInstrumentationRunnerArguments.class=org.hisp.dhis.android.core.trackedentity.TrackedEntityInstanceCallRealIntegrationShould#response_true_when_data_sync2 core:connectedAndroidTest
fi
