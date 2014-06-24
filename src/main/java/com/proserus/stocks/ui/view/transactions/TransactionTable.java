package com.proserus.stocks.ui.view.transactions;

import java.awt.Color;
import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bo.transactions.Transaction;
import com.proserus.stocks.bo.transactions.TransactionType;
import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventBus;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.ModelChangeEvents;
import com.proserus.stocks.bp.model.Filter;
import com.proserus.stocks.bp.utils.DateUtils;
import com.proserus.stocks.ui.controller.PortfolioController;
import com.proserus.stocks.ui.controller.ViewControllers;
import com.proserus.stocks.ui.view.common.AbstractTable;
import com.proserus.stocks.ui.view.common.SortedComboBoxModel;
import com.proserus.stocks.ui.view.general.ColorSettingsDialog;
import com.proserus.stocks.ui.view.general.LabelsList;

public class TransactionTable extends AbstractTable implements EventListener, ActionListener, MouseListener {
	private static final long serialVersionUID = 201404042021L;
	private Filter filter = ViewControllers.getFilter();

	private static final String ONE = "1";

	private static final String ZERO = "0";

	private PortfolioController controller = ViewControllers.getController();

	private TransactionTableModel tableModel = new TransactionTableModel();
	private TableRender tableRender = new TableRender();
	private LabelsList labl = null;
	// http://72.5.124.102/thread.jspa?messageID=4220319
	private TableRowSorter<TransactionTableModel> sorter;
	HashMap<String, Color> colors = new HashMap<String, Color>();

	private static TransactionTable transactionTable = new TransactionTable();
	private SortedComboBoxModel comboTickers = new SortedComboBoxModel();

	static public TransactionTable getInstance() {
		return transactionTable;
	}

	private TransactionTable() {
		EventBus.getInstance().add(this, ModelChangeEvents.TRANSACTION_UPDATED, ModelChangeEvents.SYMBOLS_UPDATED);
		setModel(tableModel);
		colors.put(ZERO + true, new Color(150, 190, 255));
		colors.put(ZERO + false, new Color(255, 148, 0));
		colors.put(ONE + true, new Color(245, 245, 245));
		colors.put(ONE + false, new Color(245, 245, 245));
		sorter = new TableRowSorter<TransactionTableModel>(tableModel);
		setRowSorter(sorter);
		setRowHeight(getRowHeight() + 5);
		setBorder(null);
		TableColumn sportColumn = getColumnModel().getColumn(2);

		JComboBox comboBox = new JComboBox();
		comboBox.setRenderer(new ComboRender());
		comboBox.setEditor(new ComboEditor());
		for (TransactionType transactionType : TransactionType.values()) {
			if (transactionType.isVisible()) {
				comboBox.addItem(transactionType);
			}
		}

		sportColumn.setCellEditor(new DefaultCellEditor(comboBox));
		sportColumn = getColumnModel().getColumn(0);
		sportColumn.setCellEditor(new MyDateEditor());
		// setDefaultEditor(Symbol.class, editor);
		setVisible(true);
		setFirstRowSorted(false);
		addMouseListener(this);
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		return tableRender;
	}

	@Override
	public void editingStopped(ChangeEvent e) {
		super.editingStopped(e);
	}

	@Override
	public void update(Event event, Object model) {
		if (ModelChangeEvents.TRANSACTION_UPDATED.equals(event)) {
			tableModel.setData(ModelChangeEvents.TRANSACTION_UPDATED.resolveModel(model).toArray());
			getRootPane().validate();
			// TODO Redesign Filter/SharedFilter
		} else if (ModelChangeEvents.SYMBOLS_UPDATED.equals(event)) {
			TableColumn sportColumn = getColumnModel().getColumn(1);
			JComboBox comboBox = new JComboBox(comboTickers);
			comboBox.setRenderer(new ComboRender());
			comboBox.setEditor(new ComboEditor());

			if (comboTickers.getSize() > 0) {
				getSelectionModel().clearSelection();
				comboTickers.removeAllElements();
			}
			for (Symbol symbol : ModelChangeEvents.SYMBOLS_UPDATED.resolveModel(model)) {
				comboTickers.addElement(symbol);
			}

			sportColumn.setCellEditor(new NotNullEditor(comboBox));
		}
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
	public void mouseClicked(MouseEvent evt) {
		if (getSelectedRow() < 0) {
			return;
		}

		if (getSelectedColumn() == 7 && evt.getButton() == MouseEvent.BUTTON1) {
			Transaction t = sorter.getModel().getTransaction(sorter.convertRowIndexToModel(getSelectedRow()));
			JWindow window = new JWindow(ViewControllers.getWindow());
			labl = new LabelsList(t, window);
			window.add(labl);

			window.setSize(200, 300);
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
				if (((Symbol) o).getId() != null) {
					filter.setSymbol((Symbol) o);
				} else {
					filter.setSymbol(null);
				}
				ViewControllers.getController().refreshFilteredData();
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
		if (getSelectedRow() < 0) {
			return;
		}

		if (!evt.isAltGraphDown() && !evt.isControlDown()) {
			Transaction t = sorter.getModel().getTransaction(sorter.convertRowIndexToModel(getSelectedRow()));
			controller.setSelection(t);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}

class MyDateEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 201108112016L;

	private DateFormat parseFormat = new SimpleDateFormat("yyyyMMdd");
	private DateFormat editFormat = new SimpleDateFormat("yyyy-MM-dd");
	private Calendar data;

	public MyDateEditor() {
		// TODO use INputVerifier ? DateVerifier
		super(new JTextField());
		getComponent().setName("Table.editor");
	}

	@Override
	public boolean stopCellEditing() {
		String str = (String) super.getCellEditorValue();
		if ("".equals(str)) {
			super.stopCellEditing();
		}
		str = str.replaceAll("[^0-9]", "");
		try {
			data = DateUtils.getCalendar(parseFormat.parse(str));
		} catch (ParseException e) {
			((JComponent) getComponent()).setBorder(new LineBorder(Color.red));
			return false;
		}
		return super.stopCellEditing();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.data = null;
		if (value instanceof Calendar) {
			value = editFormat.format(((Calendar) value).getTime());
		}
		((JComponent) getComponent()).setBorder(new LineBorder(Color.black));
		return super.getTableCellEditorComponent(table, value, isSelected, row, column);
	}

	@Override
	public Object getCellEditorValue() {
		return data;
	}
}

class NotNullEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 201108112016L;

	public NotNullEditor(JComboBox combobox) {
		super(combobox);
	}

	@Override
	public boolean stopCellEditing() {
		JComboBox combo = (JComboBox) getComponent();
		if (combo.getSelectedItem() == null) {
			return false;
		}
		return super.stopCellEditing();
	}
}
