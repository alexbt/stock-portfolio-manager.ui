package com.proserus.stocks.view.common;

import com.google.inject.Inject;
import com.proserus.stocks.bp.SharedFilter;
import com.proserus.stocks.controllers.iface.CurrencyController;
import com.proserus.stocks.controllers.iface.PortfolioController;
import com.proserus.stocks.view.general.Window;

public class ViewControllers {
	
	private static CurrencyController currencyController;
	@Inject
	public void setCurrencyController(CurrencyController currencyController) {
    	ViewControllers.currencyController = currencyController;
    }
	@Inject
	public void setSharedFilter(SharedFilter sharedFilter) {
    	ViewControllers.sharedFilter = sharedFilter;
    }

	public static CurrencyController getCurrencyController() {
		return currencyController;
	}

	private static PortfolioController controller;

	public static PortfolioController getController() {
		return controller;
	}

	@Inject
	public void setController(PortfolioController controller) {
		ViewControllers.controller = controller;
	}
	
	@Inject
	public void setWindow(Window window) {
    	ViewControllers.window = window;
    }

	private static SharedFilter sharedFilter;

	public static SharedFilter getSharedFilter() {
		return sharedFilter;
	}

	private static Window window;

	public static Window getWindow() {
		return window;
	}
}
