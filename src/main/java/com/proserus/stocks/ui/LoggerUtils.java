package com.proserus.stocks.ui;

import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;

public class LoggerUtils {
	private static LoggerUtils  singleton= new LoggerUtils();
	
	private LoggerUtils(){
	}
	
	public static LoggerUtils getInstance(){
		return singleton;
	}
	
	private static Logger LOGGER = Logger.getLogger(Launch.class);
	
	public void info(String message){
		LOGGER.info(message);
	}

	public void fatal(String message, Throwable e) {
		LOGGER.fatal(message, e);
	}

}
