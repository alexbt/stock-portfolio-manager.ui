package com.proserus.stocks.ui.dbutils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.persistence.PersistenceException;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proserus.stocks.bp.dao.PersistenceManager;

public class DbUtils {
	private final static Logger LOGGER = LoggerFactory.getLogger(DbUtils.class);

	public static void executeScript(PersistenceManager pm, String script) {
		String s = new String();
		StringBuffer sb = new StringBuffer();

		try {

			InputStream is = DbUtils.class.getClassLoader().getResourceAsStream(script);

			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			while ((s = br.readLine()) != null) {
				sb.append(s);
				s = s.trim();
				if (!s.isEmpty() && !s.startsWith("--")) {
					try {
						LOGGER.debug("Executing {}", new Object[] { s });
						pm.createNativeQuery(s).executeUpdate();
						LOGGER.debug("Executed successfully");
					} catch (PersistenceException e) {
						if (!e.getCause().getCause().toString().toLowerCase().contains("already exists")) {
							LOGGER.info("A problem occured executing {}: {}", new Object[] { s, e.getCause().getCause() });
							pm.getTransaction().setRollbackOnly();
						}
					} catch (RuntimeException e) {
						LOGGER.error("Failed to execute {}", new Object[] { s }, e);
						pm.getTransaction().setRollbackOnly();
						throw e;
					}

				}
			}
			br.close();

		} catch (Exception e) {
			pm.getTransaction().setRollbackOnly();
		}
	}

	static public boolean isColumnExpectedDatatype(String table, String column, String expectedType, PersistenceManager pm) {
		Validate.notEmpty(table);
		Validate.notEmpty(column);
		Validate.notEmpty(expectedType);
		Validate.notNull(pm);

		String str = "SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS where table_schema ='PUBLIC' and TABLE_NAME='" + table
				+ "' and COLUMN_NAME ='" + column + "'";

		List<?> list = pm.createNativeQuery(str).getResultList();

		return !list.isEmpty() && list.get(0).toString().toUpperCase().equals(expectedType.toUpperCase());
	}

	static public boolean isColumnExists(String table, String column, PersistenceManager pm) {
		Validate.notEmpty(table);
		Validate.notEmpty(column);
		Validate.notNull(pm);

		String str = "SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS where table_schema ='PUBLIC' and TABLE_NAME='" + table
				+ "' and COLUMN_NAME ='" + column + "'";

		List<?> list = pm.createNativeQuery(str).getResultList();

		return !list.isEmpty();
	}

	static public boolean isTableExists(String table, PersistenceManager pm) {
		table = table.toUpperCase();

		String str = "SELECT * FROM INFORMATION_SCHEMA.TABLES where table_schema ='PUBLIC' and TABLE_NAME='" + table + "'";

		return pm.createNativeQuery(str).getResultList().isEmpty() == false;
	}

	public static boolean isConstraintExist(String constraintName, PersistenceManager pm) {
		constraintName = constraintName.toUpperCase();
		String query = "select * from INFORMATION_SCHEMA.TABLE_CONSTRAINTS where constraint_name='" + constraintName + "'";

		return pm.createNativeQuery(query).getResultList().isEmpty() == false;
	}
}
