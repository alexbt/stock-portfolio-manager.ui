package com.proserus.stocks.ui.view.general;

import java.util.Comparator;

import com.proserus.stocks.bp.utils.DateUtils;

public class FilterYearComparator implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		Integer intOne = ObjectToInt(o1);
		Integer intTwo = ObjectToInt(o2);
		return intTwo.compareTo(intOne);
	}

	private int ObjectToInt(Object o2) {
		return (o2 instanceof Integer) ? (Integer) o2 : DateUtils.getNextYear();
	}
}