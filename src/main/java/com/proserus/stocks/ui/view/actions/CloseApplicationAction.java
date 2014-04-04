package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.apache.log4j.Priority;

import com.proserus.stocks.ui.controller.ViewControllers;

public class CloseApplicationAction extends AbstractAction {
	private static final long serialVersionUID = 201404031810L;

	private static final CloseApplicationAction singleton = new CloseApplicationAction();

	private CloseApplicationAction() {

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			ViewControllers.getController().cleanup();
		}  finally {
			System.exit(0);
		}
	}

	public static CloseApplicationAction getInstance() {
		return singleton;
	}
}
