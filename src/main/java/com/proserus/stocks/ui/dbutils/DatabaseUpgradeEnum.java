package com.proserus.stocks.ui.dbutils;

import org.apache.commons.lang3.Validate;

public enum DatabaseUpgradeEnum {
	VERSION_0(0,new AbstractScriptDatabaseStrategy[]{}),
	VERSION_21(21, 	 
	        new ExecuteScriptStrategy("21-1_createTables.sql"),
            new ExecuteScriptStrategy("21-2_dropHibernateSequence.sql"),
	        new ExpectedDatatypeStrategy("TRANSACTION", "TYPE", "CHARACTER VARYING", "21-3_typeEnumNotNull.sql"),
            new ExpectedDatatypeStrategy("TRANSACTION", "TYPE", "INTEGER", "21-3_typeIntegerNotNull.sql"),
            new ExpectedDatatypeStrategy("SYMBOL", "SECTOR", "INTEGER", "21-3_sectorIntegerNotNull.sql"),
            new ExpectedDatatypeStrategy("SYMBOL", "SECTOR", "CHARACTER VARYING", "21-3_sectorEnumNotNull.sql"),
            new ExpectedDatatypeStrategy("SYMBOL", "CURRENCY", "CHARACTER VARYING", "21-3_currencyEnumNotNull.sql"),
			new DropAllConstraintsStrategy(), 
			//Do not delete, check if exist before creating... Use SequenceNotExistStrategy 
			//new ExecuteScriptStrategy("21-2_hibernateSequence.sql"),
			new ExpectedDatatypeStrategy("SYMBOL", "SECTOR", "INTEGER", "21-4_dropSector.sql"),
			new ColumnNotExistStrategy("SYMBOL", "SECTOR", "21-5_addSector.sql"),
			new ExpectedDatatypeStrategy("TRANSACTION", "TYPE", "INTEGER", "21-6_typeEnum.sql"),
			new ExpectedDatatypeStrategy("SYMBOL", "CURRENCY", "INTEGER", "21-7_currencyEnum.sql"),
			new YearPriceStrategy()),

	VERSION_24(24, 
	        new ExecuteScriptStrategy("24-1_defaultValues.sql"),
	        //new ExecuteScriptStrategy("24-3_yearType.sql"),
	        new ExecuteScriptStrategy("24-3_addConstraints.sql"));

	private static int latestVersion;

	static {
		latestVersion = 0;
		for (DatabaseUpgradeEnum e : values()) {
		    for(DatabaseStrategy s: e.getStrategies()){
		        if(s instanceof ExecuteScriptStrategy){
	                Validate.notNull(DatabaseUpgradeEnum.class.getClassLoader().getResourceAsStream( ((ExecuteScriptStrategy)s).getScript()));
	            }
		    }
			latestVersion = Math.max(latestVersion, e.getVersion());
		}
	}

	private int version;

	private DatabaseStrategy[] strategies;

	private DatabaseUpgradeEnum(int version, DatabaseStrategy... strategies) {
		this.version = version;
		this.strategies = strategies;

	}

	public static int getLatestVersion() {
		return latestVersion;
	}

	public int getVersion() {
		return version;
	}

	public DatabaseStrategy[] getStrategies() {
		return strategies;
	}
}
