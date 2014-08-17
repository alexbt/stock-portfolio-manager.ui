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
			testNotZeroEmpty1();
			testNotZeroEmpty2();
			testNotZeroEmpty3();
			testNotZeroEmpty4();
			testNotZeroEmpty5();
			testNotZeroNotEmpty1();
			testNotZeroNotEmpty2();
			testNotZeroNotEmpty3();
			testNotZeroNotEmpty4();
			testNotZeroNotEmpty5();
			testNotZeroNotEmpty6();
			testNotZeroNotEmpty7();
			testNotZeroNotEmpty8();
			testNotZeroNotEmpty9();
			testNotZeroNotEmpty10();
			testZeroEmpty1();
			testZeroEmpty2();
			testZeroEmpty3();
			testZeroEmpty4();
			testZeroEmpty5();
			testZeroEmpty6();
			testZeroEmpty7();
			testZeroEmpty8();
			testZeroEmpty9();
			testZeroEmpty10();
		}
	}

	@Test
	public void testNotZeroEmpty1() {
		JTextField field = new JTextField();
		field.setText("0.00000000");
		assertFalse(numberNotZeroEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0.", field.getText());
	}

	@Test
	public void testNotZeroEmpty2() {
		JTextField field = new JTextField();
		field.setText("0,00000000");
		assertFalse(numberNotZeroEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0,", field.getText());

	}

	@Test
	public void testNotZeroEmpty3() {
		JTextField field = new JTextField();
		field.setText("0.00000001");
		assertTrue(numberNotZeroEmpty.verify(field));
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertEquals("0.00000001", field.getText());

	}

	@Test
	public void testNotZeroEmpty4() {
		JTextField field = new JTextField();
		field.setText("0,00000001");
		assertTrue(numberNotZeroEmpty.verify(field));
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertEquals("0,00000001", field.getText());

	}

	@Test
	public void testNotZeroEmpty5() {
		JTextField field = new JTextField();
		field.setText("");
		assertTrue(numberNotZeroEmpty.verify(field));
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertEquals("", field.getText());
	}

	@Test
	public void testZeroEmpty1() {
		JTextField field = new JTextField();

		field.setText("0.00000000");
		assertTrue(numberZeroEmpty.verify(field));
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertEquals("0.", field.getText());
	}

	@Test
	public void testZeroEmpty2() {
		JTextField field = new JTextField();
		field.setText("0.00000001");
		assertTrue(numberZeroEmpty.verify(field));
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertEquals("0.00000001", field.getText());
	}

	@Test
	public void testZeroEmpty3() {
		JTextField field = new JTextField();
		field.setText("abc");
		assertFalse(numberZeroEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
	}

	@Test
	public void testZeroEmpty4() {
		JTextField field = new JTextField();
		field.setText("0.000000000009");
		assertFalse(numberZeroEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0.000000000009", field.getText());
	}

	@Test
	public void testZeroEmpty5() {
		JTextField field = new JTextField();
		field.setText("0,000000000009");
		assertFalse(numberZeroEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0,000000000009", field.getText());
	}

	@Test
	public void testZeroEmpty6() {
		JTextField field = new JTextField();
		field.setText("0,00000000");
		assertTrue(numberZeroEmpty.verify(field));
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertEquals("0,", field.getText());
	}

	@Test
	public void testZeroEmpty7() {
		JTextField field = new JTextField();
		field.setText("0,00000001");
		assertTrue(numberZeroEmpty.verify(field));
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertEquals("0,00000001", field.getText());
	}

	@Test
	public void testZeroEmpty8() {
		JTextField field = new JTextField();
		field.setText("0,0000000100000");
		assertTrue(numberZeroEmpty.verify(field));
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertEquals("0,00000001", field.getText());
	}

	@Test
	public void testZeroEmpty9() {
		JTextField field = new JTextField();
		field.setText("0.000000000009000000");
		assertFalse(numberZeroEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0.000000000009", field.getText());
	}

	@Test
	public void testZeroEmpty10() {
		JTextField field = new JTextField();
		field.setText("0,000000000009000");
		assertFalse(numberZeroEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0,000000000009", field.getText());
	}

	@Test
	public void testNotZeroNotEmpty1() {
		JTextField field = new JTextField();

		field.setText("0.00000000");
		assertFalse(numberNotZeroNotEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0.", field.getText());
	}

	@Test
	public void testNotZeroNotEmpty2() {
		JTextField field = new JTextField();
		field.setText("0.00000001");
		assertTrue(numberNotZeroNotEmpty.verify(field));
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertEquals("0.00000001", field.getText());
	}

	@Test
	public void testNotZeroNotEmpty3() {
		JTextField field = new JTextField();
		field.setText("abc");
		assertFalse(numberNotZeroNotEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("abc", field.getText());
	}

	@Test
	public void testNotZeroNotEmpty4() {
		JTextField field = new JTextField();
		field.setText("0.000000000009");
		assertFalse(numberNotZeroNotEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0.000000000009", field.getText());
	}

	@Test
	public void testNotZeroNotEmpty5() {
		JTextField field = new JTextField();
		field.setText("0,000000000009");
		assertFalse(numberNotZeroNotEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0,000000000009", field.getText());
	}

	@Test
	public void testNotZeroNotEmpty6() {
		JTextField field = new JTextField();
		field.setText("0,00000000");
		assertFalse(numberNotZeroNotEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0,", field.getText());
	}

	@Test
	public void testNotZeroNotEmpty7() {
		JTextField field = new JTextField();
		field.setText("0,00000001");
		assertTrue(numberNotZeroNotEmpty.verify(field));
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertEquals("0,00000001", field.getText());
	}

	@Test
	public void testNotZeroNotEmpty8() {
		JTextField field = new JTextField();
		field.setText("0,0000000100000");
		assertTrue(numberNotZeroNotEmpty.verify(field));
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertEquals("0,00000001", field.getText());
	}

	@Test
	public void testNotZeroNotEmpty9() {
		JTextField field = new JTextField();
		field.setText("0.000000000009000000");
		assertFalse(numberNotZeroNotEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0.000000000009", field.getText());
	}

	@Test
	public void testNotZeroNotEmpty10() {
		JTextField field = new JTextField();
		field.setText("0,000000000009000");
		assertFalse(numberNotZeroNotEmpty.verify(field));
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertEquals("0,000000000009", field.getText());
	}
}
