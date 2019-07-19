package com.monarch.apps.vertx.server.verticles;

import java.util.logging.Logger;

import com.monarch.apps.vertx.server.messages.TestRequestHandler;
import com.monarch.tools.Constants;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.TimeoutHandler;

public class TestServerVerticle extends AbstractVerticle {
	private Logger logger = Logger.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	/**
	 * SERVER And route end-point is set. Configurations for server initialisation
	 * should also be set here.
	 */
	@Override
	public void start(Future<Void> future) {

		int serverPort = config().getInteger(Constants.BROKER_SERVER_PORT_KEY, Constants.BROKER_SERVER_PORT);

		Router router = Router.router(vertx);
		router.route().handler(LoggerHandler.create());
		router.route().handler(BodyHandler.create());
		router.route().handler(TimeoutHandler.create(120000L, 408));
		router.post(Constants.BROKER_MESSAGE_ENDPOINT).handler(new TestRequestHandler());

		vertx.createHttpServer().requestHandler(router).listen(serverPort, clientRequest -> {
			if (clientRequest.succeeded()) {
				logger.info("Successfully started Server verticle: " + serverPort);
				future.complete();

			} else {
				logger.warning("Failed to start Server verticle");
				future.fail(clientRequest.cause());
			}
			
		});
	}

}
