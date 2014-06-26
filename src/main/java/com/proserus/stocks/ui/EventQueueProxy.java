package com.proserus.stocks.ui;

import java.awt.AWTEvent;
import java.awt.EventQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EventQueueProxy extends EventQueue {
	private final static Logger LOGGER = LoggerFactory.getLogger(EventQueueProxy.class.getName());

	protected void dispatchEvent(AWTEvent newEvent) {
		try {
			super.dispatchEvent(newEvent);
		} catch (Throwable e) {
			LOGGER.error("Unexpected error", e);
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			}

		}
	}
}