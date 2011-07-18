package com.proserus.stocks.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.proserus.stocks.view.general.ColorSettingsDialog;

public class ShowSettingsAction extends AbstractAction  {
	public ShowSettingsAction(){
	}
	
	@Override
    public void actionPerformed(ActionEvent arg0) {
		ColorSettingsDialog.getInstance().setVisible(true);
    }

}
