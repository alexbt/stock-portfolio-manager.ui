package com.proserus.stocks.ui.dbutils;

import org.apache.log4j.Logger;

import com.proserus.stocks.bp.dao.PersistenceManager;

public class ColumnNotExistStrategy extends AbstractScriptDatabaseStrategy {
	private static Logger log = Logger.getLogger(ColumnNotExistStrategy.class.getName());
	private String columnName;
	private String tableName;

	public ColumnNotExistStrategy(String tableName, String columnName, String script){
		super(script);
		this.tableName = tableName;
		this.columnName = columnName;
	}
	
	@Override
	public void applyUpgrade(PersistenceManager pm) {
		if(!DbUtils.isColumnExists(tableName, columnName, pm)){
			DbUtils.executeScript(pm, getScript());
		}
		
		if(log.isDebugEnabled() && pm.getTransaction().getRollbackOnly()){
			log.debug("Upgrade is rolling back");
		}
	}
}
