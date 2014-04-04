package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.proserus.stocks.ui.controller.ViewControllers;
import com.proserus.stocks.ui.view.common.DialogImpl;
import com.proserus.stocks.ui.view.transactions.AddTransactionPanelImpl;

public class AddTransactionAction extends AbstractAction  {
	
	private static final long serialVersionUID = 201404031750L;
	private final static Logger LOGGER = Logger.getLogger(AddTransactionAction.class.getName());
	
	private static final AddTransactionAction singleton = new AddTransactionAction();
	
	private DialogImpl addTransactionWindow = new DialogImpl(new AddTransactionPanelImpl(),"Add a transaction");

	private AddTransactionAction(){
		
	}
	
	public static AddTransactionAction getInstance(){
		return singleton;
	}
	
	@Override
    public void actionPerformed(ActionEvent arg0) {
		try{
			addTransactionWindow.setVisibile(true);
		ViewControllers.getController().refreshOther();
		}catch(Throwable e){
			LOGGER.log(Priority.FATAL, "Error displaying AddTransaction panel", e);
		}
    }

}
