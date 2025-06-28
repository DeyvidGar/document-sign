package com.midominio.camel.documentsign.utils;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.AdviceWith;

/** AdviceWith util class. */
public class AdviceWithUtilConfigurable {

    /** CamelContext. */
    private final CamelContext context;
    /** Route id. */
    private final String routeId;

    public AdviceWithUtilConfigurable(CamelContext context, String routeId) {
        this.context = context;
        this.routeId = routeId;
    }

    /**
     * Method to replace from method with AdviceWith.
     * @param mockEndpoint for from methdo.
     * @throws Exception general.
     */
    public void replaceFromWith(String mockEndpoint) throws Exception {
        AdviceWith.adviceWith(context, routeId,
                route ->
                    route
                        .replaceFromWith(mockEndpoint)
        );
    }

    /**
     * Method to intercept endpoint, skip and go to new mock endpoint.
     * @param uri to intercept.
     * @param mock endpoint.
     * @throws Exception general.
     */
    public void interceptAndSkipAndTo(String uri, String mock) throws Exception {
        AdviceWith.adviceWith(context, routeId,
                route ->
                    route
                        .interceptSendToEndpoint(uri)
                        .skipSendToOriginalEndpoint()
                        .to(mock)
        );
    }

    /**
     * When route is complete go to mock endpoint.
     * @param mock endpoint.
     * @throws Exception general.
     */
    public void onCompletationTo(String mock) throws Exception {
        AdviceWith.adviceWith(context, routeId,
                route ->
                    route
                        .onCompletion()
                        .to(mock)
        );
    }
}
