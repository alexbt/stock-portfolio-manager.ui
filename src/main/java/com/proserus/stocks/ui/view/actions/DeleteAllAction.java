package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.proserus.stocks.ui.controller.ViewControllers;

public class DeleteAllAction extends AbstractAction {
	private static final long serialVersionUID = 201404031810L;

	private static final DeleteAllAction singleton = new DeleteAllAction();

	private DeleteAllAction() {

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			ViewControllers.getController().cleanup();
		}  finally {
			ViewControllers.getController().deleteAll();
		}
	}

	public static DeleteAllAction getInstance() {
		return singleton;
	}
}
