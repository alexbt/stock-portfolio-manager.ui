package com.proserus.stocks.ui.dbutils;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proserus.stocks.bp.dao.PersistenceManager;

public class YearPriceStrategy implements DatabaseStrategy {
	private static Logger LOGGER = LoggerFactory.getLogger(YearPriceStrategy.class.getName());

	@Override
	public void applyUpgrade(PersistenceManager pm) {
		pm.createNativeQuery("ALTER TABLE PUBLIC.HISTORICALPRICE ADD COLUMN YearValue INTEGER").executeUpdate();
		Query q = pm.createNamedQuery("historicalPriceUpdate.findAll");
		List<HistoricalPriceForUpdate> historicalPrices = q.getResultList();
		for (HistoricalPriceForUpdate historicalPriceForUpdate : historicalPrices) {
			q = pm.createNativeQuery("update HISTORICALPRICE set YearValue=" + historicalPriceForUpdate.getYear().getYear() + " where id="
					+ historicalPriceForUpdate.getId());
			q.executeUpdate();
		}
		pm.createNativeQuery("ALTER TABLE PUBLIC.HISTORICALPRICE DROP COLUMN Year").executeUpdate();
		pm.createNativeQuery("ALTER TABLE PUBLIC.HISTORICALPRICE ALTER COLUMN YearValue RENAME TO Year").executeUpdate();

		if (LOGGER.isDebugEnabled() && pm.getTransaction().getRollbackOnly()) {
			LOGGER.debug("Upgrade is rolling back");
		}
	}

}
