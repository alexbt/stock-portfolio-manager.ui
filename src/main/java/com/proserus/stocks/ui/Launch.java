package com.proserus.stocks.ui;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.persistence.PersistenceException;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.google.inject.Guice;
import com.proserus.stocks.bo.utils.PathUtils;
import com.proserus.stocks.ui.controller.ViewControllers;
import com.proserus.stocks.ui.view.general.DbChooser;

public class Launch {
	private static Logger LOGGER;
	static{
	    System.setProperty("installation.folder", PathUtils.getInstallationFolder());
	    LOGGER = Logger.getLogger(Launch.class);
	}
	
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
		try {
			LOGGER.info("**********************************");
			LOGGER.info("Starting...");
			LOGGER.info("**********************************");
			
			EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
			queue.push(new EventQueueProxy());
			
			Guice.createInjector(new SwingModule()).getInstance(ViewControllers.class);
			
			ViewControllers.getController().checkNewVersion();
			ViewControllers.getWindow().start();
			new DbChooser();
			ViewControllers.getController().checkDatabaseDuplicate();
			
		} catch(PersistenceException e){
			JOptionPane
			.showMessageDialog(
					null,
					"Database error!\n"
							+ "There seem to be an error with the database\n"
							+ "If you have issues creating entries, you may want to start from scratch\n"
							+ "Choose 'File -> Delete current portfolio'",
					"Database error!",
					JOptionPane.INFORMATION_MESSAGE, null);
			LOGGER.fatal("Database error", e);
			throw e;
		}
		catch (Throwable e) {
			LOGGER.fatal("Throwable", e);
			if(e instanceof RuntimeException){
				throw (RuntimeException)e;
			}
		}
	}

}
