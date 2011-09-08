package com.proserus.stocks.ui.controller;

import com.google.inject.Inject;
import com.proserus.stocks.bo.common.BoBuilder;
import com.proserus.stocks.bp.model.Filter;
import com.proserus.stocks.ui.view.general.Window;

public class ViewControllers {
	private static BoBuilder boBuilder;
	
	@Inject
	public void setBoBuilder(BoBuilder boBuilder) {
		ViewControllers.boBuilder = boBuilder;
    }
	static public BoBuilder getBoBuilder() {
    	return boBuilder;
    }

	@Inject
	public void setFilter(Filter filter) {
    	ViewControllers.filter = filter;
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

	private static Filter filter;

	//TODO THIS IS NASTY! THis should be data held by the controller..
	public static Filter getFilter() {
		return filter;
	}

	private static Window window;

	public static Window getWindow() {
		return window;
	}
}
