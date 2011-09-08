package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.proserus.stocks.ui.view.general.FilterPanelImpl;

public class ShowHideFiltersAction extends AbstractAction  {

	@Override
    public void actionPerformed(ActionEvent arg0) {
		FilterPanelImpl.getInstance().setVisible(!FilterPanelImpl.getInstance().isVisible());
		FilterPanelImpl.getInstance().revalidate();
    }

}
