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
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.proserus.stocks.bp.FilterBp;
import com.proserus.stocks.bp.SharedFilter;
import com.proserus.stocks.bp.SymbolsBp;
import com.proserus.stocks.controllers.PortfolioControllerImpl;
import com.proserus.stocks.controllers.iface.PortfolioController;
import com.proserus.stocks.model.symbols.Symbol;
import com.proserus.stocks.view.common.SortedComboBoxModel;
import com.proserus.stocks.view.symbols.EmptySymbol;

public class TransactionSymbolFilterPanel extends JPanel implements Observer, ActionListener {
	private static final String SYMBOL = "Symbol:";

	private static final String EMPTY_STR = "";

	private SortedComboBoxModel model = new SortedComboBoxModel();

	private PortfolioController controller = PortfolioControllerImpl.getInstance();

	public TransactionSymbolFilterPanel() {
		FlowLayout ff = new FlowLayout();
		ff.setAlignment(FlowLayout.LEFT);
		setLayout(ff);
		add(new JLabel(SYMBOL));
		JComboBox dropDown = new JComboBox();
		dropDown.setModel(model);
		controller.addSymbolsObserver(this);
		dropDown.addActionListener(this);
		add(dropDown);
	}

	@Override
	public void update(Observable arg0, Object UNUSED) {
		if (arg0 instanceof SymbolsBp) {
			model.removeAllElements();
			for (Symbol symbol : controller.getSymbols()) {
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
				FilterBp filter = SharedFilter.getInstance();
				if (((Symbol) o).getId() != null) {
					filter.setSymbol((Symbol) o);
				}

				else {
					filter.setSymbol(null);
				}
			}
		}
	}
}
