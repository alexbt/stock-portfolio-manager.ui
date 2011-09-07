package com.proserus.stocks;

import com.proserus.stocks.model.common.BoBuilder;
import com.proserus.stocks.model.symbols.HistoricalPrice;
import com.proserus.stocks.model.symbols.HistoricalPriceImpl;
import com.proserus.stocks.model.symbols.Symbol;
import com.proserus.stocks.model.symbols.SymbolImpl;
import com.proserus.stocks.model.transactions.Label;
import com.proserus.stocks.model.transactions.LabelImpl;
import com.proserus.stocks.model.transactions.Transaction;
import com.proserus.stocks.model.transactions.TransactionImpl;

public class BoBuilderImpl implements BoBuilder{

	public Transaction getTransaction(){
		return new TransactionImpl();
	}
	
	public Symbol getSymbol(){
		return new SymbolImpl();
	}
	
	public Label getLabel(){
		return new LabelImpl();
	}
	
	public HistoricalPrice getHistoricalPrice(){
		return new HistoricalPriceImpl();
	}
}
