package com.proserus.stocks.ui.dbutils;

import javax.persistence.PersistenceException;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.proserus.stocks.bo.common.BoBuilder;
import com.proserus.stocks.bo.common.DBVersion;
import com.proserus.stocks.bp.dao.PersistenceManager;
import com.proserus.stocks.bp.services.DatabaseVersionningBp;
import com.proserus.stocks.ui.model.DBVersionImpl;

@Singleton
public class DatabaseVersionningBpImpl implements DatabaseVersionningBp {
	private static Logger log = Logger.getLogger(DatabaseVersionningBpImpl.class.getName());

	@Inject
	private BoBuilder boBuilder;

	@Inject
	private PersistenceManager persistenceManager;

	@Override
	public DBVersion check() {
		log.debug("Verifying the current version");
		DBVersion version = null;

		try {
			version = persistenceManager.getEntityManager().find(DBVersionImpl.class, 1);

			if (version == null) {
				version = boBuilder.getVersion();
				version.setDatabaseVersion(DatabaseUpdagreEnum.CREATE_TABLES_VERSION.getVersion());
			}

			else if (!DbUtils.tableExist("TRANSACTION", persistenceManager)) {
				version.setDatabaseVersion(DatabaseUpdagreEnum.CREATE_TABLES_TRANSACTION.getVersion());
			}

			else if (!DbUtils.tableExist("SYMBOL", persistenceManager)) {
				version.setDatabaseVersion(DatabaseUpdagreEnum.CREATE_TABLES_SYMBOL.getVersion());
			}

			else if (!DbUtils.tableExist("LABEL", persistenceManager)) {
				version.setDatabaseVersion(DatabaseUpdagreEnum.CREATE_TABLES_LABEL.getVersion());
			}

			else if (!DbUtils.tableExist("HISTORICALPRICE", persistenceManager)) {
				version.setDatabaseVersion(DatabaseUpdagreEnum.CREATE_TABLES_HISTORICALPRICE.getVersion());
			}

			else if (!DbUtils.tableExist("TRANSACTION_LABEL", persistenceManager)) {
				version.setDatabaseVersion(DatabaseUpdagreEnum.CREATE_TABLES_TRANSACTION_LABEL.getVersion());
			}

			else if (!DbUtils.tableExist("SYMBOL_PRICES", persistenceManager)) {
				version.setDatabaseVersion(DatabaseUpdagreEnum.CREATE_TABLES_SYMBOL_PRICES.getVersion());
			}

			else if (!DbUtils.sequenceExist("HIBERNATE_SEQUENCE", persistenceManager)) {
				version.setDatabaseVersion(DatabaseUpdagreEnum.CREATE_TABLES_HIBERNATE_SEQUENCE.getVersion());
			}

		} catch (PersistenceException e2) {
			log.debug("database does not exist");
			version = boBuilder.getVersion();
			version.setDatabaseVersion(DatabaseUpdagreEnum.CREATE_DATABASE.getVersion());
		}

		assert version != null;
		return version;
	}

	@Override
	public void upgrade(DBVersion version) {
		assert version.getDatabaseVersion() < DatabaseUpdagreEnum.getLatestVersion();
		log.debug("Upgrading from Version " + version.getDatabaseVersion() + " to " + DatabaseUpdagreEnum.getLatestVersion());

		boolean firstTime = (version.getDatabaseVersion() == DatabaseUpdagreEnum.CREATE_DATABASE.getVersion());
		if (!firstTime) {
			int n = JOptionPane.showConfirmDialog(null, "This new version will perform an update on the data format.\n "
			        + "We strongly suggest you manually backup your the directory 'data' under the StockPortfolio folder before continuing.\n"
			        + "Do you want to perform the upgrade now ?", "Upgrading data format", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (n != JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}
		persistenceManager.getEntityManager().getTransaction().begin();

		while (version.getDatabaseVersion() < DatabaseUpdagreEnum.getLatestVersion()) {
			for (DatabaseStrategy strategy : DatabaseUpdagreEnum.getVersionStrategies(version.getDatabaseVersion())) {
				strategy.applyUpgrade(persistenceManager, version);
			}
			version.incrementVersion();
		}
		DatabaseUpdagreEnum.PERSIST_LATEST_VERSION.getStrategy().applyUpgrade(persistenceManager, version);

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
}
