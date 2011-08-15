package com.proserus.stocks.view.actions;

import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.proserus.stocks.controllers.iface.PortfolioController;
import com.proserus.stocks.model.ItemSelection;
import com.proserus.stocks.model.transactions.Transaction;
import com.proserus.stocks.view.common.ViewControllers;

public class RemoveTransactionAction extends AbstractAction implements Observer  {
	private PortfolioController controller = ViewControllers.getController();
	
	public RemoveTransactionAction(){
		controller.addSelectionObserver(this);
		setEnabled(false);
	}
	@Override
    public void actionPerformed(ActionEvent arg0) {
		int n = JOptionPane.showConfirmDialog(null, "Do you want to remove the selected transaction ?", "Removing transaction",
	            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	    if (n == JOptionPane.YES_OPTION) {
	    	controller.remove(controller.getSelection().getSelectedTransaction());
			controller.setSelection((Transaction)null);
	    }
	    
		
    }
	@Override
    public void update(Observable arg0, Object arg1) {
		ItemSelection selection = (ItemSelection)arg0;
		setEnabled(selection.getSelectedTransaction()!=null);
    }
}
