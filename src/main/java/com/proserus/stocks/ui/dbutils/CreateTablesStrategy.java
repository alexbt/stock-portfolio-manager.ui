package com.proserus.stocks.ui.dbutils;

import org.apache.log4j.Logger;

import com.proserus.stocks.bo.common.DBVersion;
import com.proserus.stocks.bp.dao.PersistenceManager;


public class CreateTablesStrategy extends AbstractScriptDatabaseStrategy {
	private static final String SCRIPT_PREFIX = "CREATE_TABLE_";
	public static final String SCRIPT_SUFFIX = ".sql";
	private static Logger log = Logger.getLogger(CreateTablesStrategy.class.getName());

	private String table = null;
	
	public CreateTablesStrategy(String table){
		super(SCRIPT_PREFIX + table + SCRIPT_SUFFIX);
		this.table = table;
	}
	
	@Override
    public void applyUpgrade(PersistenceManager pm, DBVersion version) {
		log.debug("will rollback?: " + pm.getEntityManager().getTransaction().getRollbackOnly());
		log.debug("Creating the tables");
		
		if(!DbUtils.tableExist(table, pm)){
			DbUtils.executeScript(pm.getEntityManager(),  getScript());
		}
		log.debug("will rollback?: " + pm.getEntityManager().getTransaction().getRollbackOnly());
    }
}

