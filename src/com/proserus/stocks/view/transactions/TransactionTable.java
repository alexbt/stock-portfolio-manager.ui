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
import com.proserus.stocks.bp.SymbolsBp;
import com.proserus.stocks.bp.TransactionsBp;
import com.proserus.stocks.controllers.iface.PortfolioController;
import com.proserus.stocks.model.symbols.Symbol;
import com.proserus.stocks.model.transactions.Transaction;
import com.proserus.stocks.model.transactions.TransactionType;
import com.proserus.stocks.view.common.AbstractTable;
import com.proserus.stocks.view.common.SortedComboBoxModel;
import com.proserus.stocks.view.common.ViewControllers;
import com.proserus.stocks.view.common.verifiers.DateVerifier;
import com.proserus.stocks.view.general.ColorSettingsDialog;
import com.proserus.stocks.view.general.LabelsList;

public class TransactionTable extends AbstractTable implements Observer, ActionListener, MouseListener {

	private static final String ONE = "1";

	private static final String ZERO = "0";

	private PortfolioController controller = ViewControllers.getController();

	private TransactionTableModel tableModel = new TransactionTableModel();
	private LabelsList labl = null;
	// http://72.5.124.102/thread.jspa?messageID=4220319
	private TableCellRenderer renderer = new PrecisionCellRenderer(2);
	private TableRowSorter<TransactionTableModel> sorter;
	HashMap<String, Color> colors = new HashMap<String, Color>();

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
		sportColumn = getColumnModel().getColumn(0);
		sportColumn.setCellEditor(new MyDateEditor());
		setVisible(true);
		setFirstRowSorted(false);
		addMouseListener(this);
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		return renderer;
	}

	@Override
	public void update(Observable arg0, Object UNUSED) {
		if (arg0 instanceof TransactionsBp) {
			tableModel.setData(controller.getTransactions(ViewControllers.getSharedFilter()).toArray());
			getRootPane().validate();
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
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (value instanceof Float) {
				setText(format.format(value));
			} else if (value instanceof Date) {
				setText(dateFormat.format(value));
			} else if (value instanceof BigDecimal) {
				setText(format.format(value));
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
			c.setBackground(ColorSettingsDialog.getColor(ViewControllers.getSharedFilter().isFiltered()));
		} else {
			c.setBackground(ColorSettingsDialog.getAlternateRowColor());
		}
		return c;
	}

	@Override
	public void mouseClicked(MouseEvent evt) {
		if(getSelectedRow() < 0){
			return;
		}
		
		if (getSelectedColumn() == 7 && evt.getButton() == MouseEvent.BUTTON1) {
			Transaction t = sorter.getModel().getTransaction(sorter.convertRowIndexToModel(getSelectedRow()));
			JWindow window = new JWindow(ViewControllers.getWindow());
			labl = new LabelsList(t, window);
			window.add(labl);

			window.setSize(150, 117);
			Point p = MouseInfo.getPointerInfo().getLocation();
			window.setLocation(p);
			window.setVisible(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JComboBox) {
			Object o = ((JComboBox) e.getSource()).getSelectedItem();
			if (o instanceof Symbol) {
				FilterBp filter = ViewControllers.getSharedFilter();
				if (((Symbol) o).getId() != null) {
					filter.setSymbol((Symbol) o);
				} else {
					filter.setSymbol(null);
				}
			}
		}
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
		
		if(!evt.isAltGraphDown() && !evt.isControlDown()){
			Transaction t = sorter.getModel().getTransaction(sorter.convertRowIndexToModel(getSelectedRow()));
			controller.setSelection(t);
		}
    }

	@Override
    public void mouseReleased(MouseEvent e) {
    }

}


class MyDateEditor extends DefaultCellEditor {
	/**
     * 
     */
    private static final long serialVersionUID = 201108112016L;
    
	DateFormat parseFormat;
	DateFormat editFormat;
	Date data;
	DateVerifier dateVerifier = new DateVerifier();

	public MyDateEditor() {
		// TODO use INputVerifier ? DateVerifier
		super(new JTextField());
		getComponent().setName("Table.editor");
		// TODO should actually check for null
		parseFormat = new SimpleDateFormat("yyyyMMdd");
		editFormat = new SimpleDateFormat("yyyy-MM-dd");
	}

	@Override
	public boolean stopCellEditing() {
		String str = (String) super.getCellEditorValue();
		if ("".equals(str)) {
			super.stopCellEditing();
		}
		str = str.replaceAll("[^0-9]", "");
		try {
			data = parseFormat.parse(str);
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
