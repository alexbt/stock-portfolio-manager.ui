package com.proserus.stocks.ui.dbutils;

import org.apache.log4j.Logger;

import com.proserus.stocks.bo.common.DBVersion;
import com.proserus.stocks.bp.dao.PersistenceManager;

public class HibernateSequenceStrategy extends AbstractScriptDatabaseStrategy {
	private static Logger log = Logger.getLogger(DropFirstSectorStrategy.class.getName());

	public HibernateSequenceStrategy(){
		super("HIBERNATE_SEQUENCE.sql");
	}
	
	@Override
	public void applyUpgrade(PersistenceManager pm, DBVersion version) {
		if(!DbUtils.sequenceExist("HIBERNATE_SEQUENCE", pm)){
			DbUtils.executeScript(pm.getEntityManager(), getScript());
		}
		
		if(log.isDebugEnabled() && pm.getEntityManager().getTransaction().getRollbackOnly()){
			log.debug("Upgrade is rolling back");
		}
	}
}
