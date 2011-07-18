package com.proserus.stocks.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.proserus.stocks.view.common.DialogImpl;
import com.proserus.stocks.view.symbols.AddEditSymbolPanelImpl;

public class AddSymbolAction extends AbstractAction  {

	@Override
    public void actionPerformed(ActionEvent arg0) {
		new DialogImpl(new AddEditSymbolPanelImpl(true),"Add a symbol").setVisibile(true);
    }

}
