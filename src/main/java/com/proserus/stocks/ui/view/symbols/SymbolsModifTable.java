package com.proserus.stocks.ui.view.symbols;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventBus;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.ModelChangeEvents;
import com.proserus.stocks.ui.view.common.AbstractDialog;
import com.proserus.stocks.ui.view.common.AbstractTable;
import com.proserus.stocks.ui.view.general.ColorSettingsDialog;
import com.proserus.stocks.ui.view.general.TableBigDecimalFieldEditor;
import com.proserus.stocks.ui.view.transactions.TableRender;

public class SymbolsModifTable extends AbstractTable implements EventListener, KeyListener {
	private static final long serialVersionUID = 201404042021L;
	private static final String ONE = "1";

	private static final String ZERO = "0";

	private SymbolsModifModel tableModel;;
	private TableRender tableRender = new TableRender();
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

		TableBigDecimalFieldEditor numberEditor = new TableBigDecimalFieldEditor(true);
		getColumnModel().getColumn(2).setCellEditor(numberEditor);

		EventBus.getInstance().add(this, ModelChangeEvents.SYMBOLS_UPDATED);

		addKeyListener(this);

		setFirstRowSorted();
		validate();
	}

	@Override
	public void update(Event event, Object model) {
		if (ModelChangeEvents.SYMBOLS_UPDATED.equals(event)) {
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
		return tableRender;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		((AbstractDialog) getParent().getParent().getParent().getParent().getParent().getParent()).keyPressed(arg0);
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
