package com.proserus.stocks;

import org.apache.log4j.Logger;

import com.google.inject.Guice;
import com.proserus.stocks.view.common.ViewControllers;

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
			ViewControllers controllers = Guice.createInjector(new SwingModule()).getInstance(ViewControllers.class);
			controllers.getWindow().start();
			controllers.getController().refresh();
		} catch (RuntimeException e) {
			LOGGER.fatal(e);
			LOGGER.fatal(e.getCause());
			throw e;
		} catch (Error e) {
			LOGGER.fatal(e);
			LOGGER.fatal(e.getCause());
			throw e;
		} catch (Throwable e) {
			LOGGER.fatal(e);
			LOGGER.fatal(e.getCause());
		}
	}

}
