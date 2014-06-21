package com.proserus.stocks.ui.dbutils;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.proserus.stocks.bp.dao.PersistenceManager;

public class YearPriceStrategy implements DatabaseStrategy {
	private static Logger log = Logger.getLogger(YearPriceStrategy.class.getName());
	
	@Override
	public void applyUpgrade(PersistenceManager pm) {
	    pm.createNativeQuery("ALTER TABLE PUBLIC.HISTORICALPRICE ADD COLUMN YearValue INTEGER").executeUpdate();
		Query q = pm.createNamedQuery("historicalPriceUpdate.findAll");
		List<HistoricalPriceForUpdate> d = q.getResultList();
		try{
		for (HistoricalPriceForUpdate historicalPriceForUpdate : d) {
		    //historicalPriceForUpdate.setYearValue(historicalPriceForUpdate.getYear().getYear());
		    //pm.persist(historicalPriceForUpdate);
		    q = pm.createNativeQuery("update HISTORICALPRICE set YearValue="
		            + historicalPriceForUpdate.getYear().getYear() + " where id="+historicalPriceForUpdate.getId());
		    q.executeUpdate();
        }
		 } catch (PersistenceException e) {
             System.out.println(e);
         } catch (RuntimeException e) {
             System.out.println(e);
         }
		//pm.createNativeQuery("commit").executeUpdate();
		pm.createNativeQuery("ALTER TABLE PUBLIC.HISTORICALPRICE DROP COLUMN Year").executeUpdate();
		pm.createNativeQuery("ALTER TABLE PUBLIC.HISTORICALPRICE ALTER COLUMN YearValue RENAME TO Year").executeUpdate();
		
		if(log.isDebugEnabled() && pm.getTransaction().getRollbackOnly()){
			log.debug("Upgrade is rolling back");
		}
	}

}
