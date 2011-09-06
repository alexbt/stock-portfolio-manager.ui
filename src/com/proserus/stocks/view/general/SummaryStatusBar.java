/*
 * -----------------------------------------------------------------------
 * Project: SCJD
 * -----------------------------------------------------------------------
 * File: ComponentStatusBar.java
 * -----------------------------------------------------------------------
 * Author: Alex Belisle-Turcot
 * -----------------------------------------------------------------------
 * Prometric Id: SP9339741
 * -----------------------------------------------------------------------
 * Email: alex.belisleturcot+scjd@gmail.com
 * -----------------------------------------------------------------------
 * Date of creation: 2007-11-30
 * -----------------------------------------------------------------------
 */
package com.proserus.stocks.view.general;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.proserus.stocks.PortfolioController;
import com.proserus.stocks.events.Event;
import com.proserus.stocks.events.EventBus;
import com.proserus.stocks.events.EventListener;
import com.proserus.stocks.events.SwingEvents;
import com.proserus.stocks.view.common.ViewControllers;

public class SummaryStatusBar extends JPanel implements EventListener {
	private static final String ONE_SPACE = " ";

	private static final long serialVersionUID = 20080113L;

	private PortfolioController controller = ViewControllers.getController();

	private static final String TOTAL_PRICE = "Last price update: ";
	private static final String TOTAL_PROFIT = "Last historical price update: ";
	
	private JLabel price;
	private JLabel historicalPrice;

	/**
	 * Creates a statusbar to display information on the model.
	 * 
	 * @param bookingController
	 *            The controller allowing booking actions on the database.
	 * @param databaseController
	 *            The controller allowing access actions bon the database.
	 */
	public SummaryStatusBar() {
		setLayout(new GridBagLayout());
		init();
		EventBus.getInstance().add(this, SwingEvents.SYMBOLS_CURRENT_PRICE_UPDATED,SwingEvents.SYMBOLS_HISTORICAL_PRICE_UPDATED);
	}

	private void init() {
		setLayout(new GridBagLayout());
		price = setupLabel(TOTAL_PRICE);
		historicalPrice = setupLabel(TOTAL_PROFIT);
	}

	private JLabel setupLabel(final String name) {
		final JLabel label = new JLabel(name);
		label.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		label.setBackground(null);
		label.setName(name);
		addComponentToGridBag(label);
		return label;
	}

	private void addComponentToGridBag(final Component comp) {
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.weightx = 1;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		add(comp, gridBagConstraints);
	}

	/**
	 * Invoked when the model has changed, allowing the view to update itself.
	 * 
	 * @see Observer
	 * @see Observable
	 */
	public void update(final Event event, final Object model) {
		if(SwingEvents.SYMBOLS_HISTORICAL_PRICE_UPDATED.equals(event)){
			historicalPrice.setText(TOTAL_PROFIT + new Date().toString());
		}else if(SwingEvents.SYMBOLS_CURRENT_PRICE_UPDATED.equals(event)){
			price.setText(TOTAL_PRICE + new Date().toString());
		}
	}
}

