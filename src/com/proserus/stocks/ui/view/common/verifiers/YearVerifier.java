package com.proserus.stocks.ui.view.common.verifiers;

import java.awt.Color;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class YearVerifier extends InputVerifier {
	private static final String EMPTY_STR = "";
	static private String PATTERN = "\\d{4}";

	public YearVerifier(){
	}
	@Override
	public boolean verify(JComponent input) {
		String str = ((JTextField) input).getText();
		if (str.matches(PATTERN) || str.compareTo(EMPTY_STR) == 0) {
			input.setBackground(Color.white);
			input.setToolTipText(EMPTY_STR);
			return true;
		}
		input.setBackground(Color.red);

		return false;
	}
}