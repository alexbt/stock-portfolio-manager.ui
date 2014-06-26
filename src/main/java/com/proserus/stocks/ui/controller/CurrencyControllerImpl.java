package com.proserus.stocks.ui.controller;

import com.proserus.stocks.bo.symbols.CurrencyEnum;
import com.proserus.stocks.bo.symbols.DefaultCurrency;
import com.proserus.stocks.bp.events.ModelChangeEvents;

public class CurrencyControllerImpl {

	private DefaultCurrency currencies = new DefaultCurrency();

	public CurrencyControllerImpl() {
	}

	public void setDefaultCurrency(CurrencyEnum currency) {
		currencies.setDefault(currency);
		ModelChangeEvents.CURRENCY_DEFAULT_CHANGED.fire(currencies.getDefault());
		currencies.save();
	}

	public CurrencyEnum getDefaultCurrency() {
		return currencies.getDefault();
	}

	public void save() {
		currencies.save();
	}
}
