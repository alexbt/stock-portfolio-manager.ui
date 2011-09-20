package com.proserus.stocks.ui.dbutils;

import java.util.ArrayList;
import java.util.Collection;

public enum DatabaseUpdagreEnum {
	CREATE_DATABASE(0, new CreateDatabaseStrategy()),
	CREATE_TABLES_HISTORICALPRICE(1, new CreateTablesStrategy("HISTORICALPRICE")),
	CREATE_TABLES_LABEL(1, new CreateTablesStrategy("LABEL")),
	CREATE_TABLES_SYMBOL_PRICES(1, new CreateTablesStrategy("SYMBOL_PRICES")),
	CREATE_TABLES_SYMBOL(1, new CreateTablesStrategy("SYMBOL")),
	CREATE_TABLES_TRANSACTION_LABEL(1, new CreateTablesStrategy("TRANSACTION_LABEL")),
	CREATE_TABLES_TRANSACTION(1, new CreateTablesStrategy("TRANSACTION")),
	CREATE_TABLES_VERSION(1, new CreateTablesStrategy("VERSION")),
	CREATE_TABLES_HIBERNATE_SEQUENCE(2, new HibernateSequenceStrategy()),
	DROP_UNAMED_CONSTRAINTS(3, new DropUnamedConstraintsStrategy()),
	CREATE_NAMED_CONSTRAINTS(4, new CreateNamedConstraintsStrategy()),
	DROP_SECTOR(5, new DropFirstSectorStrategy()),
	ADD_SECTOR(6, new AddSectorStrategy()),
	UPDATE_TRANSACTION_TYPE_ENUM(7, new TransactionTypeEnumStrategy()),
	UPDATE_CURRENCY_ENUM(8, new CurrencyEnumStrategy()),
	
	PERSIST_LATEST_VERSION(9, new LatestVersionStrategy());
	
	//make sure that the latest version is the highest number...
	static{
		for(DatabaseUpdagreEnum val: values()){
			if(!val.equals(PERSIST_LATEST_VERSION)){
				if(val.getVersion()>= PERSIST_LATEST_VERSION.getVersion() ){
					throw new AssertionError();
				}
			}
		}
	}
	private int version;

	private DatabaseStrategy strategy;
	
	private DatabaseUpdagreEnum(int version, DatabaseStrategy strategy){
		this.version = version;
		this.strategy = strategy;
		
	}

	public int getVersion() {
    	return version;
    }

	public DatabaseStrategy getStrategy() {
    	return strategy;
    }
	
	public static Collection<DatabaseStrategy> getVersionStrategies(int version){
		Collection<DatabaseStrategy> col = new ArrayList<DatabaseStrategy>();
		for(DatabaseUpdagreEnum val: values()){
			if(val.getVersion() == version){
				col.add(val.getStrategy());
			}
		}
		return col;
	}
	
	public static int getLatestVersion(){
		return PERSIST_LATEST_VERSION.getVersion();
	}
	
}
