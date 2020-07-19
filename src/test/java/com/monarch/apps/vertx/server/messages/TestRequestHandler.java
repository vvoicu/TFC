package com.monarch.apps.vertx.server.messages;

import java.util.logging.Logger;

import com.monarch.tools.Constants;
import com.monarch.tools.utils.StringUtils;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
/**
 * Handler is set on a certain endpoint in the server and may be reused across multiple endpoints.
 * Here in the Request Handler we extract the data received from a request type declared in the server where the handler is used.
 * Data is then processed and a response is sent to the client.
 * 
 * @author vvoicu
 *
 */
public class TestRequestHandler implements Handler<RoutingContext> {
	private Logger logger = Logger.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	private ClientResponseMessage clientResponseMessage;

	@Override
	public void handle(RoutingContext routingContext) {
		//if bad things happen - return a status error code
		if (routingContext.getBodyAsString() == null || routingContext.getBodyAsString().isEmpty()) {
			routingContext.response().setStatusCode(Constants.ERROR_CODE_INTERNAL).end("Failed in Message Handler: Body format incorrect");
		} else {
			//else extract data
			clientResponseMessage = new ClientResponseMessage();

			String clientRequestBody = routingContext.getBodyAsString();
			String nanoId = String.valueOf(System.nanoTime());
			//check for server kill switch in the message
			StringUtils.checkForKillSwitch(clientRequestBody);

			//add to the response the current time marker and the received request
			//extra data may be added here
			clientResponseMessage.setNanoId(nanoId);
			clientResponseMessage.setReceivedPayload(clientRequestBody);

			logger.info("MessageHandler - Data: " + clientRequestBody);

			//response sent to the client as json - data type may be changed
			routingContext.response().setStatusCode(200).end(clientResponseMessage.toJson());
		}
	}
}
