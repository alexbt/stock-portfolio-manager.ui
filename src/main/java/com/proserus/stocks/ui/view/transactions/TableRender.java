package com.proserus.stocks.ui.view.transactions;

import java.awt.Component;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.proserus.stocks.bo.common.PersistentEnum;
import com.proserus.stocks.bo.symbols.Symbol;

public class TableRender extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 201404041920L;
	private static final String PERCENT_WITH_PARENTHESIS = "(%)";
	private static final String PERCENT = "%";
	private static final String EMPTY_STR = "";
	private NumberFormat format;
	private int precision = 2;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public TableRender() {
		format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(precision);
		format.setMinimumFractionDigits(precision);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (value != null) {
			if (value instanceof Symbol) {
				setText(((Symbol) value).getTicker());
			} else if (value instanceof PersistentEnum<?>) {
				setText(((PersistentEnum<?>) value).getTitle());
			} else if (value instanceof String || value instanceof Integer) {
				setText(value.toString());
				setIcon(null);
			} else if (value instanceof Calendar) {
				setText(dateFormat.format(((Calendar) value).getTime()));
			} else if (value instanceof BigDecimal) {
				setAlignmentY(RIGHT_ALIGNMENT);
				if (((BigDecimal) value).equals(BigDecimal.ZERO)) {
					setText(EMPTY_STR);
					return this;
				}
				setText(format.format(value));

				if (table.getColumnName(column).contains(PERCENT_WITH_PARENTHESIS)) {
					setText(getText() + PERCENT);
				}
			} else {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		}
		return this;
	}

}
