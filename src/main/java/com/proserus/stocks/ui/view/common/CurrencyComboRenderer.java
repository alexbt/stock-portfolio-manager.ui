package com.proserus.stocks.ui.view.common;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.proserus.stocks.bo.symbols.CurrencyEnum;

public class CurrencyComboRenderer extends JLabel implements ListCellRenderer {

	/**
     * 
     */
	private static final long serialVersionUID = 201110210750L;

	public CurrencyComboRenderer() {
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object arg1, int arg2, boolean isSelected, boolean arg4) {
		// Get the selected index. (The index param isn't
		// always valid, so just use the value.)

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		if (arg1 instanceof CurrencyEnum) {
			CurrencyEnum currency = (CurrencyEnum) arg1;
			setIcon(currency.getIcon());
			setIconTextGap(10);
			setHorizontalAlignment(LEFT);
			setText(currency.getTitle());
		} else {
			setText(" ");
			setIcon(null);
		}

		return this;

	}
}
