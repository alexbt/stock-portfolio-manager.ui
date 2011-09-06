package com.proserus.stocks.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.proserus.stocks.view.common.ViewControllers;

public class CloseApplicationAction extends AbstractAction  {

	@Override
    public void actionPerformed(ActionEvent arg0) {
		ViewControllers.getController().cleanup();
		System.exit(0);
    }
}
