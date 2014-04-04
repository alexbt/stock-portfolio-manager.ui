package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.proserus.stocks.ui.view.general.FilterPanelImpl;

public class ShowHideFiltersAction extends AbstractAction  {
	private static final long serialVersionUID = 201404031810L;

	private static final ShowHideFiltersAction singleton = new ShowHideFiltersAction();

	public static ShowHideFiltersAction getInstance() {
		return singleton;
	}
	
	@Override
    public void actionPerformed(ActionEvent arg0) {
		FilterPanelImpl.getInstance().setVisible(!FilterPanelImpl.getInstance().isVisible());
		FilterPanelImpl.getInstance().revalidate();
    }

}
