package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import com.proserus.stocks.ui.controller.ViewControllers;

public class UpdateOldPricesAction extends AbstractAction {
	public static int keyEvent = KeyEvent.VK_O;
	private static final long serialVersionUID = 201404031810L;
	private static final UpdateOldPricesAction singleton = new UpdateOldPricesAction();

	public static UpdateOldPricesAction getInstance() {
		return singleton;
	}

	private UpdateOldPricesAction() {

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		ViewControllers.getController().updateHistoricalPrices();
	}

}
