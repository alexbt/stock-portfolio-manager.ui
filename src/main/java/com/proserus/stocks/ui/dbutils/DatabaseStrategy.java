package com.proserus.stocks.ui.dbutils;

import com.proserus.stocks.bp.dao.PersistenceManager;

public interface DatabaseStrategy {
	public void applyUpgrade(PersistenceManager pm);
	
	enum EVALUATE{
		NORMAL,
		OPPOSITE,
	}
}
