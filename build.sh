#!/bin/bash
set -xe

# You can run it from any directory.
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_DIR=$DIR/

if [ "$TEST_SUITE" == "integration" ]; then
    "$PROJECT_DIR"/gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.size=small
elif [ "$TEST_SUITE" == "integration_medium" ]; then
    "$PROJECT_DIR"/gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.size=medium
elif [ "$TEST_SUITE" == "integration_large17" ]; then
    "$PROJECT_DIR"/gradlew -Pandroid.testInstrumentationRunnerArguments.class=org.hisp.dhis.android.core.trackedentity.TrackedEntityInstanceCallRealIntegrationShould#download_tei_enrollments_and_events core:connectedAndroidTest
elif [ "$TEST_SUITE" == "integration_large18" ]; then
    "$PROJECT_DIR"/gradlew -Pandroid.testInstrumentationRunnerArguments.class=org.hisp.dhis.android.core.trackedentity.TrackedEntityInstanceCallRealIntegrationShould#response_true_when_data_sync core:connectedAndroidTest
elif [ "$TEST_SUITE" == "build" ]; then
    "$PROJECT_DIR"/gradlew -Pandroid.testInstrumentationRunnerArguments.class=org.hisp.dhis.android.core.trackedentity.TrackedEntityInstanceCallRealIntegrationShould#response_true_when_data_sync2 core:connectedAndroidTest
else
    echo "That module doesn't exist, now does it? ;)"
    exit 1
fi