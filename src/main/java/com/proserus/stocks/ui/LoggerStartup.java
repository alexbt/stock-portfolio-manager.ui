package com.proserus.stocks.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoggerStartup {
	private static LoggerStartup  singleton= new LoggerStartup();
	
	private LoggerStartup(){
	}
	
	public static LoggerStartup getInstance(){
		return singleton;
	}
	
	private static Logger LOGGER = LoggerFactory.getLogger(Launch.class);
	
	public void info(String message){
		LOGGER.info(message);
	}

	public void fatal(String message, Throwable e) {
		LOGGER.error(message, e);
	}

}
