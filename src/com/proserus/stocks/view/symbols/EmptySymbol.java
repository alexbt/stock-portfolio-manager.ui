package com.proserus.stocks.view.symbols;

import com.proserus.stocks.model.symbols.SymbolImpl;

public class EmptySymbol extends SymbolImpl{

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
