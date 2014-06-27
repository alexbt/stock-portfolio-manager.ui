package com.proserus.stocks.ui.view.common.verifiers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.Locale;

import javax.swing.JTextField;

import org.junit.Test;

import com.proserus.stocks.ui.view.common.verifiers.NumberVerifier.AllowedValues;

public class NumberVerifierTest {
	NumberVerifier numberZeroEmpty = new NumberVerifier(AllowedValues.EMPTY_OR_GREATER_EQUALS_TO_ZERO);
	NumberVerifier numberNotZeroNotEmpty = new NumberVerifier(AllowedValues.NOTEMPTY_AND_GREATER_THAN_ZERO);
	NumberVerifier numberNotZeroEmpty = new NumberVerifier(AllowedValues.EMPTY_OR_GREATER_THAN_ZERO);

	@Test
	public void testLocales() {
		for (Locale loc : Locale.getAvailableLocales()) {
			Locale.setDefault(loc);
			System.out.println("test " + loc);
			testNotZeroEmpty();
			testNotZeroNotEmpty();
			testZeroEmpty();
		}
	}

	@Test
	public void testNotZeroEmpty() {
		JTextField field = new JTextField();
		field.setText("0.00000000");
		assertFalse(numberNotZeroEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0.", field.getText());

		field.setText("0,00000000");
		assertFalse(numberNotZeroEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0.", field.getText());

		field.setText("0.00000001");
		assertTrue(numberNotZeroEmpty.verify(field));
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertEquals("0.00000001", field.getText());

		field.setText("0,00000001");
		assertTrue(numberNotZeroEmpty.verify(field));
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertEquals("0.00000001", field.getText());

		field.setText("");
		assertTrue(numberNotZeroEmpty.verify(field));
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertEquals("", field.getText());
	}

	@Test
	public void testZeroEmpty() {
		JTextField field = new JTextField();

		field.setText("0.00000000");
		assertTrue(numberZeroEmpty.verify(field));
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertEquals("0.", field.getText());

		field.setText("0.00000001");
		assertTrue(numberZeroEmpty.verify(field));
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertEquals("0.00000001", field.getText());

		field.setText("abc");
		assertFalse(numberZeroEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());

		field.setText("0.000000000009");
		assertFalse(numberZeroEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0.000000000009", field.getText());

		field.setText("0,000000000009");
		assertFalse(numberZeroEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0.000000000009", field.getText());

		field.setText("0,00000000");
		assertTrue(numberZeroEmpty.verify(field));
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertEquals("0.", field.getText());

		field.setText("0,00000001");
		assertTrue(numberZeroEmpty.verify(field));
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertEquals("0.00000001", field.getText());

		field.setText("0,0000000100000");
		assertTrue(numberZeroEmpty.verify(field));
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertEquals("0.00000001", field.getText());

		field.setText("0.000000000009000000");
		assertFalse(numberZeroEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0.000000000009", field.getText());

		field.setText("0,000000000009000");
		assertFalse(numberZeroEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0.000000000009", field.getText());
	}

	@Test
	public void testNotZeroNotEmpty() {
		JTextField field = new JTextField();

		field.setText("0.00000000");
		assertFalse(numberNotZeroNotEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0.", field.getText());

		field.setText("0.00000001");
		assertTrue(numberNotZeroNotEmpty.verify(field));
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertEquals("0.00000001", field.getText());

		field.setText("abc");
		assertFalse(numberNotZeroNotEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("abc", field.getText());

		field.setText("0.000000000009");
		assertFalse(numberNotZeroNotEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0.000000000009", field.getText());

		field.setText("0,000000000009");
		assertFalse(numberNotZeroNotEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0.000000000009", field.getText());

		field.setText("0,00000000");
		assertFalse(numberNotZeroNotEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0.", field.getText());

		field.setText("0,00000001");
		assertTrue(numberNotZeroNotEmpty.verify(field));
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertEquals("0.00000001", field.getText());

		field.setText("0,0000000100000");
		assertTrue(numberNotZeroNotEmpty.verify(field));
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertEquals("0.00000001", field.getText());

		field.setText("0.000000000009000000");
		assertFalse(numberNotZeroNotEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0.000000000009", field.getText());

		field.setText("0,000000000009000");
		assertFalse(numberNotZeroNotEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0.000000000009", field.getText());
	}
}
