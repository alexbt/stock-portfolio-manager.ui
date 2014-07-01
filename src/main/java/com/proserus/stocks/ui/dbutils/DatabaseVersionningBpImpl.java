package com.proserus.stocks.ui.dbutils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.persistence.PersistenceException;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.proserus.stocks.bo.common.BoBuilder;
import com.proserus.stocks.bo.common.DBVersion;
import com.proserus.stocks.bo.utils.PathUtils;
import com.proserus.stocks.bp.dao.PersistenceManager;
import com.proserus.stocks.bp.services.DatabaseVersionningBp;
import com.proserus.stocks.ui.view.general.Version;

@Singleton
public class DatabaseVersionningBpImpl implements DatabaseVersionningBp {

	private final static Logger LOGGER = LoggerFactory.getLogger(DatabaseVersionningBpImpl.class);

	private static final String LATESTVERSION_PROPERTY = "latestversion";

	public static final String LATEST_VERSION_URL = "http://stock-portfolio-manager.googlecode.com/hg/latestversion";

	private boolean ignorePopop = false;

	public void setIgnorePopop(boolean ignorePopop) {
		this.ignorePopop = ignorePopop;
	}

	@Inject
	private BoBuilder boBuilder;

	@Inject
	private PersistenceManager persistenceManager;

	public void setPersistenceManager(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}

	@Override
	public DBVersion retrieveCurrentVersion() {
		LOGGER.debug("Verifying the current version");
		DBVersion version = null;

		try {
			version = (DBVersion) persistenceManager.createNamedQuery("version.find").getSingleResult();
			LOGGER.debug("database version: {}", new Object[] { version.getDatabaseVersion() });
		} catch (PersistenceException e2) {
		}

		if (version == null) {
			version = boBuilder.getVersion();
			version.setDatabaseVersion(DatabaseUpgradeEnum.VERSION_0.getVersion());
		}

		assert version != null;
		return version;
	}

	@Override
	public void upgrade(DBVersion version) {

		LOGGER.debug("Upgrading from Version {} to {}",
				new Object[] { version.getDatabaseVersion(), DatabaseUpgradeEnum.getLatestVersion() });
		boolean isFirstTime = version.getDatabaseVersion() == DatabaseUpgradeEnum.VERSION_0.getVersion();
		if (!isFirstTime && !ignorePopop) {
			int n = JOptionPane.showConfirmDialog(null, "This new version will perform an update on the data format.\n "
					+ "If you wish to manually backup your data first, please click 'No'.\n"
					+ "The most reliable way to backup your data is to make a copy of the 'data' directory\n\n"
					+ "Do you want to perform the upgrade now and launch the application ?", "Upgrading data format",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (n != JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}
		int initialVersion = version.getDatabaseVersion();
		try {
			for (DatabaseUpgradeEnum dbEnu : DatabaseUpgradeEnum.values()) {
				persistenceManager.getTransaction().begin();
				LOGGER.debug("Upgrading to {}", new Object[] { dbEnu.getVersion() });
				if (version.getDatabaseVersion() < dbEnu.getVersion() && !persistenceManager.getTransaction().getRollbackOnly()) {
					for (DatabaseStrategy dbStrategy : dbEnu.getStrategies()) {
						dbStrategy.applyUpgrade(persistenceManager);
						if (persistenceManager.getTransaction().getRollbackOnly()) {
							LOGGER.info("A problem occured in {}", new Object[] { dbStrategy });
							break;
						}
					}
					version.setDatabaseVersion(dbEnu.getVersion());
					persistenceManager.persist(version);
				}
				persistenceManager.getTransaction().commit();
			}
		} catch (Throwable e) {
			if (persistenceManager.getTransaction().isActive()) {
				LOGGER.debug("Exception while upgrading database to {}", new Object[] { version }, e);
				persistenceManager.getTransaction().setRollbackOnly();
			}
		}

		if (persistenceManager.getTransaction().isActive() && persistenceManager.getTransaction().getRollbackOnly()) {
			persistenceManager.getTransaction().rollback();
			LOGGER.debug("Rolling back database upgrade from version {} back to {}", new Object[] { version, initialVersion });
			throw new AssertionError();

			// if (!isFirstTime && !ignorePopop) {
			// JOptionPane
			// .showMessageDialog(
			// null,
			// "Upgrade failed!\n"
			// +
			// "The application will now exit.\nPlease open a bug issue on the website and provide the 'traces.log'\n"
			// + "http://code.google.com/p/stock-portfolio-manager/issues/list",
			// "Upgrade failed!",
			// JOptionPane.INFORMATION_MESSAGE, null);
			// }
		} else {
			LOGGER.debug("Version check and upgrade is successful");
			if (!isFirstTime && !ignorePopop) {
				JOptionPane.showMessageDialog(null, "Upgrade completed successfully!", "Upgrade completed!",
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
		InputStream fis = null;
		try {

			fis = new FileInputStream(PathUtils.getInstallationFolder() + "/version.properties");
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
			fos = new FileOutputStream(PathUtils.getInstallationFolder() + "/version.properties");
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
			if (versionFromWeb != null) {
				value = Double.parseDouble(versionFromWeb);
			}

		} catch (MalformedURLException e) {
		} catch (IOException e) {
		} catch (NumberFormatException e) {

		}

		return value;
	}
}
