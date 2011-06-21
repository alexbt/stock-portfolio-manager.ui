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
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.ListDataListener;

import org.jfree.data.time.Year;

import com.proserus.stocks.bp.DateUtil;
import com.proserus.stocks.bp.FilterBp;
import com.proserus.stocks.bp.SharedFilter;
import com.proserus.stocks.bp.SymbolsBp;
import com.proserus.stocks.bp.TransactionsBp;
import com.proserus.stocks.controllers.PortfolioControllerImpl;
import com.proserus.stocks.model.symbols.Symbol;
import com.proserus.stocks.view.common.EmptyYear;
import com.proserus.stocks.view.common.SortedComboBoxModel;
import com.proserus.stocks.view.symbols.EmptySymbol;

/**
 * @author Alex
 * 
 */
public class FilterPanelImpl extends AbstractFilterPanel implements ActionListener, Observer, KeyListener {
	private SortedComboBoxModel modelSymbols = new SortedComboBoxModel();
	private SortedComboBoxModel modelYears = new SortedComboBoxModel(new FilterYearComparator());
	static private FilterPanelImpl singleton = new FilterPanelImpl();

	static public FilterPanelImpl getInstance() {
		return singleton;
	}

	private FilterPanelImpl() {
		setBorder(BorderFactory.createLineBorder(Color.black));
		getLabelList().setAddEnabled(false);
		getLabelList().setListColor(getLabelList().getBackground());
		getLabelList().setHorizontal(true);
		getLabelList().setModeFilter(true);
		PortfolioControllerImpl.getInstance().addLabelsObserver(getLabelList());

		PortfolioControllerImpl.getInstance().addTransactionsObserver(this);
		PortfolioControllerImpl.getInstance().addFilterObserver(this);
		PortfolioControllerImpl.getInstance().addTransactionObserver(this);

		getLabelList().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		getYearField().addActionListener(this);
		getYearField().setActionCommand("changeYear");
		getYearField().setModel(modelYears);

		populateSymbols();

		getSymbolField().setModel(modelSymbols);
		PortfolioControllerImpl.getInstance().addSymbolsObserver(this);
		getSymbolField().addActionListener(this);
	}

	private void populateYears(Year min) {
		if (!min.equals(modelYears.getElementAt(modelYears.getSize()-1))) {
			Object o = modelYears.getSelectedItem();
			
			for(ListDataListener listener: modelYears.getListDataListeners()){
				modelYears.removeListDataListener(listener);
			}
			getYearField().removeActionListener(this);
			
			modelYears.removeAllElements();
			for (Year i = DateUtil.getCurrentYear(); i.getYear() >= min.getYear(); i = (Year) i.previous()) {
				modelYears.addElement(i);
			}
			modelYears.addElement((new EmptyYear()));
			modelYears.setSelectedItem(o);
			getYearField().addActionListener(this);
		}
	}

	private void populateSymbols() {
		Iterator<Symbol> iter = PortfolioControllerImpl.getInstance().getSymbols().iterator();
		modelSymbols.removeAllElements();
		while (iter.hasNext()) {
			modelSymbols.addElement(iter.next());
		}
		Symbol s = new EmptySymbol();
		modelSymbols.addElement(s);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource().equals(getYearField()) && arg0.getActionCommand().compareTo("changeYear") == 0) {
			Year selectedYear = (Year) ((JComboBox) arg0.getSource()).getSelectedItem();
			if (!selectedYear.toString().isEmpty()) {
				SharedFilter.getInstance().setYear(selectedYear);
			} else {
				SharedFilter.getInstance().setYear(null);
			}
		}

		if (arg0.getSource() instanceof JComboBox) {
			Object o = ((JComboBox) arg0.getSource()).getSelectedItem();
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

	@Override
	public void update(Observable arg0, Object UNUSED) {
		if (arg0 instanceof TransactionsBp) {
			populateYears(PortfolioControllerImpl.getInstance().getFirstYear());
		} else if (arg0 instanceof SymbolsBp) {
			Object o = modelSymbols.getSelectedItem();
			modelSymbols.removeAllElements();
			for (Symbol symbol : PortfolioControllerImpl.getInstance().getSymbols()) {
				modelSymbols.addElement(symbol);
			}
			Symbol s = new EmptySymbol();
			modelSymbols.addElement(s);
			modelSymbols.setSelectedItem(o);
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
			PortfolioControllerImpl.getInstance().setCustomFilter((((JTextField) textField.getSource()).getText()));
		} else {
			PortfolioControllerImpl.getInstance().setCustomFilter("");
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
