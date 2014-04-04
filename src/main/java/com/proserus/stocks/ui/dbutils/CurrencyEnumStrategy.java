package com.proserus.stocks.ui.dbutils;

import org.apache.log4j.Logger;

import com.proserus.stocks.bo.common.DBVersion;
import com.proserus.stocks.bp.dao.PersistenceManager;

public class CurrencyEnumStrategy extends AbstractScriptDatabaseStrategy {
	private static Logger log = Logger.getLogger(AddSectorStrategy.class.getName());
	
	public CurrencyEnumStrategy(){
		super("CURRENCY_ENUM.sql");
	}
	
	@Override
	public void applyUpgrade(PersistenceManager pm, DBVersion version) {
		DbUtils.executeScript(pm.getEntityManager(), getScript());

		if(log.isDebugEnabled() && pm.getEntityManager().getTransaction().getRollbackOnly()){
			log.debug("Upgrade is rolling back");
		}
    }
}
