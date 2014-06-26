package com.proserus.stocks.ui.dbutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proserus.stocks.bp.dao.PersistenceManager;

public class ConstraintExistStrategy extends AbstractScriptDatabaseStrategy {
	private static Logger log = LoggerFactory.getLogger(ConstraintExistStrategy.class.getName());
	private String constraintName;

	public ConstraintExistStrategy(String constraintName, String script) {
		super(script);
		this.constraintName = constraintName;
	}

	@Override
	public void applyUpgrade(PersistenceManager pm) {
		if (DbUtils.isConstraintExist(constraintName, pm)) {
			DbUtils.executeScript(pm, getScript());
		}

		if (log.isDebugEnabled() && pm.getTransaction().getRollbackOnly()) {
			log.debug("Upgrade is rolling back");
		}
	}
}
