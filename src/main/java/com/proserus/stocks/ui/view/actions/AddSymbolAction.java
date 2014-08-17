package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proserus.stocks.ui.view.common.DialogImpl;
import com.proserus.stocks.ui.view.symbols.AddEditSymbolPanelImpl;

public class AddSymbolAction extends AbstractAction {
	public static int keyEvent = KeyEvent.VK_S;
	private static final long serialVersionUID = 201404031808L;
	private final static Logger LOGGER = LoggerFactory.getLogger(AddSymbolAction.class.getName());
	private static final AddSymbolAction singleton = new AddSymbolAction();

	private DialogImpl addSymbolWindow = new DialogImpl(new AddEditSymbolPanelImpl(true), "Add a symbol");

	private AddSymbolAction() {

	}

	public static AddSymbolAction getInstance() {
		return singleton;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			addSymbolWindow.setVisibile(true);
		} catch (Throwable e) {
			LOGGER.error("Error displaying AddEditSymbol panel", e);
		}
	}

}
