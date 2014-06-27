package com.proserus.stocks.ui.view.general;

import java.awt.Color;
import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.proserus.stocks.ui.view.common.verifiers.DateVerifier;

public class TableDateFieldEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 201108112016L;

	private DateFormat editFormat = new SimpleDateFormat("yyyy-MM-dd");
	Pattern p = Pattern.compile("(\\d{4})([^0-9])?(\\d{2})([^0-9])?(\\d{2})");
	private Calendar data;
	private DateVerifier dateVerifier = new DateVerifier();
	JTextField field = new JTextField();

	public TableDateFieldEditor() {
		super(new JTextField());
		getComponent().setName("Table.editor");
	}

	@Override
	public boolean stopCellEditing() {
		String str = (String) super.getCellEditorValue();
		field.setText(str);
		Calendar calendar = dateVerifier.verifyCalendar(field);
		if (calendar != null) {
			data = calendar;
			((JComponent) getComponent()).setBackground(Color.white);
			return super.stopCellEditing();
		} else {
			((JComponent) getComponent()).setBackground(Color.red);
			return false;
		}
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.data = null;
		if (value instanceof Calendar) {
			value = editFormat.format(((Calendar) value).getTime());
		}
		((JComponent) getComponent()).setBackground(Color.white);
		((JComponent) getComponent()).setBorder(new LineBorder(Color.black));
		return super.getTableCellEditorComponent(table, value, isSelected, row, column);
	}

	@Override
	public Object getCellEditorValue() {
		return data;
	}
}
