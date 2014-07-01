package com.proserus.stocks.ui.view.symbols;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import com.proserus.stocks.bo.symbols.CurrencyEnum;
import com.proserus.stocks.bo.symbols.SectorEnum;
import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventBus;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.ModelChangeEvents;
import com.proserus.stocks.bp.model.Filter;
import com.proserus.stocks.ui.controller.PortfolioController;
import com.proserus.stocks.ui.controller.ViewControllers;
import com.proserus.stocks.ui.view.actions.ShowEditSymbolAction;
import com.proserus.stocks.ui.view.common.AbstractTable;
import com.proserus.stocks.ui.view.general.ColorSettingsDialog;
import com.proserus.stocks.ui.view.general.ComboBoxModelEditor;
import com.proserus.stocks.ui.view.general.ComboBoxModelRenderer;
import com.proserus.stocks.ui.view.transactions.TableRender;

public class SymbolsTable extends AbstractTable implements EventListener, KeyListener, MouseListener {
	private static final long serialVersionUID = 201404042021L;
	private static final String ONE = "1";

	private static final String ZERO = "0";
	private Filter filter = ViewControllers.getFilter();
	private PortfolioController controller = ViewControllers.getController();
	private TableRender tableRender = new TableRender();

	private SymbolsTableModel tableModel;
	private HashMap<String, Color> colors = new HashMap<String, Color>();
	private TableRowSorter<SymbolsTableModel> sorter;
	private AbstractAction openSymbol = ShowEditSymbolAction.getInstance();
	private static SymbolsTable symbolTable = new SymbolsTable();
	private Symbol selectSymbol = null;

	static public SymbolsTable getInstance() {
		return symbolTable;
	}

	private SymbolsTable() {
		colors.put(ZERO + true, new Color(150, 190, 255));
		colors.put(ZERO + false, new Color(255, 148, 0));
		colors.put(ONE + true, new Color(245, 245, 245));
		colors.put(ONE + false, new Color(245, 245, 245));
		tableModel = new SymbolsTableModel();
		setModel(tableModel);
		sorter = new TableRowSorter<SymbolsTableModel>(tableModel);
		setRowSorter(sorter);
		EventBus.getInstance().add(this, ModelChangeEvents.FILTER_SYMBOLS);
		EventBus.getInstance().add(this, ModelChangeEvents.SYMBOLS_UPDATED);
		EventBus.getInstance().add(this, ModelChangeEvents.TRANSACTION_UPDATED);
		setRowHeight(getRowHeight() + 5);
		setVisible(true);
		TableColumn sportColumn = getColumnModel().getColumn(3);
		JComboBox comboBox = new JComboBox();
		comboBox.setRenderer(new ComboBoxModelRenderer());
		comboBox.setEditor(new ComboBoxModelEditor());
		for (CurrencyEnum cur : CurrencyEnum.values()) {
			if (cur.isVisible()) {
				comboBox.addItem(cur);
			}
		}
		comboBox.setRenderer(new ComboBoxModelRenderer());
		comboBox.setMaximumRowCount(12);
		sportColumn.setCellEditor(new DefaultCellEditor(comboBox));

		TableColumn sportColumnSector = getColumnModel().getColumn(4);
		JComboBox comboBoxSector = new JComboBox();
		comboBoxSector.setRenderer(new ComboBoxModelRenderer());
		comboBoxSector.setEditor(new ComboBoxModelEditor());
		for (SectorEnum cur : SectorEnum.retrieveSortedSectors()) {
			comboBoxSector.addItem(cur);
		}
		comboBoxSector.setMaximumRowCount(12);
		sportColumnSector.setCellEditor(new DefaultCellEditor(comboBoxSector));

		int i = 0;
		for (String val : SymbolsTableModel.COLUMN_NAMES) {
			if (val.contains("Custom")) {
				getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor(new JCheckBox()));
				break;
			}
			i++;
		}
		getSelectionModel().addListSelectionListener(this);

		tableModel.addTableModelListener(this);
		addKeyListener(this);
		setFirstRowSorted();
		addMouseListener(this);
		validate();
	}

	@Override
	public void update(Event event, Object model) {
		if (ModelChangeEvents.FILTER_SYMBOLS.equals(event)) {
			updateSymbolTable(ModelChangeEvents.FILTER_SYMBOLS.resolveModel(model));
		} else if (ModelChangeEvents.SYMBOLS_UPDATED.equals(event)) {
			updateSymbolTable(ModelChangeEvents.SYMBOLS_UPDATED.resolveModel(model));
		}
	}

	private void updateSymbolTable(Collection<Symbol> col) {
		Object[] array = col.toArray().length == 0 ? null : col.toArray();
		tableModel.setData(array);
		tableModel.fireTableDataChanged();
	}

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
		Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
		if (getSelectedRow() == rowIndex) {
			c.setBackground(ColorSettingsDialog.getTableSelectionColor());
		} else if (rowIndex % 2 == 0) {
			c.setBackground(ColorSettingsDialog.getColor(filter.isFiltered()));
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
	public void mouseClicked(MouseEvent evt) {
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_DELETE && getSelectedRow() > -1) {

		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent evt) {
		if (getSelectedRow() < 0) {
			return;
		}
		if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2 && getSelectedColumn() == 2) {
			openSymbol.actionPerformed(null);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		super.valueChanged(e);
		if (getSelectedRow() < 0) {
			return;
		}
		int row = getRowSorter().convertRowIndexToModel(getSelectedRow());
		Symbol symbol = tableModel.getSymbol(row);
		if (!symbol.equals(selectSymbol)) {
			selectSymbol = symbol;
			controller.setSelection(selectSymbol);
		}
	}
}
