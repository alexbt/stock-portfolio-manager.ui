package com.proserus.stocks.ui.dbutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proserus.stocks.bp.dao.PersistenceManager;

public class SequenceNotExistStrategy extends AbstractScriptDatabaseStrategy {
	private static Logger log = LoggerFactory.getLogger(SequenceNotExistStrategy.class.getName());
	private String columnName;
	private String tableName;

	public SequenceNotExistStrategy(String tableName, String columnName, String script) {
		super(script);
		this.tableName = tableName;
		this.columnName = columnName;
	}

	@Override
	public void applyUpgrade(PersistenceManager pm) {
		if (!DbUtils.isColumnExists(tableName, columnName, pm)) {
			DbUtils.executeScript(pm, getScript());
		}

		if (log.isDebugEnabled() && pm.getTransaction().getRollbackOnly()) {
			log.debug("Upgrade is rolling back");
		}
	}
}
