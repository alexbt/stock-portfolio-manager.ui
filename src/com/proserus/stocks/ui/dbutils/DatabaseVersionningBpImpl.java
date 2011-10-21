package com.proserus.stocks.ui.dbutils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.persistence.PersistenceException;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.proserus.stocks.bo.common.BoBuilder;
import com.proserus.stocks.bo.common.DBVersion;
import com.proserus.stocks.bp.dao.PersistenceManager;
import com.proserus.stocks.bp.services.DatabaseVersionningBp;
import com.proserus.stocks.ui.view.general.Version;

@Singleton
public class DatabaseVersionningBpImpl implements DatabaseVersionningBp {
	private static final String LATESTVERSION_PROPERTY = "latestversion";

	public static final String LATEST_VERSION_URL = "http://stock-portfolio-manager.googlecode.com/hg/latestversion";

	private static Logger log = Logger.getLogger(DatabaseVersionningBpImpl.class.getName());

	@Inject
	private BoBuilder boBuilder;

	@Inject
	private PersistenceManager persistenceManager;

	public void setPersistenceManager(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}

	@Override
	public DBVersion retrieveCurrentVersion() {
		log.debug("Verifying the current version");
		DBVersion version = null;

		try {

			try {
				version = (DBVersion) persistenceManager.getEntityManager().createNamedQuery("version.find").getSingleResult();
			} catch (PersistenceException e2) {

			}

			if (version == null) {
				version = boBuilder.getVersion();

				if (!DbUtils.tableExist("TRANSACTION", persistenceManager)) {
					version.setDatabaseVersion(DatabaseUpgradeEnum.CREATE_TABLES_TRANSACTION.getVersion());
				}

				else if (!DbUtils.tableExist("SYMBOL", persistenceManager)) {
					version.setDatabaseVersion(DatabaseUpgradeEnum.CREATE_TABLES_SYMBOL.getVersion());
				}

				else if (!DbUtils.tableExist("LABEL", persistenceManager)) {
					version.setDatabaseVersion(DatabaseUpgradeEnum.CREATE_TABLES_LABEL.getVersion());
				}

				else if (!DbUtils.tableExist("HISTORICALPRICE", persistenceManager)) {
					version.setDatabaseVersion(DatabaseUpgradeEnum.CREATE_TABLES_HISTORICALPRICE.getVersion());
				}

				else if (!DbUtils.tableExist("TRANSACTION_LABEL", persistenceManager)) {
					version.setDatabaseVersion(DatabaseUpgradeEnum.CREATE_TABLES_TRANSACTION_LABEL.getVersion());
				}

				else if (!DbUtils.tableExist("SYMBOL_PRICES", persistenceManager)) {
					version.setDatabaseVersion(DatabaseUpgradeEnum.CREATE_TABLES_SYMBOL_PRICES.getVersion());
				}
				
				else if (!DbUtils.tableExist("VERSION", persistenceManager)) {
					version.setDatabaseVersion(DatabaseUpgradeEnum.CREATE_TABLES_VERSION.getVersion());
				}
				
				else if (!DbUtils.sequenceExist("HIBERNATE_SEQUENCE", persistenceManager)) {
					version.setDatabaseVersion(DatabaseUpgradeEnum.CREATE_TABLES_HIBERNATE_SEQUENCE.getVersion());
				}

				else {
					assert false : "Should not be here... but let's take no chances...";
					version.setDatabaseVersion(DatabaseUpgradeEnum.CREATE_TABLES_TRANSACTION.getVersion());
				}
			}

		} catch (PersistenceException e2) {
			assert false : "Should not be here... but let's take no chances...";
			log.debug("database does not exist");
			version.setDatabaseVersion(DatabaseUpgradeEnum.CREATE_TABLES_TRANSACTION.getVersion());
		}

		assert version != null;
		return version;
	}

	@Override
	public void upgrade(DBVersion version) {
		assert version.getDatabaseVersion() < DatabaseUpgradeEnum.getLatestVersion();
		log.debug("Upgrading from Version " + version.getDatabaseVersion() + " to " + DatabaseUpgradeEnum.getLatestVersion());

		boolean firstTime = (version.getDatabaseVersion() == DatabaseUpgradeEnum.getInitialVersion());
		if (!firstTime) {
			int n = JOptionPane.showConfirmDialog(null, "This new version will perform an update on the data format.\n "
			        + "If you wish to manually backup your data first, please click 'No'.\n" +
			        		"The most reliable way to backup your data is to make a copy of the 'data' directory\n\n"
			        + "Do you want to perform the upgrade now and launch the application ?", "Upgrading data format", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (n != JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}
		persistenceManager.getEntityManager().getTransaction().begin();

		while (version.getDatabaseVersion() < DatabaseUpgradeEnum.getLatestVersion()) {
			for (DatabaseStrategy strategy : DatabaseUpgradeEnum.getVersionStrategies(version.getDatabaseVersion())) {
				strategy.applyUpgrade(persistenceManager, version);
			}
			version.incrementVersion();
		}
		DatabaseUpgradeEnum.PERSIST_LATEST_VERSION.getStrategy().applyUpgrade(persistenceManager, version);

		if (persistenceManager.getEntityManager().getTransaction().getRollbackOnly()) {
			log.debug("Version check and upgrade is rolledback from version " + version.getDatabaseVersion());
			persistenceManager.getEntityManager().getTransaction().rollback();
			if (!firstTime) {
				JOptionPane.showMessageDialog(null, "Upgrade failed!\n"
				        + "The application will now exit.\nPlease open a bug issue on the website and provide the 'traces.log'\n"
				        + "http://code.google.com/p/stock-portfolio-manager/issues/list", "Upgrade failed!", JOptionPane.INFORMATION_MESSAGE, null);
			}
		} else {
			log.debug("Version check and upgrade is successful");
			persistenceManager.getEntityManager().getTransaction().commit();
			if (!firstTime) {
				JOptionPane.showMessageDialog(null, "Upgrade completed successfully!", "Upgrade completed successfully!",
				        JOptionPane.INFORMATION_MESSAGE, null);
			}
		}
	}

	@Override
	public boolean isNewerVersion(double latestVersion) {
		Properties pro = new Properties();

		loadProperty(pro);

		if (pro.getProperty(LATESTVERSION_PROPERTY) == null) {
			writeVersion(pro, Version.VERSION);
			loadProperty(pro);
		}

		if (pro.getProperty(LATESTVERSION_PROPERTY) != null) {
			double currentVersion = Double.parseDouble(pro.getProperty(LATESTVERSION_PROPERTY));

			if (currentVersion < latestVersion) {
				return true;
			}
		}

		return false;
	}

	private void loadProperty(Properties pro) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("version.properties");
			pro.load(fis);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}

		}
	}

	public void writeVersion(Properties pro, String version) {
		pro.setProperty(LATESTVERSION_PROPERTY, version);

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream("version.properties");
			pro.store(fos, "");
			fos.flush();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}
	}

	@Override
	public Double retrieveLatestVersion(String url) {
		Double value = null;

		try {
			URL oracle = new URL(url);
			BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
			String versionFromWeb;

			versionFromWeb = in.readLine();
			value = Double.parseDouble(versionFromWeb);

		} catch (MalformedURLException e) {
		} catch (IOException e) {
		} catch (NumberFormatException e) {

		}

		return value;
	}
}
