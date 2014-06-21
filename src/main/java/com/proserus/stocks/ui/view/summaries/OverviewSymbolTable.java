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
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import org.apache.commons.lang3.StringUtils;

import com.proserus.stocks.bo.analysis.SymbolAnalysis;
import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventBus;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.SwingEvents;
import com.proserus.stocks.bp.model.Filter;
import com.proserus.stocks.ui.controller.ViewControllers;
import com.proserus.stocks.ui.view.common.AbstractTable;
import com.proserus.stocks.ui.view.general.ColorSettingsDialog;
import com.proserus.stocks.ui.view.transactions.TableRender;

public class OverviewSymbolTable extends AbstractTable implements EventListener {
    private static final long serialVersionUID = 201404041920L;
    private Filter filter = ViewControllers.getFilter();

    private static final String ONE = "1";

    private static final String ZERO = "0";

    private OverviewSymbolModel tableModel = new OverviewSymbolModel();
    private TableRender tableRender = new TableRender();
    HashMap<String, Color> colors = new HashMap<String, Color>();

    private static OverviewSymbolTable symbolTable = new OverviewSymbolTable();

    static public OverviewSymbolTable getInstance() {
        return symbolTable;
    }

    private OverviewSymbolTable() {
        EventBus.getInstance().add(this, SwingEvents.SYMBOL_ANALYSIS_UPDATED);
        colors.put(ZERO + true, new Color(150, 190, 255));
        colors.put(ZERO + false, new Color(255, 148, 0));
        colors.put(ONE + true, new Color(245, 245, 245));
        colors.put(ONE + false, new Color(245, 245, 245));
        setModel(tableModel);
        // c.addSummaryObserver(this);
        setPreferredScrollableViewportSize(new Dimension(200, 275));
        validate();
        // setSize(400, 300);
        setVisible(true);
        setRowHeight(getRowHeight() + 5);

        setRowSorter(new TableRowSorter<OverviewSymbolModel>(tableModel));
        setFirstRowSorted(true);
    }

    @Override
  //TODO Do not use toString() for business logic
    public void update(Event event, Object model) {
        if (SwingEvents.SYMBOL_ANALYSIS_UPDATED.equals(event)) {
            Collection<SymbolAnalysis> col = SwingEvents.SYMBOL_ANALYSIS_UPDATED.resolveModel(model);
            tableModel.setData(col);
            // Redesign filters
            StringBuilder sb = new StringBuilder();
            for (SymbolAnalysis symbolAnalysis : col) {
                sb.append(symbolAnalysis.getSnapshot() + ", ");
            }
            setToolTipText(StringUtils.removeEnd(String.valueOf(sb),", "));
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
