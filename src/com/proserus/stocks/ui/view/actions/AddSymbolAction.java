package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.proserus.stocks.ui.view.common.DialogImpl;
import com.proserus.stocks.ui.view.symbols.AddEditSymbolPanelImpl;

public class AddSymbolAction extends AbstractAction  {

	@Override
    public void actionPerformed(ActionEvent arg0) {
		new DialogImpl(new AddEditSymbolPanelImpl(true),"Add a symbol").setVisibile(true);
    }

}
