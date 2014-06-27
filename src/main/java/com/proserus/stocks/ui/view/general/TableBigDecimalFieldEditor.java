package com.proserus.stocks.ui.view.general;

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.proserus.stocks.ui.view.common.verifiers.NumberVerifier;
import com.proserus.stocks.ui.view.common.verifiers.NumberVerifier.AllowedValues;

public class TableBigDecimalFieldEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 201408112016L;
	private boolean isZeroAccepted;
	final private static Map<Boolean, NumberVerifier> numberVerifiers = new HashMap<Boolean, NumberVerifier>() {
		private static final long serialVersionUID = 201407071234L;

		{
			put(true, new NumberVerifier(AllowedValues.EMPTY_OR_GREATER_EQUALS_TO_ZERO));
			put(false, new NumberVerifier(AllowedValues.NOTEMPTY_AND_GREATER_THAN_ZERO));
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
			return false;
		}
		return super.stopCellEditing();
	}

	@Override
	public Object getCellEditorValue() {
		String no = (String) super.getCellEditorValue();
		BigDecimal number;
		number = BigDecimal.valueOf(Double.valueOf(no));

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
