package com.proserus.stocks.ui;

import java.awt.AWTEvent;
import java.awt.EventQueue;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

class EventQueueProxy extends EventQueue {
	private final static Logger LOGGER = Logger.getLogger(EventQueueProxy.class.getName());
    protected void dispatchEvent(AWTEvent newEvent) {
        try {
            super.dispatchEvent(newEvent);
        } catch (Throwable e) {
         	LOGGER.log(Level.FATAL, "Unexpected error", e);
        }
    }
}