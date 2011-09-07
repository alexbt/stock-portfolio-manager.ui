/**
 * 
 */
package com.proserus.stocks.view.general;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.jfree.data.time.Year;

import com.proserus.stocks.bp.DateUtil;
import com.proserus.stocks.bp.Filter;
import com.proserus.stocks.events.Event;
import com.proserus.stocks.events.EventBus;
import com.proserus.stocks.events.EventListener;
import com.proserus.stocks.events.SwingEvents;
import com.proserus.stocks.model.symbols.Symbol;
import com.proserus.stocks.model.transactions.TransactionType;
import com.proserus.stocks.view.common.EmptyYear;
import com.proserus.stocks.view.common.SortedComboBoxModel;
import com.proserus.stocks.view.common.ViewControllers;
import com.proserus.stocks.view.symbols.EmptySymbol;

/**
 * @author Alex
 * 
 */
public class FilterPanelImpl extends AbstractFilterPanel implements ActionListener, EventListener, KeyListener {
	private SortedComboBoxModel modelSymbols = new SortedComboBoxModel();
	private SortedComboBoxModel modelYears = new SortedComboBoxModel(new FilterYearComparator());
	static private FilterPanelImpl singleton = new FilterPanelImpl();
	private Filter filter = ViewControllers.getFilter();

	static public FilterPanelImpl getInstance() {
		return singleton;
	}

	private FilterPanelImpl() {
		setBorder(BorderFactory.createLineBorder(Color.black));
		getLabelList().setAddEnabled(false);
		getLabelList().setListColor(getLabelList().getBackground());
		getLabelList().setHorizontal(true);
		getLabelList().setModeFilter(true);
		EventBus.getInstance().add(getLabelList(), SwingEvents.LABELS_UPDATED);

		EventBus.getInstance().add(this, SwingEvents.TRANSACTION_UPDATED);

		getLabelList().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		getYearField().addActionListener(this);
		getYearField().setModel(modelYears);

//		populateSymbols();

		getSymbolField().setModel(modelSymbols);
		EventBus.getInstance().add(this, SwingEvents.SYMBOLS_UPDATED);
		getSymbolField().addActionListener(this);
		
		getTransactionTypeField().addItem("");
		for (TransactionType transactionType : TransactionType.values()) {
			getTransactionTypeField().addItem(transactionType);
		}
		getTransactionTypeField().addActionListener(this);
	}

	private void populateYears(Year min) {
		if (!min.equals(modelYears.getElementAt(modelYears.getSize()-1))) {
			getYearField().removeActionListener(this);
			
			Object o = modelYears.getSelectedItem();
			
			modelYears.removeAllElements();
			for (Year i = DateUtil.getCurrentYear(); i.getYear() >= min.getYear(); i = (Year) i.previous()) {
				modelYears.addElement(i);
			}
			modelYears.addElement((new EmptyYear()));
			modelYears.setSelectedItem(o);
			getYearField().addActionListener(this);
		}
	}

//	private void populateSymbols() {
//		Iterator<Symbol> iter = ViewControllers.getController().getSymbols().iterator();
//		modelSymbols.removeAllElements();
//		while (iter.hasNext()) {
//			modelSymbols.addElement(iter.next());
//		}
//		Symbol s = new EmptySymbol();
//		modelSymbols.addElement(s);
//	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource().equals(getYearField())) {
			Year selectedYear = (Year) ((JComboBox) arg0.getSource()).getSelectedItem();
			if (!selectedYear.toString().isEmpty()) {
				filter.setYear(selectedYear);
			} else {
				filter.setYear(null);
			}
			ViewControllers.getController().refreshFilteredData();
		} else if (arg0.getSource().equals(getSymbolField())) {
			Object o = ((JComboBox) arg0.getSource()).getSelectedItem();
			if (o instanceof Symbol) {
				if (((Symbol) o).getId() != null) {
					filter.setSymbol((Symbol) o);
				}

				else {
					filter.setSymbol(null);
				}
			}
			ViewControllers.getController().refreshFilteredData();
		}else if (arg0.getSource().equals(getTransactionTypeField())) {
			Object o = ((JComboBox) arg0.getSource()).getSelectedItem();
			TransactionType type=null;
			if(o instanceof TransactionType){
				type = (TransactionType)o;
			}
			
			filter.setTransactionType(type);
			ViewControllers.getController().refreshFilteredData();
		}
	}

	@Override
	public void update(Event event, Object model) {
		if (SwingEvents.TRANSACTION_UPDATED.equals(event)) {
			populateYears(ViewControllers.getController().getFirstYear());
		} else if (SwingEvents.SYMBOLS_UPDATED.equals(event)) {
			Object o = modelSymbols.getSelectedItem();
			getSymbolField().removeActionListener(this);
			modelSymbols.removeAllElements();
			for (Symbol symbol : SwingEvents.SYMBOLS_UPDATED.resolveModel(model)) {
				modelSymbols.addElement(symbol);
			}
			Symbol s = new EmptySymbol();
			modelSymbols.addElement(s);
			modelSymbols.setSelectedItem(o);
			getSymbolField().addActionListener(this);
		}
	}
	

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent textField) {
		// TODO BEFORE: int length = ((JTextField)
		// textField.getSource()).getText().length();
		// NOW BETTER ?
		if (!((JTextField) textField.getSource()).getText().isEmpty()) {
			ViewControllers.getController().setCustomFilter((((JTextField) textField.getSource()).getText()));
		} else {
			ViewControllers.getController().setCustomFilter("");
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
