package com.proserus.stocks.view.transactions;

import java.awt.Color;
import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import com.proserus.stocks.bp.FilterBp;
import com.proserus.stocks.bp.SharedFilter;
import com.proserus.stocks.bp.SymbolsBp;
import com.proserus.stocks.bp.TransactionsBp;
import com.proserus.stocks.controllers.PortfolioControllerImpl;
import com.proserus.stocks.controllers.iface.PortfolioController;
import com.proserus.stocks.model.symbols.Symbol;
import com.proserus.stocks.model.transactions.TransactionType;
import com.proserus.stocks.view.common.AbstractEditableTable;
import com.proserus.stocks.view.common.SortedComboBoxModel;
import com.proserus.stocks.view.common.verifiers.DateVerifier;
import com.proserus.stocks.view.general.ColorSettingsDialog;
import com.proserus.stocks.view.general.LabelsList;
import com.proserus.stocks.view.general.Window;

public class TransactionTable extends AbstractEditableTable implements Observer, ActionListener, MouseListener {

	private static final String ONE = "1";

	private static final String ZERO = "0";

	private PortfolioController controller = PortfolioControllerImpl.getInstance();

	private TransactionTableModel tableModel = new TransactionTableModel();
	private LabelsList labl = null;
	// http://72.5.124.102/thread.jspa?messageID=4220319
	private TableCellRenderer renderer = new PrecisionCellRenderer(2);
	private TableRowSorter<TransactionTableModel> sorter;
	private String selectedSymbol = "";
	private String selectedYear = "";
	private String[] custom = new String[] {};
	private String[] selectedLabels = new String[] {};
	HashMap<String, Color> colors = new HashMap<String, Color>();
	private boolean filtered = false;

	private static TransactionTable transactionTable = new TransactionTable();
	private SortedComboBoxModel comboTickers = new SortedComboBoxModel();

	static public TransactionTable getInstance() {
		return transactionTable;
	}

	private TransactionTable() {
		setModel(tableModel);
		colors.put(ZERO + true, new Color(150, 190, 255));
		colors.put(ZERO + false, new Color(255, 148, 0));
		colors.put(ONE + true, new Color(245, 245, 245));
		colors.put(ONE + false, new Color(245, 245, 245));
		sorter = new TableRowSorter<TransactionTableModel>(tableModel);
		setRowSorter(sorter);
		controller.addTransactionObserver(this);
		controller.addTransactionsObserver(this);
		controller.addSymbolsObserver(this);
		setRowHeight(getRowHeight() + 5);
		setBorder(null);
		TableColumn sportColumn = getColumnModel().getColumn(2);

		JComboBox comboBox = new JComboBox();
		for (TransactionType transactionType : TransactionType.values()) {
			comboBox.addItem(transactionType);
		}

		sportColumn.setCellEditor(new DefaultCellEditor(comboBox));

		// getColumnModel().getColumn(getColumnCount()-1).set
		// getColumnModel().getColumn(getColumnCount()-1).setCellEditor(new
		// DefaultCellEditor(new JTextField()));

		sportColumn = getColumnModel().getColumn(0);

		// getColumnModel().getColumn(6).getagetInputMap().put(KeyStroke.getKeyStroke("F2"),
		// .
		// evt.getSource()evt.

		sportColumn.setCellEditor(new MyDateEditor());
		// TableCellEditor ch = new LabelsEditor(c);
		// sportColumn.setCellEditor(ch);
		// setSize(600, 400);
		setVisible(true);

		setFirstRowSorted(false);

	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		return renderer;
	}

	@Override
	public void update(Observable arg0, Object UNUSED) {
		if (arg0 instanceof TransactionsBp) {
			tableModel.setData(controller.getTransactions(SharedFilter.getInstance()).toArray());
			// TODO Redesign Filter/SharedFilter
		} else if (arg0 instanceof SymbolsBp) {
			TableColumn sportColumn = getColumnModel().getColumn(1);
			JComboBox comboBox = new JComboBox(comboTickers);
			comboTickers.removeAllElements();
			for (Symbol symbol : controller.getSymbols()) {
				comboTickers.addElement(symbol);
			}
			sportColumn.setCellEditor(new DefaultCellEditor(comboBox));
		}
	}

	private static class PrecisionCellRenderer extends DefaultTableCellRenderer {
		private NumberFormat format;
		private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

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
			} else if (value instanceof Date) {
				setText(dateFormat.format(value));
			}else if (value instanceof BigDecimal) {
				setText(format.format(value));
			}

			if (row % 2 != 0) {
				// Component c = super.prepareRenderer(this, row, column);
				// c.setBackground(Color.yellow);
			}
			return this;
		}
	}

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
		Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
		if (getSelectedRow() == rowIndex) {
			c.setBackground(ColorSettingsDialog.getTableSelectionColor());
		} else if (rowIndex % 2 == 0) {
			c.setBackground(ColorSettingsDialog.getColor(SharedFilter.getInstance().isFiltered()));
		} else {
			c.setBackground(ColorSettingsDialog.getAlternateRowColor());
		}
		return c;
	}

	@Override
	public void mouseClicked(MouseEvent evt) {
		super.mouseClicked(evt);
		if (getSelectedColumn() == 7 && evt.getButton() == MouseEvent.BUTTON1) {
			getModel().getValueAt(getSelectedRow(), 7);
			// ((JComponent)evt.getSource()).setFocusable(false);
			// getColumnModel().getColumn(6).getCellEditor().cancelCellEditing();
			JWindow jj = new JWindow(Window.getInstance());
			labl = new LabelsList(sorter.getModel().getTransaction(sorter.convertRowIndexToModel(getSelectedRow())),
			        true, true, jj, false);
			jj.add(labl);

			jj.setSize(150, 117);
			// jj.requestFocus();
			Point p = MouseInfo.getPointerInfo().getLocation();
			jj.setLocation(p);
			jj.setVisible(true);
			// this.clearSelection();

			// jj.grabFocus();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
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

	@Override
	protected void delete() {
		controller.remove(sorter.getModel().getTransaction(
		        getRowSorter().convertRowIndexToModel(getSelectedRow())));
	}
}

class DateCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
	        int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (value instanceof Date) {
			// Use SimpleDateFormat class to get a formatted String from Date
			// object.
			String strDate = new SimpleDateFormat("MM/dd/yyyy").format((Date) value);
			// Sorting algorithm will work with model value. So you dont need to
			// worry
			// about the renderer's display value.
			setText(strDate);
		}

		return this;
	}

	public DateCellRenderer() {
		super();
	}
}
	class MyDateEditor extends DefaultCellEditor {
	DateFormat parseFormat;
	DateFormat editFormat;
	Date data;
	DateVerifier dateVerifier = new DateVerifier();
	public MyDateEditor() {
		//TODO use INputVerifier ? DateVerifier
		super(new JTextField());
		getComponent().setName("Table.editor");
		// TODO should actually check for null
		parseFormat = new SimpleDateFormat("yyyyMMdd");
		editFormat = new SimpleDateFormat("yyyy-MM-dd");
	}

	@Override
	public boolean stopCellEditing() {
		String s = (String) super.getCellEditorValue();
		if ("".equals(s)) {
			super.stopCellEditing();
		}
		s = s.replaceAll("[^0-9]", "");
		try {
			data  = parseFormat.parse(s);
		} catch (ParseException e) {
			((JComponent) getComponent()).setBorder(new LineBorder(Color.red));
			return false;
		}
		return super.stopCellEditing();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.data = null;
		if (value instanceof Date) {
			value = editFormat.format((Date) value);
		}
		((JComponent) getComponent()).setBorder(new LineBorder(Color.black));
		return super.getTableCellEditorComponent(table, value, isSelected, row, column);
	}

	@Override
	public Object getCellEditorValue() {
		return data;
	}
}
