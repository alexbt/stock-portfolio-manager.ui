package com.proserus.stocks.view.common;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class UpdatePriceAction extends AbstractAction  {

	@Override
    public void actionPerformed(ActionEvent arg0) {
		ViewControllers.getController().updatePrices();
    }

}
