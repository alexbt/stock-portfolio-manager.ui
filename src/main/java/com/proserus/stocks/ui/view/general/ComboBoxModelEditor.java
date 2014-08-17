package com.proserus.stocks.ui.view.general;

import javax.swing.plaf.basic.BasicComboBoxEditor;

import org.apache.commons.lang3.StringUtils;

import com.proserus.stocks.bo.common.Database;
import com.proserus.stocks.bo.symbols.CurrencyEnum;
import com.proserus.stocks.bo.symbols.SectorEnum;
import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bo.transactions.Label;
import com.proserus.stocks.bo.transactions.TransactionType;

public class ComboBoxModelEditor extends BasicComboBoxEditor {

	@Override
	// TODO share logic with ComboBoxModelRenderer
	public void setItem(Object value) {
		String anObject = "";
		if (value != null) {
			if (value instanceof Symbol) {
				anObject = ((Symbol) value).getTicker();
			} else if (value instanceof CurrencyEnum) {
				CurrencyEnum currency = (CurrencyEnum) value;
				anObject = currency.getTitle();
			} else if (value instanceof SectorEnum) {
				anObject = ((SectorEnum) value).getTitle();
			} else if (value instanceof TransactionType) {
				anObject = ((TransactionType) value).getTitle();
			} else if (value instanceof String || value instanceof Integer) {
				anObject = value.toString();
			} else if (value instanceof Label) {
				anObject = ((Label) value).getName();
			} else if (value instanceof Database) {
				String path = ((Database) value).getPath();
				if (path.length() > 95) {
					path = "..." + StringUtils.right(path, 95);
				}

				anObject = path;
			}

		}
		super.setItem(anObject);
	}
}
