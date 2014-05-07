package com.proserus.stocks.ui.view.symbols;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class AddHistoricalPricePanelImpl extends AbstractAddHistoricalPricePanel implements ActionListener, Observer {
	private static final long serialVersionUID = 201404041920L;
	public AddHistoricalPricePanelImpl() {
		super();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	@Override
	public void update(Observable o, Object arg) {
	}
}
