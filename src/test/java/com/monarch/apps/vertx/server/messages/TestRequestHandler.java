package com.monarch.apps.vertx.server.messages;

import java.util.logging.Logger;

import com.monarch.tools.Constants;
import com.monarch.tools.utils.StringUtils;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class TestRequestHandler implements Handler<RoutingContext> {
	private Logger logger = Logger.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	private ClientResponseMessage clientResponseMessage;

	@Override
	public void handle(RoutingContext routingContext) {

		if (routingContext.getBodyAsString() == null || routingContext.getBodyAsString().isEmpty()) {
			routingContext.response().setStatusCode(Constants.ERROR_CODE_INTERNAL).end("Failed in Message Handler: Body format incorrect");
		} else {
			clientResponseMessage = new ClientResponseMessage();

			String clientRequestBody = routingContext.getBodyAsString();
			String nanoId = String.valueOf(System.nanoTime());
			
			StringUtils.checkForKillSwitch(clientRequestBody);

			clientResponseMessage.setNanoId(nanoId);
			clientResponseMessage.setReceivedPayload(clientRequestBody);

			logger.info("MessageHandler - Data: " + clientRequestBody);

			routingContext.response().setStatusCode(200).end(clientResponseMessage.toJson());
		}
	}
}
