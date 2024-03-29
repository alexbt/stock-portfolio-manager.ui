package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import com.proserus.stocks.ui.view.general.ColorSettingsDialog;

public class ShowSettingsAction extends AbstractAction {
	public static int keyEvent = KeyEvent.VK_C;
	private static final long serialVersionUID = 201404031810L;
	private static final ShowSettingsAction singleton = new ShowSettingsAction();

	public static ShowSettingsAction getInstance() {
		return singleton;
	}

	private ShowSettingsAction() {
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		ColorSettingsDialog.getInstance().setVisible(true);
	}

}
