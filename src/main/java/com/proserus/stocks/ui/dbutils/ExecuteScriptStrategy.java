package com.proserus.stocks.ui.dbutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proserus.stocks.bp.dao.PersistenceManager;

public class ExecuteScriptStrategy extends AbstractScriptDatabaseStrategy {
	private static Logger log = LoggerFactory.getLogger(ExecuteScriptStrategy.class.getName());
	private String script;

	public ExecuteScriptStrategy(String script) {
		super(script);
		this.script = script;
	}

	@Override
	public void applyUpgrade(PersistenceManager pm) {
		log.debug("will rollback?: {}", new Object[] { pm.getTransaction().getRollbackOnly() });
		log.debug("Executing0 {}", new Object[] { script });

		DbUtils.executeScript(pm, getScript());
		log.debug("will rollback?: {}", new Object[] { pm.getTransaction().getRollbackOnly() });
	}
}
