package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.proserus.stocks.ui.controller.ViewControllers;

public class CloseApplicationAction extends AbstractAction  {

	@Override
    public void actionPerformed(ActionEvent arg0) {
		try {
		ViewControllers.getController().cleanup();
		}finally{
			System.exit(0);
		}
    }
}
