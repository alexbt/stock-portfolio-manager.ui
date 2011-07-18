package com.proserus.stocks.view.actions;

import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.proserus.stocks.controllers.iface.PortfolioController;
import com.proserus.stocks.model.ItemSelection;
import com.proserus.stocks.model.symbols.Symbol;
import com.proserus.stocks.view.common.ViewControllers;

public class RemoveSymbolAction extends AbstractAction implements Observer {
	private PortfolioController controller = ViewControllers.getController();
	
	private static final String THE_SYMBOL_IS_CURRENTLY_USED_IN_TRANSACTIONS = "The symbol is currently used in transactions";
	
	private static final String CANNOT_REMOVE_SYMBOL = "Cannot remove symbol";
	
	public RemoveSymbolAction(){
		controller.addSelectionObserver(this);
		setEnabled(false);
	}
	
	@Override
    public void actionPerformed(ActionEvent arg0) {
		if (controller.remove(controller.getSelection().getSelectedSymbol())) {
			controller.setSelection((Symbol)null);
		} else {
			JOptionPane.showConfirmDialog(null, THE_SYMBOL_IS_CURRENTLY_USED_IN_TRANSACTIONS, CANNOT_REMOVE_SYMBOL,
			        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
		}
    }
	
	@Override
    public void update(Observable arg0, Object arg1) {
		ItemSelection selection = (ItemSelection)arg0;
		setEnabled(selection.getSelectedSymbol()!=null);
    }
}
