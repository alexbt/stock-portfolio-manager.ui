package com.proserus.stocks.ui.view.general;

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.proserus.stocks.bo.utils.BigDecimalUtils;
import com.proserus.stocks.ui.view.common.verifiers.NumberVerifier;

public class TableBigDecimalFieldEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 201408112016L;
	private boolean isZeroAccepted;
	final private static Map<Boolean, NumberVerifier> numberVerifiers = new HashMap<Boolean, NumberVerifier>() {
		private static final long serialVersionUID = 201407071234L;

		{
			put(true, new NumberVerifier(true, true));
			put(false, new NumberVerifier(false, false));
		}
	};

	public TableBigDecimalFieldEditor(boolean isZeroAccepted) {
		super(new JTextField());
		this.isZeroAccepted = isZeroAccepted;
	}

	@Override
	public boolean stopCellEditing() {
		JTextField field = (JTextField) getComponent();
		boolean b = numberVerifiers.get(isZeroAccepted).verify(field);
		if (!b) {
			field.setText((BigDecimalUtils.formatNumber(field.getText())));
			return true;
		}
		return super.stopCellEditing();
	}

	@Override
	public Object getCellEditorValue() {
		String no = (String) super.getCellEditorValue();
		BigDecimal number;
		try {
			number = BigDecimal.valueOf(NumberFormat.getInstance().parse(no).doubleValue());
		} catch (ParseException e) {
			number = BigDecimal.ZERO;
		}

		((JComponent) getComponent()).setBorder(new LineBorder(Color.black));
		((JComponent) getComponent()).setBackground(Color.white);

		return number.setScale(8);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		((JComponent) getComponent()).setBorder(new LineBorder(Color.black));
		((JComponent) getComponent()).setBackground(Color.white);
		BigDecimal no = (BigDecimal) value;
		String val = no.setScale(8).toPlainString();
		DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance();
		char sep = formatter.getDecimalFormatSymbols().getDecimalSeparator();
		val = val.replace('.', sep);

		return super.getTableCellEditorComponent(table, val, isSelected, row, column);
	}
}
