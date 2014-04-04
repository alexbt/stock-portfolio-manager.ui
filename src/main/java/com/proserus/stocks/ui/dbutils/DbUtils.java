package com.proserus.stocks.ui.dbutils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.proserus.stocks.bp.dao.PersistenceManager;

public class DbUtils {
	private static Logger log = Logger.getLogger(DbUtils.class.getName());

	public static void executeScript(EntityManager em, String script) {
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
						log.debug("Executing " + s);
						em.createNativeQuery(s).executeUpdate();
						log.debug("Executed successfully");
					} catch (RuntimeException e) {
						log.error("Failed to execute " + s, e);
						em.getTransaction().setRollbackOnly();
						throw e;
					}

				}
			}
			br.close();

		} catch (Exception e) {
			em.getTransaction().setRollbackOnly();
		}
	}

	static public boolean columnExist(String table, String column, PersistenceManager pm) {
		table = table.toUpperCase();
		column = column.toUpperCase();

		String str = "SELECT * FROM INFORMATION_SCHEMA.COLUMNS where table_schema ='PUBLIC' and TABLE_NAME='" + table + "' and COLUMN_NAME ='"
		        + column + "'";

		return pm.getEntityManager().createNativeQuery(str).getResultList().isEmpty() == false;
	}
	
	static public boolean tableExist(String table, PersistenceManager pm) {
		table = table.toUpperCase();

		String str = "SELECT * FROM INFORMATION_SCHEMA.TABLES where table_schema ='PUBLIC' and TABLE_NAME='" + table + "'";

		return pm.getEntityManager().createNativeQuery(str).getResultList().isEmpty() == false;
	}
	
	static public boolean sequenceExist(String sequence, PersistenceManager pm) {
		sequence = sequence.toUpperCase();

		String str = "SELECT * FROM INFORMATION_SCHEMA.SEQUENCES where SEQUENCE_SCHEMA ='PUBLIC' and SEQUENCE_NAME='" + sequence + "'";

		return pm.getEntityManager().createNativeQuery(str).getResultList().isEmpty() == false;
	}
}
