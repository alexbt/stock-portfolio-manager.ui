package com.proserus.stocks.ui.view.transactions;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.apache.commons.lang3.StringUtils;
import com.proserus.stocks.bo.common.Database;
import com.proserus.stocks.bo.symbols.CurrencyEnum;
import com.proserus.stocks.bo.symbols.SectorEnum;
import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bo.transactions.Label;
import com.proserus.stocks.bo.transactions.TransactionType;

public class ComboRender extends DefaultListCellRenderer {

    private static final long serialVersionUID = 201406192008L;

    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            if (value instanceof Symbol) {
                setText(((Symbol) value).getTicker());
            }  else if (value instanceof CurrencyEnum) {
                CurrencyEnum currency = (CurrencyEnum) value;
                setText(currency.name());
                setIcon(currency.getIcon());
                setIconTextGap(10);
                setHorizontalAlignment(LEFT);
                setText(currency.name());
            } else if (value instanceof SectorEnum) {
                setText(((SectorEnum) value).getTitle());
            } else if (value instanceof TransactionType) {
                setText(((TransactionType) value).getTitle());
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

        }
        return this;
    }

}
