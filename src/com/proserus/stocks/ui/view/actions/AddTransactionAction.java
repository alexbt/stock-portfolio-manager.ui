package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.proserus.stocks.ui.controller.ViewControllers;
import com.proserus.stocks.ui.view.common.DialogImpl;
import com.proserus.stocks.ui.view.transactions.AddTransactionPanelImpl;

public class AddTransactionAction extends AbstractAction  {

	@Override
    public void actionPerformed(ActionEvent arg0) {
		AddTransactionPanelImpl panel = new AddTransactionPanelImpl();
		ViewControllers.getController().refreshOther();
		new DialogImpl(panel,"Add a transaction").setVisibile(true);
    }

}
