package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import com.proserus.stocks.ui.controller.ViewControllers;

public class UpdatePriceAction extends AbstractAction  {
	public static int keyEvent = KeyEvent.VK_P;
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
