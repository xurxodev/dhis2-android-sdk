#!/bin/bash
set -xe

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_DIR=$DIR/

android-wait-for-emulator
sleep 180
adb devices
adb shell input keyevent 82 &

if [ "$TEST_SUITE" == "integration" ]
then
"$PROJECT_DIR"/gradlew connectedAndroidTest -P android.testInstrumentationRunnerArguments.size=small
fi
if [ "$TEST_SUITE" == "integration_large1" ]
then
"$PROJECT_DIR"/gradlew -Pandroid.testInstrumentationRunnerArguments.class=org.hisp.dhis.android.core.LogoutCallRealIntegrationShould#delete_autenticate_user_table_only_when_log_out_after_sync_data core:connectedAndroidTest --info
fi
if [ "$TEST_SUITE" == "integration_large2" ]
then
"$PROJECT_DIR"/gradlew -Pandroid.testInstrumentationRunnerArguments.class=org.hisp.dhis.android.core.LogoutCallRealIntegrationShould#delete_autenticate_user_table_only_when_log_out_after_sync_metadata core:connectedAndroidTest --info
fi
if [ "$TEST_SUITE" == "integration_large3" ]
then
"$PROJECT_DIR"/gradlew -Pandroid.testInstrumentationRunnerArguments.class=org.hisp.dhis.android.core.LogoutCallRealIntegrationShould#have_empty_database_when_wipe_db_after_sync_data core:connectedAndroidTest --info
fi
if [ "$TEST_SUITE" == "integration_large4" ]
then
"$PROJECT_DIR"/gradlew -Pandroid.testInstrumentationRunnerArguments.class=org.hisp.dhis.android.core.LogoutCallRealIntegrationShould#have_empty_database_when_wipe_db_after_sync_metadata core:connectedAndroidTest --info
fi
if [ "$TEST_SUITE" == "integration_large5" ]
then
"$PROJECT_DIR"/gradlew -Pandroid.testInstrumentationRunnerArguments.class=org.hisp.dhis.android.core.LogoutCallRealIntegrationShould#response_successful_on_login_logout_and_login core:connectedAndroidTest --info
fi
if [ "$TEST_SUITE" == "integration_large6" ]
then
"$PROJECT_DIR"/gradlew -Pandroid.testInstrumentationRunnerArguments.class=org.hisp.dhis.android.core.MetadataCallRealIntegrationShould#response_successful_on_login_logout_and_login core:connectedAndroidTest --info
fi
if [ "$TEST_SUITE" == "integration_large8" ]
then
"$PROJECT_DIR"/gradlew -Pandroid.testInstrumentationRunnerArguments.class=org.hisp.dhis.android.core.MetadataCallRealIntegrationShould#response_successful_on_login_wipe_db_and_login core:connectedAndroidTest --info
fi
if [ "$TEST_SUITE" == "integration_large9" ]
then
"$PROJECT_DIR"/gradlew -Pandroid.testInstrumentationRunnerArguments.class=org.hisp.dhis.android.core.MetadataCallRealIntegrationShould#response_successful_on_sync_meta_data_two_times core:connectedAndroidTest --info
fi
if [ "$TEST_SUITE" == "integration_large10" ]
then
"$PROJECT_DIR"/gradlew -Pandroid.testInstrumentationRunnerArguments.class=org.hisp.dhis.android.core.category.CategoryComboEndpointCallRealIntegrationShould#download_categories_combos_and_relatives core:connectedAndroidTest --info
fi
if [ "$TEST_SUITE" == "integration_large11" ]
then
"$PROJECT_DIR"/gradlew -Pandroid.testInstrumentationRunnerArguments.class=org.hisp.dhis.android.core.category.CategoryEndpointCallRealIntegrationShould#call_categories_endpoint core:connectedAndroidTest --info
fi
if [ "$TEST_SUITE" == "integration_large12" ]
then
"$PROJECT_DIR"/gradlew -Pandroid.testInstrumentationRunnerArguments.class=org.hisp.dhis.android.core.event.EventEndPointCallRealIntegrationShould#download_event_with_category_combo_option core:connectedAndroidTest --info
fi
if [ "$TEST_SUITE" == "integration_large13" ]
then
"$PROJECT_DIR"/gradlew -Pandroid.testInstrumentationRunnerArguments.class=org.hisp.dhis.android.core.event.EventEndPointCallRealIntegrationShould#download_number_of_events_according_to_default_limit core:connectedAndroidTest --info
fi
if [ "$TEST_SUITE" == "integration_large14" ]
then
"$PROJECT_DIR"/gradlew -Pandroid.testInstrumentationRunnerArguments.class=org.hisp.dhis.android.core.event.EventPostCallRealIntegrationShould#pull_event_with_correct_category_combo_after_be_pushed core:connectedAndroidTest --info
fi
if [ "$TEST_SUITE" == "integration_large15" ]
then
"$PROJECT_DIR"/gradlew -Pandroid.testInstrumentationRunnerArguments.class=org.hisp.dhis.android.core.event.EventPostCallRealIntegrationShould#successful_response_after_sync_events core:connectedAndroidTest --info
fi
if [ "$TEST_SUITE" == "integration_large16" ]
then
"$PROJECT_DIR"/gradlew -Pandroid.testInstrumentationRunnerArguments.class=org.hisp.dhis.android.core.relationship.RelationshipTypeEnPointCallMockIntegrationShould#download_RelationShipTypes_according_to_default_query core:connectedAndroidTest --info
fi
if [ "$TEST_SUITE" == "integration_large17" ]
then
"$PROJECT_DIR"/gradlew -Pandroid.testInstrumentationRunnerArguments.class=org.hisp.dhis.android.core.trackedentity.TrackedEntityInstanceCallRealIntegrationShould#download_tei_enrollments_and_events core:connectedAndroidTest --info
fi
if [ "$TEST_SUITE" == "integration_large18" ]
then
"$PROJECT_DIR"/gradlew -Pandroid.testInstrumentationRunnerArguments.class=org.hisp.dhis.android.core.trackedentity.TrackedEntityInstanceCallRealIntegrationShould#response_true_when_data_sync core:connectedAndroidTest --info
fi
