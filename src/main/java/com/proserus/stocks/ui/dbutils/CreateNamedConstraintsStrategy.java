package com.proserus.stocks.ui.dbutils;

import org.apache.log4j.Logger;

import com.proserus.stocks.bo.common.DBVersion;
import com.proserus.stocks.bp.dao.PersistenceManager;

public class CreateNamedConstraintsStrategy extends AbstractScriptDatabaseStrategy {
	private static Logger log = Logger.getLogger(CreateNamedConstraintsStrategy.class.getName());
	
	public CreateNamedConstraintsStrategy(){
		super( "CREATE_NAMED_CONSTRAINTS.sql");
	}
	
	@Override
	public void applyUpgrade(PersistenceManager pm, DBVersion version) {
		try{
			DbUtils.executeScript(pm.getEntityManager(), getScript());
		} catch(RuntimeException e){
			log.error("Failed to execute", e);
		}
		if(log.isDebugEnabled() && pm.getEntityManager().getTransaction().getRollbackOnly()){
			log.debug("Upgrade is rolling back");
		}
    }
}
