package com.proserus.stocks.ui;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.persistence.PersistenceException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.proserus.stocks.bo.utils.PathUtils;
import com.proserus.stocks.ui.controller.ViewControllers;
import com.proserus.stocks.ui.view.general.DbChooser;

public class Launch {
	static {
		System.setProperty("installation.folder", PathUtils.getInstallationFolder());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Logger LOGGER = LoggerFactory.getLogger(Launch.class);
		LOGGER.info("**********************************");
		LOGGER.info("Starting...");
		LOGGER.info("**********************************");
		LOGGER.info("installation.folder: {}", new Object[] { PathUtils.getInstallationFolder() });
		LOGGER.info("current.folder: {}", new Object[] { PathUtils.getCurrentFolder() });

		try {
			EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
			queue.push(new EventQueueProxy());

			Guice.createInjector(new SwingModule()).getInstance(ViewControllers.class);

			ViewControllers.getController().checkNewVersion();
			ViewControllers.getWindow().start();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					new DbChooser();
				}
			});
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					new Thread() {
						@Override
						public void run() {
							ViewControllers.getController().checkDatabaseDuplicate();
						}
					}.start();
				}
			});

		} catch (PersistenceException e) {
			JOptionPane.showMessageDialog(null, "Database error!\n" + "There seem to be an error with the database\n"
					+ "If you have issues creating entries, you may want to start from scratch\n"
					+ "Choose 'File -> Delete current portfolio'", "Database error!", JOptionPane.INFORMATION_MESSAGE, null);
			LOGGER.error("Database error", e);
			throw e;
		} catch (Throwable e) {
			LOGGER.error("Unexpected Throwable", e);
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			}
		}
	}

}
