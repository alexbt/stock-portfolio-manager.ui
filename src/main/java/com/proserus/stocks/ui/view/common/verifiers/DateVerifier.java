package com.proserus.stocks.ui.view.common.verifiers;

import java.awt.Color;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class DateVerifier extends InputVerifier {
	private static final String EMPTY_STR = "";
	private static final String FORMAT_IS_YYYYMMDD = "format is yyyymmdd";
	Pattern p = Pattern.compile("(\\d{4})([^0-9])?(\\d{2})([^0-9])?(\\d{2})");

	@Override
	public boolean verify(JComponent input) {
		return verifyCalendar(input) != null;
	}

	public Calendar verifyCalendar(JComponent input) {
		JTextField dateField = ((JTextField) input);

		String str = dateField.getText();
		Calendar calendar = null;
		if (!str.isEmpty()) {
			Matcher m = p.matcher(str);
			if (m.matches()) {
				Calendar c = Calendar.getInstance();
				int year = Integer.parseInt(m.group(1));
				int month = Integer.parseInt(m.group(3)) - 1;
				int day = Integer.parseInt(m.group(5));
				c.set(Calendar.YEAR, year);
				c.set(Calendar.MONTH, month);
				c.set(Calendar.DAY_OF_MONTH, day);
				if (c.before(Calendar.getInstance()) && c.get(Calendar.YEAR) == year && c.get(Calendar.MONTH) == month
						&& c.get(Calendar.DAY_OF_MONTH) == day) {
					calendar = c;
				}

			}
		}
		if (calendar != null) {
			dateField.setBackground(Color.white);
			dateField.setToolTipText(EMPTY_STR);
		} else {
			dateField.setBackground(Color.red);
			dateField.setToolTipText(FORMAT_IS_YYYYMMDD);
		}
		return calendar;
	}
}