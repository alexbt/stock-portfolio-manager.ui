package com.proserus.stocks.ui;

import java.awt.EventQueue;
import java.awt.Toolkit;

import org.apache.log4j.Logger;

import com.google.inject.Guice;
import com.proserus.stocks.ui.controller.ViewControllers;

public class Launch {
	private static Logger LOGGER = Logger.getLogger("stacktrace." + Launch.class.getName());

	// Get exchange rate from web
	// Set default Currency
	// Show/Hide columns
	// Full proof list of Symbols...
	// Can add symbols from files, transaction and symbols
	// All tables editable
	// Combobox for buy,sell,div
	// Combobox for currency
	// Combobox for symbols
	// Default columns hidden at startup

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Window s;
		try {
			
			EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
			queue.push(new EventQueueProxy());
			
			ViewControllers controllers = Guice.createInjector(new SwingModule()).getInstance(ViewControllers.class);
			
			controllers.getController().checkDatabaseVersion();
			controllers.getWindow().start();
			controllers.getController().refresh();
			
			controllers.getController().checkNewVersion();
		} catch (RuntimeException e) {
			LOGGER.fatal("Runtime exception", e);
			throw e;
		} catch (Error e) {
			LOGGER.fatal("Error", e);
			throw e;
		} catch (Throwable e) {
			LOGGER.fatal("Throwable", e);
		}
	}

}
