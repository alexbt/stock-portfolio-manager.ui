package com.proserus.stocks.ui.view.general;

import java.awt.Color;
import java.awt.Component;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.proserus.stocks.bp.utils.DateUtils;

public class TableDateFieldEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 201108112016L;

	private DateFormat parseFormat = new SimpleDateFormat("yyyyMMdd");
	private DateFormat editFormat = new SimpleDateFormat("yyyy-MM-dd");
	private Calendar data;

	public TableDateFieldEditor() {
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
