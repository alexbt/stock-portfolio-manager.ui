package com.proserus.stocks.ui.model;

import com.proserus.stocks.bo.common.BoBuilder;
import com.proserus.stocks.bo.common.DBVersion;
import com.proserus.stocks.bo.symbols.HistoricalPrice;
import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bo.transactions.Label;
import com.proserus.stocks.bo.transactions.Transaction;

public class BoBuilderImpl implements BoBuilder {

	public Transaction getTransaction() {
		return new TransactionImpl();
	}

	public Symbol getSymbol() {
		return new SymbolImpl();
	}

	public Label getLabel() {
		return new LabelImpl();
	}

	public HistoricalPrice getHistoricalPrice() {
		return new HistoricalPriceImpl();
	}

	@Override
	public DBVersion getVersion() {
		return new DBVersionImpl();
	}
}
