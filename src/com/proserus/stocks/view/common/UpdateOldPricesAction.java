package com.proserus.stocks.view.common;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class UpdateOldPricesAction extends AbstractAction  {

	@Override
    public void actionPerformed(ActionEvent arg0) {
		ViewControllers.getController().updateHistoricalPrices();
    }

}
