package com.proserus.stocks.ui.view.symbols;

import java.math.BigDecimal;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import com.proserus.stocks.bo.symbols.CurrencyEnum;
import com.proserus.stocks.bo.symbols.HistoricalPrice;
import com.proserus.stocks.bo.symbols.SectorEnum;
import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bp.utils.DateUtil;
import com.proserus.stocks.ui.controller.ViewControllers;

public class SymbolsTableModel extends AbstractTableModel {
	
	private static final String THE_SYMBOL_ALREADY_EXIST = "The symbol already exist";

	private static final String CANNOT_ADD_SYMBOL = "Cannot add symbol";


	private static final long serialVersionUID = 20080113L;

	public static final String[] COLUMN_NAMES = { "Symbol", "Name", "Price", "Currency", "Sector", "Use Custom Prices" };

	private Object[] data = null;

	/**
	 * Default constructor to create the Table Model for the contractor table.
	 */
	public SymbolsTableModel() {
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		// Note that the data/cell address is constant,
		// no matter where the cell appears onscreen.
		if (col == 2) {
			return false;
		}
		return true;
	}

	public Symbol getSymbol(int row) {
		return (Symbol) data[row];
	}

	/**
	 * Replace the list of contractors to be displayed in the table.
	 * 
	 * @param data
	 *            Array of contractors (as Object).
	 * @see Contractor
	 */
	public void setData(final Object[] data) {
		if (data != null && data.length > 0 && !(data[0] instanceof Symbol)) {
			throw new ClassCastException();
		}
		this.data = data;
		fireTableDataChanged();
	}

	/**
	 * Retrieves the number of column in the table.
	 */
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	/**
	 * Retrieves the number of row in the table.
	 */
	public int getRowCount() {
		if (data != null) {
			return data.length;
		} else {
			return 0;
		}
	}

	/**
	 * Retrieves the name of a specific column in the table.
	 * 
	 * @param columnIndex
	 * 
	 *            The column number requested.
	 * @return The name to be displayed for that column.
	 */
	@Override
	public String getColumnName(final int columnIndex) {
		return COLUMN_NAMES[columnIndex];
	}

	public Symbol get(int rowIndex) {
		return (Symbol) data[rowIndex];
	}

	/**
	 * Retrieves the value of a specific cell in the table.
	 * 
	 * @param rowIndex
	 *            The row number of the field requested.
	 * @param columnIndex
	 *            The column number of the field requested.
	 * @return The coressponding field of the contracotr at the specified row and column.
	 * @see Contractor
	 */
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		if (data != null) {
			return (getColValue((Symbol) data[rowIndex], columnIndex));
		}
		return "";
	}

	@Override
	public void setValueAt(Object obj, int row, int col) {
		setColValue((Symbol) data[row], obj, col);
		fireTableRowsUpdated(row, row);
	}

	/**
	 * Invoked when the table is displayed, to identify the value type
	 */
	@Override
	public Class<?> getColumnClass(int col) {
		if (getValueAt(0, col) != null)
			return getValueAt(0, col).getClass();

		return String.class;/*
							 * switch (col) { case 0: case 1: return String.class; case 2: return Float.class; case 3: return String.class;
							 * case 4: return Boolean.class; default: return Boolean.class; }
							 */
	}

	private Object getColValue(final Symbol symbol, final int column) {
		int i = 0;

		// TODO Manage Date better
		HistoricalPrice h = symbol.getPrice(DateUtil.getCurrentYear());

		if (column == i++) {
			return symbol.getTicker();
		} else if (column == i++) {
			return symbol.getName();
		} else if (column == i++) {
			return symbol.isCustomPriceFirst() && h.getCustomPrice() != BigDecimal.ZERO ? h.getCustomPrice() : h.getPrice();
		} else if (column == i++) {
			return symbol.getCurrency();
		} else if (column == i++) {
			return symbol.getSector();
		} else if (column == i++) {
			return new Boolean(symbol.isCustomPriceFirst());
		}
		throw new AssertionError();
	}

	private void setColValue(final Symbol s, Object value, int column) {
		int i = 0;
		if (column == i++) {
			String oldValue = s.getTicker();
			s.setTicker((String) value);
			ViewControllers.getController().addSymbol(s);
			//TODO do not compare like this ==> Fixed
			if(!ViewControllers.getController().addSymbol(s).equals(s)){
				s.setTicker(oldValue);
				JOptionPane.showConfirmDialog(ViewControllers.getWindow(), THE_SYMBOL_ALREADY_EXIST, CANNOT_ADD_SYMBOL,
				        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
			}
		} else {
			if (column == i++) {
				s.setName((String) value);
			} else if (column == i++) {
				// current price
				throw new AssertionError();
			} else if (column == i++) {
				s.setCurrency((CurrencyEnum) value);
			}else if (column == i++) {
				s.setSector((SectorEnum) value);
			} else if (column == i++) {
				s.setCustomPriceFirst((Boolean) value);
			}
			ViewControllers.getController().updateSymbol(s);
		}
	}
}
