package com.proserus.stocks.view.common;

import org.jfree.data.time.Year;

import com.proserus.stocks.bp.DateUtil;

public class EmptyYear extends Year{
	public EmptyYear(){
		super(0);
	}
	@Override
	public String toString() {
	    return "";
	}
	
	@Override
	public int getYear() {
	    return ((Year)DateUtil.getCurrentYear().next()).getYear();
	}
	
	@Override
	public int compareTo(Object arg0) {
	    return 1;
	}
}
