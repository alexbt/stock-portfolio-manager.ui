package com.proserus.stocks.ui.dbutils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proserus.stocks.bp.dao.PersistenceManager;

public class DropAllConstraintsStrategy implements DatabaseStrategy {
	private static Logger LOGGER = LoggerFactory.getLogger(DropAllConstraintsStrategy.class.getName());

	private enum CONSTRAINT_TYPES {
		CHECK("CHECK"), FOREIGN_KEY("FOREIGN KEY"), UNIQUE("UNIQUE"), PRIMARY_KEY("PRIMARY KEY");
		private String name;

		private CONSTRAINT_TYPES(String name) {
			this.name = name;
		}
	}

	@Override
	public void applyUpgrade(PersistenceManager pm) {
		@SuppressWarnings("unchecked")
		List<Object> list = pm.createNativeQuery("select * from INFORMATION_SCHEMA.TABLE_CONSTRAINTS").getResultList();

		for (CONSTRAINT_TYPES constraintType : CONSTRAINT_TYPES.values()) {
			for (Object o : list) {
				String type = (String) ((Object[]) o)[3];
				String schema = (String) ((Object[]) o)[1];
				if (type.equals(constraintType.name) && schema.equals("PUBLIC")) {
					String constraintName = (String) ((Object[]) o)[2];
					String tableName = (String) ((Object[]) o)[6];
					String command = "alter table PUBLIC." + tableName + " drop constraint " + constraintName;
					try {
						pm.createNativeQuery(command).executeUpdate();
					} catch (RuntimeException e) {
						LOGGER.error("Error ", e);
						throw e;
					}
				}
			}
		}
		if (LOGGER.isDebugEnabled() && pm.getTransaction().getRollbackOnly()) {
			LOGGER.debug("Upgrade is rolling back");
		}
	}

}
