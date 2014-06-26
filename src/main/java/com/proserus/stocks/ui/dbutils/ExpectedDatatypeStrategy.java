package com.proserus.stocks.ui.dbutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proserus.stocks.bp.dao.PersistenceManager;

public class ExpectedDatatypeStrategy extends AbstractScriptDatabaseStrategy {
	private static Logger log = LoggerFactory.getLogger(ExpectedDatatypeStrategy.class.getName());
	private String table;
	private String column;
	private String type;

	public ExpectedDatatypeStrategy(String table, String column, String type, String script) {
		super(script);
		this.table = table.toUpperCase();
		this.column = column.toUpperCase();
		this.type = type.toUpperCase();
	}

	@Override
	public void applyUpgrade(PersistenceManager pm) {
		if (DbUtils.isColumnExpectedDatatype(table, column, type, pm)) {
			DbUtils.executeScript(pm, getScript());
		}

		if (log.isDebugEnabled() && pm.getTransaction().getRollbackOnly()) {
			log.debug("Upgrade is rolling back");
		}
	}
}
