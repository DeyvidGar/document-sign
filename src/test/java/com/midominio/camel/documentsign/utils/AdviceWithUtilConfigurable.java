package com.midominio.camel.documentsign.utils;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.model.RouteDefinition;

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

    /** Method to replace from method with AdviceWith. */
    public RouteDefinition replaceFromWith(String startTest) throws Exception {
        return AdviceWith.adviceWith(
                context,
                routeId,
                route -> route
                        .replaceFromWith(startTest)
        );
    }

     /** Method to intercept endpoint, skip and go to new mock endpoint. */
    public RouteDefinition interceptAndSkipAndTo(String uri, String mock) throws Exception {
        return AdviceWith.adviceWith(
                context,
                routeId,
                route -> route
                        .interceptSendToEndpoint(uri)
                        .skipSendToOriginalEndpoint()
                        .to(mock)
        );
    }

    /** When route is complete go to mock endpoint. */
    public RouteDefinition onCompletationTo(String mock) throws Exception {
        return AdviceWith.adviceWith(
                context,
                routeId,
                route -> route
                        .onCompletion()
                        .to(mock)
        );
    }
}
