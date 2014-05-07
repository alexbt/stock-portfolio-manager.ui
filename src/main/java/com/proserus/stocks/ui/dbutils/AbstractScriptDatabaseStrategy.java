package com.proserus.stocks.ui.dbutils;

public abstract class AbstractScriptDatabaseStrategy implements DatabaseStrategy{
	private String script = null;
	public AbstractScriptDatabaseStrategy(String script){
		this.script = script;
		assert getClass().getClassLoader().getResourceAsStream(getScript()) != null: getScript() + " does not exist";
	}
	protected String getScript(){
		return script;
	}
}
