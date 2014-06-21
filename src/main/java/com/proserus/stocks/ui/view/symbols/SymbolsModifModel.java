package com.proserus.stocks.ui.view.symbols;

import java.math.BigDecimal;

import javax.swing.table.AbstractTableModel;

import com.proserus.stocks.bo.symbols.HistoricalPrice;
import com.proserus.stocks.bp.utils.DateUtil;
import com.proserus.stocks.ui.controller.ViewControllers;

public class SymbolsModifModel extends AbstractTableModel {
	private static final long serialVersionUID = 20080113L;

	public static final String[] COLUMN_NAMES = { "Year", "Price", "Custom Price" };

	private Object[] data = null;

	/**
	 * Default constructor to create the Table Model for the contractor table.
	 */
	public SymbolsModifModel() {
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		// Note that the data/cell address is constant,
		// no matter where the cell appears onscreen.
		if (col == 2) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Replace the list of contractors to be displayed in the table.
	 * 
	 * @param data
	 *            Array of contractors (as Object).
	 * @see Contractor
	 */
	public void setData(final Object[] data) {
		if (data != null && data.length > 0 && !(data[0] instanceof HistoricalPrice)) {
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

	public HistoricalPrice get(int rowIndex) {
		return (HistoricalPrice) data[rowIndex];
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
			return (getColValue((HistoricalPrice) data[rowIndex], columnIndex));
		}
		return "";
	}

	@Override
	public void setValueAt(Object obj, int row, int col) {
		setColValue((HistoricalPrice) data[row], obj, col);
		fireTableCellUpdated(row, col);
	}

	/**
	 * Invoked when the table is displayed, to identify the value type
	 */
	@Override
	public Class<?> getColumnClass(int col) {
		return getValueAt(0, col).getClass();/*
											 * switch (col) { case 0: case 1: return String.class; case 2: return Float.class; case 3:
											 * return String.class; case 4: return Boolean.class; default: return Boolean.class; }
											 */
	}

	private Object getColValue(final HistoricalPrice h, final int column) {
		int i = 0;
		if (column == i++) {
			//TODO Manage Date better
			return h.getYear().equals(DateUtil.getCurrentYear()) ? "Today" : String.valueOf(h.getYear());
		} else if (column == i++) {
			return h.getPrice();
		} else if (column == i++) {
			return h.getCustomPrice();
		}
		throw new AssertionError();
	}

	private void setColValue(HistoricalPrice h, Object value, int column) {
		h.setCustomPrice((BigDecimal)value);
		ViewControllers.getController().update(h);
	}
}
