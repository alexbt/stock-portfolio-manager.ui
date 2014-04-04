package com.proserus.stocks.ui.view.common.verifiers;

import java.awt.Color;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateVerifier extends InputVerifier {
	private static final String EMPTY_STR = "";
	private static final String FORMAT_IS_YYYYMMDD = "format is yyyymmdd";
	static private String DATE = "\\d{4}(\\d{2}|(([^0-9])\\d{2}([^0-9])))\\d{2}";
	static private String DATE_FORMAT_2 = "yyyyMMdd";

	@Override
	public boolean verify(JComponent input) {
		JTextField date = ((JTextField) input);
		if (date.getText().matches(DATE) || date.getText().isEmpty()) {
			try {
				if(!date.getText().isEmpty()){

					DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_FORMAT_2);
					fmt.parseDateTime(date.getText().replaceAll("[^0-9]", ""));
				}
				date.setBackground(Color.white);
				date.setToolTipText(EMPTY_STR);
				return true;
			  } catch (Exception e) {
			  }
		}
		date.setBackground(Color.red);
		date.setToolTipText(FORMAT_IS_YYYYMMDD);
		return false;
	}
}