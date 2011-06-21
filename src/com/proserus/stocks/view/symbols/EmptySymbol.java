package com.proserus.stocks.view.symbols;

import com.proserus.stocks.model.symbols.Symbol;

public class EmptySymbol extends Symbol{

	public EmptySymbol(){
		super();
	}
	
	@Override
	public String getTicker() {
	    return "";
	}
	
	@Override
	public int compareTo(Object arg0) {
	    return 1;
	}
}
