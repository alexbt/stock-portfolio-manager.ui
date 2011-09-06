package com.proserus.stocks.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.proserus.stocks.events.Event;
import com.proserus.stocks.events.EventBus;
import com.proserus.stocks.events.EventListener;
import com.proserus.stocks.events.SwingEvents;
import com.proserus.stocks.model.symbols.Symbol;
import com.proserus.stocks.view.symbols.SymbolsModificationView;

public class ShowEditSymbolAction extends AbstractAction implements EventListener {
	private Symbol selectedSymbol = null;
	public ShowEditSymbolAction(){
		EventBus.getInstance().add(this, SwingEvents.SYMBOL_SELECTION_CHANGED);
		setEnabled(false);
	}
	
	@Override
    public void actionPerformed(ActionEvent arg0) {
		new SymbolsModificationView(selectedSymbol).setVisibile(true);
    }
	

	@Override
    public void update(Event event, Object model) {
	   if(SwingEvents.SYMBOL_SELECTION_CHANGED.equals(event)){
		   selectedSymbol = SwingEvents.SYMBOL_SELECTION_CHANGED.resolveModel(model);
		   setEnabled(selectedSymbol!=null);
	   }
    }
}
