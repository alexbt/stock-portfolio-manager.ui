package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import com.proserus.stocks.ui.view.general.FilterPanelImpl;

public class ShowHideFiltersAction extends AbstractAction {
	public static int keyEvent = KeyEvent.VK_F;
	private static final long serialVersionUID = 201404031810L;

	private static final ShowHideFiltersAction singleton = new ShowHideFiltersAction();

	public static ShowHideFiltersAction getInstance() {
		return singleton;
	}

	private ShowHideFiltersAction() {

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		FilterPanelImpl.getInstance().setVisible(!FilterPanelImpl.getInstance().isVisible());
		FilterPanelImpl.getInstance().revalidate();
	}

}
