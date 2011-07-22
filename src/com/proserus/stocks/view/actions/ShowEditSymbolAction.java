package com.proserus.stocks.view.actions;

import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;

import com.proserus.stocks.controllers.iface.PortfolioController;
import com.proserus.stocks.model.ItemSelection;
import com.proserus.stocks.view.common.ViewControllers;
import com.proserus.stocks.view.symbols.SymbolsModificationView;

public class ShowEditSymbolAction extends AbstractAction implements Observer {
	private PortfolioController controller = ViewControllers.getController();
	
	public ShowEditSymbolAction(){
		controller.addSelectionObserver(this);
		setEnabled(false);
	}
	
	@Override
    public void actionPerformed(ActionEvent arg0) {
		new SymbolsModificationView(controller.getSelection().getSelectedSymbol()).setVisibile(true);
    }
	
	@Override
    public void update(Observable arg0, Object arg1) {
		ItemSelection selection = (ItemSelection)arg0;
		setEnabled(selection.getSelectedSymbol()!=null);
    }
}
