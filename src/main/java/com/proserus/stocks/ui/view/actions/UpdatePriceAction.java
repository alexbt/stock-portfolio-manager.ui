package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.proserus.stocks.ui.controller.ViewControllers;

public class UpdatePriceAction extends AbstractAction  {
	private static final long serialVersionUID = 201404031810L;
	
	private static final UpdatePriceAction singleton = new UpdatePriceAction();
	
	public static UpdatePriceAction getInstance() {
		return singleton;
	}
	private UpdatePriceAction(){
		
	}

	@Override
    public void actionPerformed(ActionEvent arg0) {
		ViewControllers.getController().updatePrices();
    }

	

}
