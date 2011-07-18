package com.proserus.stocks.view.common;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.proserus.stocks.view.transactions.AddTransactionPanelImpl;

public class AddTransactionAction extends AbstractAction  {

	@Override
    public void actionPerformed(ActionEvent arg0) {
		new DialogImpl(new AddTransactionPanelImpl(),"Add a transaction").setVisibile(true);
    }

}
