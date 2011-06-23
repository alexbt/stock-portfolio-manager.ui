package com.proserus.stocks.view.symbols;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import com.proserus.stocks.bp.SymbolsBp;
import com.proserus.stocks.model.symbols.Symbol;
import com.proserus.stocks.view.common.AbstractDialog;
import com.proserus.stocks.view.common.AbstractEditableTable;
import com.proserus.stocks.view.common.ViewControllers;
import com.proserus.stocks.view.general.ColorSettingsDialog;

public class SymbolsModifTable extends AbstractEditableTable implements Observer, KeyListener {
	private static final String CANNOT_REMOVE_SYMBOL = "Cannot remove symbol";

	private static final String THE_SYMBOL_IS_CURRENTLY_USED_IN_TRANSACTIONS = "The symbol is currently used in transactions";

	private static final String ONE_SPACE = " ";

	private static final String ONE = "1";

	private static final String ZERO = "0";

	private SymbolsModifModel tableModel;;
	private TableCellRenderer renderer = new PrecisionCellRenderer(2);
	private HashMap<String, Color> colors = new HashMap<String, Color>();
	private TableRowSorter<SymbolsModifModel> sorter;
	private boolean filtered = false;
	private Symbol symbol;

	public SymbolsModifTable(Symbol symbol) {
		this.symbol = symbol;
		colors.put(ZERO + true, new Color(150, 190, 255));
		colors.put(ZERO + false, new Color(255, 148, 0));
		colors.put(ONE + true, new Color(245, 245, 245));
		colors.put(ONE + false, new Color(245, 245, 245));

		tableModel = new SymbolsModifModel();
		tableModel.setData(symbol.getHistoricalPricesValues().toArray());
		setModel(tableModel);
		sorter = new TableRowSorter<SymbolsModifModel>(tableModel);
		setRowSorter(sorter);
		setRowHeight(getRowHeight() + 5);
		setVisible(true);
		
		ViewControllers.getController().addSymbolsObserver(this);

		addKeyListener(this);

		setFirstRowSorted();
		validate();
	}

	@Override
	public void update(Observable arg0, Object UNUSED) {
		if(arg0 instanceof SymbolsBp){
			tableModel.setData(symbol.getHistoricalPricesValues().toArray());
		}
	}
	

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
		Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
		if (getSelectedRow() == rowIndex) {
			c.setBackground(ColorSettingsDialog.getTableSelectionColor());
		} else if (rowIndex % 2 == 0) {
			c.setBackground(ColorSettingsDialog.getColor(filtered));
		} else {
			c.setBackground(ColorSettingsDialog.getAlternateRowColor());
		}

		return c;
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		return renderer;
	}

	private static class PrecisionCellRenderer extends DefaultTableCellRenderer {
		private NumberFormat format;

		PrecisionCellRenderer(int precision) {
			format = NumberFormat.getNumberInstance();
			format.setMaximumFractionDigits(precision);
			format.setMinimumFractionDigits(precision);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
		        boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (value instanceof Float) {
				setText(format.format(value));
			}if (value instanceof BigDecimal) {
				setText(format.format(value));
			}
			return this;
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		((AbstractDialog)getParent().getParent().getParent().getParent().getParent().getParent()).keyPressed(arg0);
	}

	@Override
	protected void delete() {
		// if (symbolController.remove(tableModel.get(getRowSorter().convertRowIndexToModel(getSelectedRow())))) {
		// } else {
		// JOptionPane.showConfirmDialog(this, THE_SYMBOL_IS_CURRENTLY_USED_IN_TRANSACTIONS, CANNOT_REMOVE_SYMBOL,
		// JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
		// }
	}

	@Override
	public void mouseClicked(MouseEvent evt) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

}
