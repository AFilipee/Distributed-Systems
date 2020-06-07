package com.forkexec.pts.ws.cli;

import javax.xml.ws.Response;
import java.util.concurrent.ExecutionException;
import com.forkexec.pts.ws.*;
import javax.xml.ws.AsyncHandler;

/**
 * Client application.
 * 
 * Looks for Points using UDDI and arguments provided
 */
public class PointsClientApp {

	static boolean received = false;

	public static void main(String[] args) throws Exception {
		// Check arguments.
		if (args.length == 0) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + PointsClientApp.class.getName() + " wsURL OR uddiURL wsName");
			return;
		}
		String uddiURL = null;
		String wsName = null;
		String wsURL = null;
		if (args.length == 1) {
			wsURL = args[0];
		} else if (args.length >= 2) {
			uddiURL = args[0];
			wsName = args[1];
		}

		// Create client.
		PointsClient client = null;

		if (wsURL != null) {
			System.out.printf("Creating client for server at %s%n", wsURL);
			client = new PointsClient(wsURL);
		} else if (uddiURL != null) {
			System.out.printf("Creating client using UDDI at %s for server with name %s%n", uddiURL, wsName);
			client = new PointsClient(uddiURL, wsName);
		}

		/** [====== Remote invocation methods =======] */

		/* Lab Web Services III - Polling */

		/*
		 * String userEmail = "user@example.com"; client.activateUser(userEmail); //
		 * asynchronous call with polling Response<PointsBalanceResponse> response =
		 * client.pointsBalanceAsync(userEmail);
		 * 
		 * while (!response.isDone()) { Thread.sleep(10 milliseconds );
		 * System.out.print("."); System.out.flush(); }
		 * 
		 * try { System.out.println("(Polling) asynchronous call result: " +
		 * response.get().getReturn()); } catch (ExecutionException e) {
		 * System.out.println("Caught execution exception.");
		 * System.out.print("Cause: "); System.out.println(e.getCause()); }
		 */

	}
}
