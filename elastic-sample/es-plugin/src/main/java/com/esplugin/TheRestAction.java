package com.esplugin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.*;

import java.io.IOException;

import static org.elasticsearch.rest.RestRequest.Method.GET;

/**
 * Created by jhkwon78 on 2017-04-27.
 */
public class TheRestAction extends BaseRestHandler  {
    final static Logger LOGGER = LogManager.getLogger(TheRestAction.class);

    @Inject
    public TheRestAction(Settings settings, RestController controller) {
        super(settings);
        controller.registerHandler(GET, "_the/{action}", this);
        controller.registerHandler(GET, "_the", this);
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient client) throws IOException {
        LOGGER.info("one _the endpoint, param {}", request.param("action"));

        String action = request.param("action");

        if (null != action) {
            return new TheRestChannelConsumer("your action is " + action);
        }

        return new TheRestChannelConsumer("default action");
    }

    class TheRestChannelConsumer implements RestChannelConsumer {

        String message;

        public TheRestChannelConsumer() {
            message = new String();
        }

        public TheRestChannelConsumer(String message) {
            this.message = message;
        }

        @Override
        public void accept(RestChannel restChannel) throws Exception {

            final XContentBuilder builder = restChannel.newBuilder();
            builder.startObject();
            builder.field("message", this.message);
            builder.endObject();

            restChannel.sendResponse(new BytesRestResponse(RestStatus.OK, builder));
        }
    }
}
