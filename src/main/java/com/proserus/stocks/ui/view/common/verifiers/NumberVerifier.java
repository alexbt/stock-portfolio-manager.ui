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

	private boolean isZeroAccepted;
	private boolean canBeEmpty;

	public NumberVerifier(boolean isZeroAccepted, boolean canBeEmpty) {
		this.isZeroAccepted = isZeroAccepted;
		this.canBeEmpty = canBeEmpty;
	}

	@Override
	public boolean verify(JComponent input) {
		boolean ok = false;
		JTextField number = ((JTextField) input);
		if (canBeEmpty && number.getText().isEmpty()) {
			ok = true;
		} else {
			try {
				String no = BigDecimalUtils.formatNumber(number.getText());
				Double val = Double.parseDouble(no);
				if ((no.matches(PRICE_MASK) && val >= GREATER_THAN_ZERO) || (isZeroAccepted && val == ZERO)) {
					number.setText(no);
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