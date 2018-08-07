package org.hisp.dhis.client.sdk.models.common;

public class UnsupportedServerVersionException extends RuntimeException {
    public UnsupportedServerVersionException(int serverVersion, int minApiVersion) {
        super(String.format(
                "The server version %d is not supported. Minimum server version supported is %d",
                serverVersion, minApiVersion));
    }
}
