package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventBus;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.SwingEvents;
import com.proserus.stocks.ui.view.symbols.SymbolsModificationView;

//FIXME remove EventListener
public class ShowEditSymbolAction extends AbstractAction implements EventListener {
	private static final long serialVersionUID = 201404031810L;
	private Symbol selectedSymbol = null;
	private static ShowEditSymbolAction singleton = new ShowEditSymbolAction();
	private ShowEditSymbolAction(){
		EventBus.getInstance().add(this, SwingEvents.SYMBOL_SELECTION_CHANGED);
		setEnabled(false);
	}
	
	public static ShowEditSymbolAction getInstance(){
		return singleton;
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
