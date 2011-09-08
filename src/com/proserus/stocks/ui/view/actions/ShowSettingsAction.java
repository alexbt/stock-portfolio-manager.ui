package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.proserus.stocks.ui.view.general.ColorSettingsDialog;

public class ShowSettingsAction extends AbstractAction  {
	public ShowSettingsAction(){
	}
	
	@Override
    public void actionPerformed(ActionEvent arg0) {
		ColorSettingsDialog.getInstance().setVisible(true);
    }

}
