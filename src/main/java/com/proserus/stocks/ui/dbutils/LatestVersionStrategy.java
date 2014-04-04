package com.proserus.stocks.ui.dbutils;

import org.apache.log4j.Logger;

import com.proserus.stocks.bo.common.DBVersion;
import com.proserus.stocks.bp.dao.PersistenceManager;

public class LatestVersionStrategy implements DatabaseStrategy{
	private static Logger log = Logger.getLogger(LatestVersionStrategy.class.getName());
	
	@Override
    public void applyUpgrade(PersistenceManager pm, DBVersion version) {
		pm.persist(version);

		if(log.isDebugEnabled() && pm.getEntityManager().getTransaction().getRollbackOnly()){
			log.debug("Upgrade is rolling back");
		}
    }

}
