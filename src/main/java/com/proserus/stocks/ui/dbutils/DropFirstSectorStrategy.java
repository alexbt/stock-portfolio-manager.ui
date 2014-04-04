package com.proserus.stocks.ui.dbutils;

import org.apache.log4j.Logger;

import com.proserus.stocks.bo.common.DBVersion;
import com.proserus.stocks.bp.dao.PersistenceManager;

public class DropFirstSectorStrategy extends AbstractScriptDatabaseStrategy {
	private static Logger log = Logger.getLogger(DropFirstSectorStrategy.class.getName());

	public DropFirstSectorStrategy(){
		super("DROP_SECTOR.sql");
	}
	
	@Override
	public void applyUpgrade(PersistenceManager pm, DBVersion version) {
		if(DbUtils.columnExist("SYMBOL", "SECTOR", pm)){
			DbUtils.executeScript(pm.getEntityManager(), getScript());
		}
		
		if(log.isDebugEnabled() && pm.getEntityManager().getTransaction().getRollbackOnly()){
			log.debug("Upgrade is rolling back");
		}
	}
}
