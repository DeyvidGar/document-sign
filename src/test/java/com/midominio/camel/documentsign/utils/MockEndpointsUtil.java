package com.midominio.camel.documentsign.utils;

import org.apache.camel.component.mock.MockEndpoint;

public class MockEndpointsUtil {

    /** Assert all mock endpoints. */
    public static void checkAssertionsSatisfied(MockEndpoint... mocks) throws InterruptedException {
        for (MockEndpoint mockEndpoint : mocks) {
            mockEndpoint.assertIsSatisfied();
        }
    }
}
