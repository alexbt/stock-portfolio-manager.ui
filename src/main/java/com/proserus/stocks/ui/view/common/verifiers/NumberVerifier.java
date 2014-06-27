package com.proserus.stocks.ui.view.common.verifiers;

import java.awt.Color;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

import com.proserus.stocks.bo.utils.BigDecimalUtils;

public class NumberVerifier extends InputVerifier {
	private static final String FORMAT_NUMBER = "format is ##.########";
	private static final String EMPTY_STR = "";
	public static final double GREATER_THAN_ZERO = 0.000000009;
	public static final double ZERO = 0;
	// TODO move to BigDecimalUtils..
	static private String PRICE_MASK = "[0-9]*(\\.||\\,)?\\d{0,8}";

	private AllowedValues allowedValues;

	public enum AllowedValues {
		EMPTY_OR_GREATER_EQUALS_TO_ZERO, //
		EMPTY_OR_GREATER_THAN_ZERO, //
		NOTEMPTY_AND_GREATER_THAN_ZERO;//
	}

	public NumberVerifier(AllowedValues allowedValues) {
		this.allowedValues = allowedValues;
	}

	@Override
	public boolean verify(JComponent input) {
		boolean ok = false;
		JTextField number = ((JTextField) input);
		if ((allowedValues.equals(AllowedValues.EMPTY_OR_GREATER_EQUALS_TO_ZERO) || allowedValues
				.equals(AllowedValues.EMPTY_OR_GREATER_THAN_ZERO)) && number.getText().isEmpty()) {
			ok = true;
		} else {
			try {
				String no = BigDecimalUtils.formatNumber(number.getText());
				number.setText(no);
				Double val = Double.parseDouble(no);
				if ((no.matches(PRICE_MASK) && val >= GREATER_THAN_ZERO)
						|| (allowedValues.equals(AllowedValues.EMPTY_OR_GREATER_EQUALS_TO_ZERO) && val == ZERO)) {
					ok = true;
				}
			} catch (NumberFormatException e) {

			}
		}

		number.setBackground(ok ? Color.white : Color.red);
		number.setToolTipText(ok ? EMPTY_STR : FORMAT_NUMBER);

		return ok;
	}
}