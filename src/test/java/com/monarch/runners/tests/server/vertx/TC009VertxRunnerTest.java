package com.monarch.runners.tests.server.vertx;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.monarch.apps.vertx.server.verticles.TestServerVerticle;
import com.monarch.tools.utils.StringUtils;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * Test relies on a KillSwitch flag to be set in the target folder. The file
 * will be read and the Vertx server will be taken down if the kill switch is
 * found in any of the requests. <br>
 * 
 * Test will deploy a server and responde to any client connection with the payload and a timestamp field.
 * If the body is null an error is thrown.
 * If the body contains the killSwitch it will undeploy and close vertx.
 * 
 * @author voicu.turcu
 *
 */
@RunWith(JUnit4.class)
public class TC009VertxRunnerTest {

	private Vertx vertx;

	private int waitTimeCycleMS = 10000;

	@Before
	public void dataSetup() {
		// before - clean up
		StringUtils.cleanKillSwitch();

		// setup vertx
		VertxOptions options = new VertxOptions();
		options.setBlockedThreadCheckInterval(361000);

		vertx = Vertx.vertx(options);
	}

	@Test
	public void tc009VertxRunnerTest() {
		// Servers deploy
		vertx.deployVerticle(new TestServerVerticle());
	}

	@After
	public void cleanUp() {
		// self clean up - if the killSwitch message is received the server is closed.
		do {
			if (StringUtils.killSwitchExists())
				for (String deployIdNow : vertx.deploymentIDs()) {
					vertx.undeploy(deployIdNow);
				}
			try {
				Thread.sleep(waitTimeCycleMS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} while (!StringUtils.killSwitchExists());
		vertx.close();
	}
}
