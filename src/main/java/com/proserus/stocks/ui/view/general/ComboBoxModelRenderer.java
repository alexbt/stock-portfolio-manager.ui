package com.proserus.stocks.ui.view.general;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.apache.commons.lang3.StringUtils;

import com.proserus.stocks.bo.common.Database;
import com.proserus.stocks.bo.common.PersistentEnum;
import com.proserus.stocks.bo.symbols.CurrencyEnum;
import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bo.transactions.Label;

public class ComboBoxModelRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 201406192008L;

	// TODO share logic with ComboBoxModelEditor
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		if (value != null) {
			if (value instanceof Symbol) {
				setText(((Symbol) value).getTicker());
			} else if (value instanceof PersistentEnum<?>) {
				setText(((PersistentEnum<?>) value).getTitle());
				if (value instanceof CurrencyEnum) {
					CurrencyEnum currency = (CurrencyEnum) value;
					setIcon(currency.getIcon());
					setIconTextGap(10);
					setHorizontalAlignment(LEFT);
				}
			} else if (value instanceof String || value instanceof Integer) {
				setText(value.toString());
				setIcon(null);
			} else if (value instanceof Label) {
				setText(((Label) value).getName());
			} else if (value instanceof Database) {
				String path = ((Database) value).getPath();
				if (path.length() > 95) {
					path = "..." + StringUtils.right(path, 95);
				}

				setText(path);
			} else {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			}

		} else {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		}
		return this;
	}

}
