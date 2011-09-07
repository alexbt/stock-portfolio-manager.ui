/*
 * -----------------------------------------------------------------------
 * Project: SCJD
 * -----------------------------------------------------------------------
 * File: ComponentStatusBar.java
 * -----------------------------------------------------------------------
 * Author: Alex Belisle-Turcot
 * -----------------------------------------------------------------------
 * Prometric Id: SP9339741
 * -----------------------------------------------------------------------
 * Email: alex.belisleturcot+scjd@gmail.com
 * -----------------------------------------------------------------------
 * Date of creation: 2007-11-30
 * -----------------------------------------------------------------------
 */
package com.proserus.stocks.view.transactions;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.proserus.stocks.bp.Filter;
import com.proserus.stocks.events.Event;
import com.proserus.stocks.events.EventBus;
import com.proserus.stocks.events.EventListener;
import com.proserus.stocks.events.SwingEvents;
import com.proserus.stocks.model.symbols.Symbol;
import com.proserus.stocks.view.common.SortedComboBoxModel;
import com.proserus.stocks.view.common.ViewControllers;
import com.proserus.stocks.view.symbols.EmptySymbol;

public class TransactionSymbolFilterPanel extends JPanel implements EventListener, ActionListener {
	private Filter filter = ViewControllers.getFilter();
	
	private static final String SYMBOL = "Symbol:";

	private SortedComboBoxModel model = new SortedComboBoxModel();

	public TransactionSymbolFilterPanel() {
		EventBus.getInstance().add(this, SwingEvents.SYMBOLS_UPDATED);
		FlowLayout ff = new FlowLayout();
		ff.setAlignment(FlowLayout.LEFT);
		setLayout(ff);
		add(new JLabel(SYMBOL));
		JComboBox dropDown = new JComboBox();
		dropDown.setModel(model);
		dropDown.addActionListener(this);
		add(dropDown);
	}

	@Override
	public void update(Event event, Object mo) {
		if(SwingEvents.SYMBOLS_UPDATED.equals(event)){
			model.removeAllElements();
			for (Symbol symbol : SwingEvents.SYMBOLS_UPDATED.resolveModel(mo)) {
				model.addElement(symbol);
			}
			Symbol s = new EmptySymbol();
			model.addElement(s);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JComboBox) {
			Object o = ((JComboBox) e.getSource()).getSelectedItem();
			if (o instanceof Symbol) {
				if (((Symbol) o).getId() != null) {
					filter.setSymbol((Symbol) o);
				}
				else {
					filter.setSymbol(null);
				}
				ViewControllers.getController().refreshFilteredData();
			}
		}
	}
}
