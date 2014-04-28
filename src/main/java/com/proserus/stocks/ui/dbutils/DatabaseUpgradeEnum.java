package com.proserus.stocks.ui.dbutils;

public enum DatabaseUpgradeEnum {
	VERSION_0(0,new AbstractScriptDatabaseStrategy[]{}),
	VERSION_1(1,new AbstractScriptDatabaseStrategy[]{}),
	VERSION_21(21, new ExecuteScriptStrategy("21-1_createTables.sql"),
			new DropAllConstraintsStrategy(), new ExpectedDatatypeStrategy(
					"SYMBOL", "SECTOR", "INTEGER", "21-3_dropSector.sql"),
			new ColumnNotExistStrategy("SYMBOL", "SECTOR", "21-4_addSector.sql"),
			new ExpectedDatatypeStrategy("TRANSACTION", "TYPE", "INTEGER", "21-5_typeEnum.sql"),
			new ExpectedDatatypeStrategy("SYMBOL", "CURRENCY", "INTEGER", "21-6_currencyEnum.sql")),

	VERSION_24(24, new DropAllConstraintsStrategy(), new ExecuteScriptStrategy(
			"24-3_addConstraints.sql"));

	private static int latestVersion;

	static {
		latestVersion = 0;
		for (DatabaseUpgradeEnum e : values()) {
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
