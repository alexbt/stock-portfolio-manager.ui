package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventBus;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.SwingEvents;
import com.proserus.stocks.ui.controller.PortfolioController;
import com.proserus.stocks.ui.controller.ViewControllers;

//FIXME remove EventListener
public class RemoveSymbolAction extends AbstractAction implements EventListener {
	private static final long serialVersionUID = 201404031810L;
	private PortfolioController controller = ViewControllers.getController();
	
	private Symbol selectedSymbol = null;
	
	private static final String THE_SYMBOL_IS_CURRENTLY_USED_IN_TRANSACTIONS = "The symbol is currently used in transactions";
	
	private static final String CANNOT_REMOVE_SYMBOL = "Cannot remove symbol";
	
	public RemoveSymbolAction(){
		EventBus.getInstance().add(this, SwingEvents.SYMBOL_SELECTION_CHANGED);
		setEnabled(false);
	}
	
	@Override
    public void actionPerformed(ActionEvent arg0) {
		int n = JOptionPane.showConfirmDialog(null, "Do you want to remove the selected symbol ?", "Removing symbol",
	            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	    if (n == JOptionPane.YES_OPTION) {
	    	if (controller.remove(selectedSymbol)) {
				controller.setSelection((Symbol)null);
			} else {
				JOptionPane.showConfirmDialog(null, THE_SYMBOL_IS_CURRENTLY_USED_IN_TRANSACTIONS, CANNOT_REMOVE_SYMBOL,
				        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
			}
	    }
    }
	
	@Override
    public void update(Event event, Object model) {
	   if(SwingEvents.SYMBOL_SELECTION_CHANGED.equals(event)){
		   selectedSymbol = SwingEvents.SYMBOL_SELECTION_CHANGED.resolveModel(model);
		   setEnabled(selectedSymbol!=null);
	   }
    }
}
