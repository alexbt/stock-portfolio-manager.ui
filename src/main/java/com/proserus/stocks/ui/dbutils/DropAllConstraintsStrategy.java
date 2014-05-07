package com.proserus.stocks.ui.dbutils;

import java.util.List;

import org.apache.log4j.Logger;

import com.proserus.stocks.bp.dao.PersistenceManager;

public class DropAllConstraintsStrategy implements DatabaseStrategy {
	private static Logger log = Logger.getLogger(DropAllConstraintsStrategy.class.getName());
	
	private enum CONSTRAINT_TYPES {
		FOREIGN_KEY("FOREIGN KEY"), UNIQUE("UNIQUE"), PRIMARY_KEY("PRIMARY KEY"), CHECK("CHECK");
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
					pm.createNativeQuery(command).executeUpdate();
				}
			}
		}
		if(log.isDebugEnabled() && pm.getTransaction().getRollbackOnly()){
			log.debug("Upgrade is rolling back");
		}
	}

}
