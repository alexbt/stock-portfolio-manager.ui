package com.proserus.stocks.ui.view.summaries;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import org.apache.commons.lang3.StringUtils;

import com.proserus.stocks.bo.analysis.SymbolAnalysis;
import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventBus;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.ModelChangeEvents;
import com.proserus.stocks.bp.model.Filter;
import com.proserus.stocks.ui.controller.ViewControllers;
import com.proserus.stocks.ui.view.common.AbstractTable;
import com.proserus.stocks.ui.view.general.ColorSettingsDialog;
import com.proserus.stocks.ui.view.transactions.TableRender;

public class PerformanceSymbolTable extends AbstractTable implements EventListener {
	private static final long serialVersionUID = 201106191114L;

	private Filter filter = ViewControllers.getFilter();

	private static final String ONE = "1";

	private static final String ZERO = "0";

	private PerformanceSymbolModel tableModel = new PerformanceSymbolModel();
	private TableRender tableRender = new TableRender();
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
		EventBus.getInstance().add(this, ModelChangeEvents.SYMBOL_ANALYSIS_UPDATED);

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
		if (ModelChangeEvents.SYMBOL_ANALYSIS_UPDATED.equals(event)) {
			Collection<SymbolAnalysis> col = ModelChangeEvents.SYMBOL_ANALYSIS_UPDATED.resolveModel(model);
			tableModel.setData(col);

			StringBuilder sb = new StringBuilder();
			for (SymbolAnalysis symbolAnalysis : col) {
				sb.append(symbolAnalysis.getSnapshot() + ", ");
			}
			setToolTipText(StringUtils.removeEnd(String.valueOf(sb), ", "));
			getRootPane().validate();
		}
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		return tableRender;
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
}
