package com.proserus.stocks.ui;

import java.awt.AWTEvent;
import java.awt.EventQueue;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

class EventQueueProxy extends EventQueue {
	private final static Logger LOGGER = Logger.getLogger(EventQueueProxy.class.getName());
    protected void dispatchEvent(AWTEvent newEvent) {
        try {
            super.dispatchEvent(newEvent);
        } catch (Throwable e) {
        	LOGGER.log(Priority.FATAL, "Unexpected error", e);
        }
    }
}