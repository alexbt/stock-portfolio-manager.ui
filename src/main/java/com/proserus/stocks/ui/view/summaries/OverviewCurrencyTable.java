package com.proserus.stocks.ui.view.summaries;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import org.apache.commons.lang3.StringUtils;

import com.proserus.stocks.bo.analysis.CurrencyAnalysis;
import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventBus;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.ModelChangeEvents;
import com.proserus.stocks.bp.model.Filter;
import com.proserus.stocks.ui.controller.ViewControllers;
import com.proserus.stocks.ui.view.common.AbstractTable;
import com.proserus.stocks.ui.view.general.ColorSettingsDialog;
import com.proserus.stocks.ui.view.transactions.TableRender;

public class OverviewCurrencyTable extends AbstractTable implements EventListener {
	private static final long serialVersionUID = 201404041920L;
	private Filter filter = ViewControllers.getFilter();

	private static final int ROW_SIZE = 21;

	private static final String ONE = "1";

	private static final String ZERO = "0";

	private OverviewCurrencyModel tableModel = new OverviewCurrencyModel();
	private TableRender tableRender = new TableRender();

	HashMap<String, Color> colors = new HashMap<String, Color>();

	private static OverviewCurrencyTable currencySummary = new OverviewCurrencyTable();

	static public OverviewCurrencyTable getInstance() {
		return currencySummary;
	}

	private OverviewCurrencyTable() {
		EventBus.getInstance().add(this, ModelChangeEvents.CURRENCY_ANALYSIS_UPDATED);

		colors.put(ZERO + true, new Color(150, 190, 255));
		colors.put(ZERO + false, new Color(255, 148, 0));
		colors.put(ONE + true, new Color(245, 245, 245));
		colors.put(ONE + false, new Color(245, 245, 245));
		setModel(tableModel);
		// setSize(400, 30);
		setPreferredScrollableViewportSize(new Dimension(200, ROW_SIZE * tableModel.getRowCount()));
		setRowHeight(getRowHeight() + 5);
		setVisible(true);
		validate();
		setRowSorter(new TableRowSorter<OverviewCurrencyModel>(tableModel));
		setFirstRowSorted(true);
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		return tableRender;
	}

	@Override
	public void update(Event event, Object model) {
		if (ModelChangeEvents.CURRENCY_ANALYSIS_UPDATED.equals(event)) {
			Collection<CurrencyAnalysis> col = ModelChangeEvents.CURRENCY_ANALYSIS_UPDATED.resolveModel(model);
			// TODO Redesign FIlter/SharedFilter
			tableModel.setData(col);
			setPreferredScrollableViewportSize(new Dimension(200, ROW_SIZE * col.size()));

			StringBuilder sb = new StringBuilder();
			for (CurrencyAnalysis currencyAnalysis : col) {
				sb.append(currencyAnalysis.getSnapshot() + ", ");
			}
			setToolTipText(StringUtils.removeEnd(String.valueOf(sb), ", "));
			getRootPane().validate();
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
}
