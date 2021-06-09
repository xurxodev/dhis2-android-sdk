package org.hisp.dhis.client.sdk.models.common;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

public class SystemInfoShould {

    @Rule
    public ExpectedException mExpectedException = ExpectedException.none();

    @org.junit.jupiter.api.Test
    public void return_min_api_version_if_server_version_is_empty() {
        SystemInfo systemInfo = new SystemInfo();

        assertThat(systemInfo.getApiVersion(),is(SystemInfo.minSupportedAPIVersion));
    }

    @org.junit.jupiter.api.Test
    public void throw_unsupported_server_exception_if_server_version_is_smaller_than_min() {
        mExpectedException.expect(UnsupportedServerVersionException.class);
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.version = "2." + (SystemInfo.minSupportedAPIVersion - 6);;

        systemInfo.getApiVersion();
    }

    @org.junit.jupiter.api.Test
    public void return_max_api_version_if_server_version_greater_than_max() {
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.version = "2." + (SystemInfo.maxSupportedAPIVersion + 1);

        assertThat(systemInfo.getApiVersion(),is(SystemInfo.maxSupportedAPIVersion));
    }

    @Test
    public void return_expected_api_version_if_server_version_is_between_thresholds() {
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.version = "2." + (SystemInfo.maxSupportedAPIVersion - 1);

        assertThat(systemInfo.getApiVersion(),is(SystemInfo.maxSupportedAPIVersion - 1));
    }
}