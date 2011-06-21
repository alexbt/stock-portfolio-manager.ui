package com.proserus.stocks.view.common.verifiers;

import java.awt.Color;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class NumberVerifier extends InputVerifier {
	private static final String FORMAT_NUMBER = "format is ##.##";
	private static final String EMPTY_STR = "";
	static private String OPTIONAL_PRICE = "[0-9]*\\.?[0-9]*";

	@Override
	public boolean verify(JComponent input) {
		JTextField number = ((JTextField) input);
		if (number.getText().isEmpty()) {
			number.setBackground(Color.white);
			number.setToolTipText(EMPTY_STR);
			return true;
		} else if (number.getText().matches(OPTIONAL_PRICE)) {
			try {
				Float.parseFloat(number.getText());
				number.setBackground(Color.white);
				number.setToolTipText(EMPTY_STR);
				return true;
			} catch (NumberFormatException e) {

			}
		}
		number.setBackground(Color.red);
		number.setToolTipText(FORMAT_NUMBER);
		return false;
	}
}