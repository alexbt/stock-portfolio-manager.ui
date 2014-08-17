package com.proserus.stocks.ui.view.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.Locale;

import javax.swing.JTextField;

import org.junit.Test;
import org.mockito.Mockito;

public class TableBigDecimalFieldEditorTest {

	TableBigDecimalFieldEditor editorWithZero = Mockito.spy(new TableBigDecimalFieldEditor(true));
	TableBigDecimalFieldEditor editorNoZero = Mockito.spy(new TableBigDecimalFieldEditor(false));

	@Test
	public void testLocales() {
		for (Locale loc : Locale.getAvailableLocales()) {
			Locale.setDefault(loc);
			System.out.println("test " + loc);
			testValues();
		}
	}

	@Test
	public void testValues() {
		JTextField field = new JTextField();
		Mockito.when(editorWithZero.getComponent()).thenReturn(field);
		Mockito.when(editorNoZero.getComponent()).thenReturn(field);

		field.setText("0.00000000");
		assertTrue(editorWithZero.stopCellEditing());
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertFalse(editorNoZero.stopCellEditing());
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
	}

	@Test
	public void testValues2() {
		JTextField field = new JTextField();
		Mockito.when(editorWithZero.getComponent()).thenReturn(field);
		Mockito.when(editorNoZero.getComponent()).thenReturn(field);
		field.setText("0.00000001");
		assertTrue(editorWithZero.stopCellEditing());
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertTrue(editorNoZero.stopCellEditing());
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());

	}

	@Test
	public void testValues3() {
		JTextField field = new JTextField();
		Mockito.when(editorWithZero.getComponent()).thenReturn(field);
		Mockito.when(editorNoZero.getComponent()).thenReturn(field);
		field.setText("abc");
		assertFalse(editorWithZero.stopCellEditing());
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertFalse(editorNoZero.stopCellEditing());
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());

	}

	@Test
	public void testValues4() {
		JTextField field = new JTextField();
		Mockito.when(editorWithZero.getComponent()).thenReturn(field);
		Mockito.when(editorNoZero.getComponent()).thenReturn(field);
		field.setText("0.000000000009");
		assertFalse(editorWithZero.stopCellEditing());
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertFalse(editorNoZero.stopCellEditing());
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());

	}

	@Test
	public void testValues5() {
		JTextField field = new JTextField();
		Mockito.when(editorWithZero.getComponent()).thenReturn(field);
		Mockito.when(editorNoZero.getComponent()).thenReturn(field);
		field.setText("0,000000000009");
		assertFalse(editorWithZero.stopCellEditing());
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertFalse(editorNoZero.stopCellEditing());
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());

	}

	@Test
	public void testValues6() {
		JTextField field = new JTextField();
		Mockito.when(editorWithZero.getComponent()).thenReturn(field);
		Mockito.when(editorNoZero.getComponent()).thenReturn(field);
		field.setText("0,00000000");
		assertTrue(editorWithZero.stopCellEditing());
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertFalse(editorNoZero.stopCellEditing());
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());

	}

	@Test
	public void testValues7() {
		JTextField field = new JTextField();
		Mockito.when(editorWithZero.getComponent()).thenReturn(field);
		Mockito.when(editorNoZero.getComponent()).thenReturn(field);
		field.setText("0,00000001");
		assertTrue(editorWithZero.stopCellEditing());
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertTrue(editorNoZero.stopCellEditing());
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
	}

	@Test
	public void testValues8() {
		JTextField field = new JTextField();
		Mockito.when(editorWithZero.getComponent()).thenReturn(field);
		Mockito.when(editorNoZero.getComponent()).thenReturn(field);
		field.setText("0,0000000100000");
		assertTrue(editorWithZero.stopCellEditing());
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());
		assertEquals("0,00000001", field.getText());
		assertTrue(editorNoZero.stopCellEditing());
		assertEquals("0,00000001", field.getText());
		assertEquals(Color.white.getRGB(), field.getBackground().getRGB());

	}

	@Test
	public void testValues9() {
		JTextField field = new JTextField();
		Mockito.when(editorWithZero.getComponent()).thenReturn(field);
		Mockito.when(editorNoZero.getComponent()).thenReturn(field);
		field.setText("0.000000000009000000");
		assertFalse(editorWithZero.stopCellEditing());
		assertEquals("0.000000000009", field.getText());
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertFalse(editorNoZero.stopCellEditing());
		assertEquals("0.000000000009", field.getText());
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
	}

	@Test
	public void testValues10() {
		JTextField field = new JTextField();
		Mockito.when(editorWithZero.getComponent()).thenReturn(field);
		Mockito.when(editorNoZero.getComponent()).thenReturn(field);
		field.setText("0,000000000009000");
		assertFalse(editorWithZero.stopCellEditing());
		assertEquals("0,000000000009", field.getText());
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
		assertFalse(editorNoZero.stopCellEditing());
		assertEquals("0,000000000009", field.getText());
		assertEquals(Color.red.getRGB(), field.getBackground().getRGB());
	}
}
