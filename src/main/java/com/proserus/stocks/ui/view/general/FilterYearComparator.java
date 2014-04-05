package com.proserus.stocks.ui.view.general;

import java.util.Comparator;

import org.jfree.data.time.Year;

public class FilterYearComparator implements Comparator<Object> {

	@Override
    public int compare(Object o1, Object o2) {
	    if(!(o1 instanceof Year && o2 instanceof Year)){
	    	throw new ClassCastException();
	    }
	    return ((Year)o2).compareTo((Year)o1);
    }
}
