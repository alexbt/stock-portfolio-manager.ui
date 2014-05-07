package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

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
	public static int keyEvent = KeyEvent.VK_D;
	private static final long serialVersionUID = 201404031810L;
	private PortfolioController controller = ViewControllers.getController();
	
	private Symbol selectedSymbol = null;
	
	private static final String CANNOT_REMOVE_SYMBOL = "Cannot remove symbol";
	
	private static final RemoveSymbolAction singleton = new RemoveSymbolAction();
	
	public static RemoveSymbolAction getInstance(){
		return singleton;
	}
	private RemoveSymbolAction(){
		EventBus.getInstance().add(this, SwingEvents.SYMBOL_SELECTION_CHANGED);
		setEnabled(false);
	}
	
	@Override
    public void actionPerformed(ActionEvent arg0) {
		int n = JOptionPane.showConfirmDialog(null, "Do you want to remove symbol '"+selectedSymbol.getTicker()+"' ?", "Removing symbol",
	            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	    if (n == JOptionPane.YES_OPTION) {
	    	if (controller.remove(selectedSymbol)) {
				controller.setSelection((Symbol)null);
			} else {
				JOptionPane.showConfirmDialog(null, "The symbol '"
						+ selectedSymbol.getTicker()
						+ "' is currently used in transactions", CANNOT_REMOVE_SYMBOL,
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
