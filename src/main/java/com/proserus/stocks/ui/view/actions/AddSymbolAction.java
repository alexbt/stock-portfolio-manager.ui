package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.proserus.stocks.ui.view.common.DialogImpl;
import com.proserus.stocks.ui.view.symbols.AddEditSymbolPanelImpl;

public class AddSymbolAction extends AbstractAction {
	private static final long serialVersionUID = 201404031808L;
	private final static Logger LOGGER = Logger.getLogger(AddSymbolAction.class.getName());
	private static final AddSymbolAction singleton = new AddSymbolAction();

	private DialogImpl addSymbolWindow = new DialogImpl(
			new AddEditSymbolPanelImpl(true), "Add a symbol");

	private AddSymbolAction() {

	}
	
	public static AddSymbolAction getInstance(){
		return singleton;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			addSymbolWindow.setVisibile(true);
		} catch (Throwable e) {
			LOGGER.log(Level.FATAL, "Error displaying AddEditSymbol panel", e);
		}
	}

}
