package com.proserus.stocks.ui.view.summaries;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventBus;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.SwingEvents;
import com.proserus.stocks.bp.model.Filter;
import com.proserus.stocks.ui.controller.PortfolioController;
import com.proserus.stocks.ui.view.common.AbstractTable;
import com.proserus.stocks.ui.view.common.ViewControllers;
import com.proserus.stocks.ui.view.general.ColorSettingsDialog;
public class PerformanceSymbolTable extends AbstractTable implements EventListener {
	/**
     * 
     */
    private static final long serialVersionUID = 201106191114L;
    
    private Filter filter = ViewControllers.getFilter();

	private static final String ONE = "1";

	private static final String ZERO = "0";

	private PortfolioController controller = ViewControllers.getController();
	
	private PerformanceSymbolModel tableModel = new PerformanceSymbolModel();
	private TableCellRenderer renderer = new PrecisionCellRenderer(2);
	HashMap<String, Color> colors = new HashMap<String, Color>();

	private static PerformanceSymbolTable symbolTable = new PerformanceSymbolTable();

	static public PerformanceSymbolTable getInstance() {
		return symbolTable;
	}

	private PerformanceSymbolTable() {
		colors.put(ZERO + true, new Color(150, 190, 255));
		colors.put(ZERO + false, new Color(255, 148, 0));
		colors.put(ONE + true, new Color(245, 245, 245));
		colors.put(ONE + false, new Color(245, 245, 245));
		setModel(tableModel);
		// c.addSummaryObserver(this);
		EventBus.getInstance().add(this, SwingEvents.SYMBOL_ANALYSIS_UPDATED);
		
		setPreferredScrollableViewportSize(new Dimension(200, 275));
		validate();
		// setSize(400, 300);
		setVisible(true);
		setRowHeight(getRowHeight() + 5);

		setRowSorter(new TableRowSorter<PerformanceSymbolModel>(tableModel));
		setFirstRowSorted(true);
	}

	@Override
	public void update(Event event, Object model) {
		// TODO Redesign Filter/SharedFilter
		if(SwingEvents.SYMBOL_ANALYSIS_UPDATED.equals(event)){
			Collection col = SwingEvents.SYMBOL_ANALYSIS_UPDATED.resolveModel(model);
			tableModel.setData(col);
			setToolTipText(col.toString());
			getRootPane().validate();
		}
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		return renderer;
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

	private static class PrecisionCellRenderer extends DefaultTableCellRenderer {
		/**
         * 
         */
        private static final long serialVersionUID = 201106191114L;
		private static final String PERCENT_WITH_PARENTHESIS = "(%)";
		private static final String PERCENT = "%";
		private static final String EMPTY_STR = "";
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
				setAlignmentY(RIGHT_ALIGNMENT);
				if (((BigDecimal) value).equals(BigDecimal.ZERO)) {
					setText(EMPTY_STR);
					return this;
				}
				setText(format.format(value));
			}

			if (column == 8) {
			}
			if (table.getColumnName(column).contains(PERCENT_WITH_PARENTHESIS)) {
				setText(getText() + PERCENT);
			}
			return this;
		}
	}
}
