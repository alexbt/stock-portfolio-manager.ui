package com.proserus.stocks.view.summaries;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.proserus.stocks.bp.SharedFilter;
import com.proserus.stocks.controllers.PortfolioControllerImpl;
import com.proserus.stocks.controllers.iface.PortfolioController;
import com.proserus.stocks.view.common.AbstractTable;
import com.proserus.stocks.view.general.ColorSettingsDialog;

public class PerformanceCurrencyTable extends AbstractTable implements Observer {
	private static final int ROW_SIZE = 21;

	private static final String ONE = "1";

	private static final String ZERO = "0";

	private PortfolioController controller = PortfolioControllerImpl.getInstance();

	private PerformanceCurrencyModel tableModel = new PerformanceCurrencyModel();
	private TableCellRenderer renderer = new PrecisionCellRenderer(2);
	HashMap<String, Color> colors = new HashMap<String, Color>();
	private boolean filtered = false;

	private static PerformanceCurrencyTable currencySummary = new PerformanceCurrencyTable();

	static public PerformanceCurrencyTable getInstance() {
		return currencySummary;
	}

	private PerformanceCurrencyTable() {
		colors.put(ZERO + true, new Color(150, 190, 255));
		colors.put(ZERO + false, new Color(255, 148, 0));
		colors.put(ONE + true, new Color(245, 245, 245));
		colors.put(ONE + false, new Color(245, 245, 245));
		setModel(tableModel);
		controller.addAnalysisObserver(this);
		// setSize(400, 30);
		setPreferredScrollableViewportSize(new Dimension(200, ROW_SIZE * tableModel.getRowCount()));
		setRowHeight(getRowHeight() + 5);
		setVisible(true);
		validate();
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		return renderer;
	}

	@Override
	public void update(Observable arg0, Object UNUSED) {
		Collection col = controller.getCurrencyAnalysis(SharedFilter.getInstance());
		// TODO Redesign FIlter/SharedFilter
		tableModel.setData(col);
		setPreferredScrollableViewportSize(new Dimension(200, ROW_SIZE * col.size()));
		setToolTipText(col.toString());
		validate();
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

	private static class PrecisionCellRenderer extends DefaultTableCellRenderer {
		private static final String PERCENT_WITH_PARENTHESIS = "(%)";
		private static final String EMPTY_STR = "";
		private static final String PERCENT = "%";
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
			if (value instanceof BigDecimal) {
				setAlignmentX(RIGHT_ALIGNMENT);
				if (((BigDecimal) value).equals(BigDecimal.ZERO)) {
					setText(EMPTY_STR);
					return this;
				}
				setText(format.format(value));
			}
			if (table.getColumnName(column).contains(PERCENT_WITH_PARENTHESIS)) {
				setText(getText() + PERCENT);
			}
			return this;
		}
	}

	@Override
	protected void delete() {

	}
}
