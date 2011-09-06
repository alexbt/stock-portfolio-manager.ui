package com.proserus.stocks.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.proserus.stocks.view.common.DialogImpl;
import com.proserus.stocks.view.common.ViewControllers;
import com.proserus.stocks.view.transactions.AddTransactionPanelImpl;

public class AddTransactionAction extends AbstractAction  {

	@Override
    public void actionPerformed(ActionEvent arg0) {
		AddTransactionPanelImpl panel = new AddTransactionPanelImpl();
		ViewControllers.getController().refreshOther();
		new DialogImpl(panel,"Add a transaction").setVisibile(true);
    }

}
