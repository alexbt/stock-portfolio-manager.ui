package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.proserus.stocks.ui.controller.ViewControllers;

public class UpdateOldPricesAction extends AbstractAction  {

	@Override
    public void actionPerformed(ActionEvent arg0) {
		ViewControllers.getController().updateHistoricalPrices();
    }

}
