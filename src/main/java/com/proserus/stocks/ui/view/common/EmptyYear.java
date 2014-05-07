package com.proserus.stocks.ui.view.common;

import org.jfree.data.time.Year;

import com.proserus.stocks.bp.utils.DateUtil;

public class EmptyYear extends Year{
	private static final long serialVersionUID = 201404041920L;
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
