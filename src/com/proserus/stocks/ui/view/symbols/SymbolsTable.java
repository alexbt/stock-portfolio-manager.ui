package com.proserus.stocks.ui.view.symbols;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import com.proserus.stocks.bo.symbols.CurrencyEnum;
import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventBus;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.SwingEvents;
import com.proserus.stocks.ui.controller.PortfolioController;
import com.proserus.stocks.ui.controller.ViewControllers;
import com.proserus.stocks.ui.view.actions.ShowEditSymbolAction;
import com.proserus.stocks.ui.view.common.AbstractTable;
import com.proserus.stocks.ui.view.general.ColorSettingsDialog;

public class SymbolsTable extends AbstractTable implements EventListener, KeyListener,MouseListener {
	private static final String ONE = "1";

	private static final String ZERO = "0";

	private PortfolioController controller = ViewControllers.getController();

	private SymbolsTableModel tableModel;;
	private TableCellRenderer renderer = new PrecisionCellRenderer(2);
	private HashMap<String, Color> colors = new HashMap<String, Color>();
	private TableRowSorter<SymbolsTableModel> sorter;
	private AbstractAction openSymbol = new ShowEditSymbolAction();
	private static SymbolsTable symbolTable = new SymbolsTable();

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
		EventBus.getInstance().add(this, SwingEvents.SYMBOLS_UPDATED);
		EventBus.getInstance().add(this, SwingEvents.TRANSACTION_UPDATED);
		setRowHeight(getRowHeight() + 5);
		setVisible(true);

		TableColumn sportColumn = getColumnModel().getColumn(3);
		JComboBox comboBox = new JComboBox();
		for (CurrencyEnum cur : CurrencyEnum.values()) {
			comboBox.addItem(cur);
		}
		
		comboBox.setMaximumRowCount(11);
		sportColumn.setCellEditor(new DefaultCellEditor(comboBox));
		getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JCheckBox()));
		addKeyListener(this);

		setFirstRowSorted();
		addMouseListener(this);
		validate();
	}

	@Override
	public void update(Event event, Object model) {
		if (SwingEvents.SYMBOLS_UPDATED.equals(event)) {
			Collection<Symbol> col = SwingEvents.SYMBOLS_UPDATED.resolveModel(model);
			Object[] array = col.toArray().length == 0 ? null : col.toArray();
			tableModel.setData(array);
//			getRootPane().validate();
			tableModel.fireTableDataChanged();
		}
	}

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
		Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
		if (getSelectedRow() == rowIndex) {
			c.setBackground(ColorSettingsDialog.getTableSelectionColor());
		} else if (rowIndex % 2 == 0) {
			c.setBackground(ColorSettingsDialog.getColor(false));
		} else {
			c.setBackground(ColorSettingsDialog.getAlternateRowColor());
		}

		return c;
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		return renderer;
	}

	@Override
	public void mouseClicked(MouseEvent evt) {
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
			}else if (value instanceof BigDecimal) {
				setText(format.format(value));
			}
			return this;
		}
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
		if(getSelectedRow() < 0){
			return;
		}
		int row = getRowSorter().convertRowIndexToModel(getSelectedRow());
		Symbol symbol = tableModel.getSymbol(row);
		
		if(!evt.isAltGraphDown() && !evt.isControlDown()){
			controller.setSelection(symbol);
		}
		if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2 && getSelectedColumn() == 2) {
			openSymbol.actionPerformed(null);
		}
    }

	@Override
    public void mouseReleased(MouseEvent e) {
    }
}
