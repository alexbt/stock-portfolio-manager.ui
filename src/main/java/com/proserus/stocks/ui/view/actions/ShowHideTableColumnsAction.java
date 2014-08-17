package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

import com.proserus.stocks.ui.view.common.AbstractTable;

public class ShowHideTableColumnsAction extends AbstractAction {
	private static final long serialVersionUID = 201404031810L;

	private AbstractTable table;

	public ShowHideTableColumnsAction(AbstractTable table) {
		this.table = table;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		table.showHideColumn((JMenuItem) arg0.getSource());
	}

}
