package com.proserus.stocks.ui.view.general;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

public class AbstractFilterPanel extends JPanel{
	private static final long serialVersionUID = 201404041920L;
	private LabelsList labelList;
	private JComboBox yearField;
	private JLabel transactionTypeLabel;

	private JComboBox transactionTypeField;
	public JComboBox getSectorField() {
    	return sectorField;
    }

	public JComboBox getCurrencyField() {
    	return currencyField;
    }

	private JLabel currencyLabel;
	private JComboBox symbolField;
	private JComboBox currencyField;
	private JComboBox sectorField;
	
	public AbstractFilterPanel() {
		
		labelList = new LabelsList();
		JLabel yearLabel = new JLabel("Year:");
		yearField = new JComboBox();
		yearField.setRenderer(new ComboBoxModelRenderer());
		yearField.setEditor(new ComboBoxModelEditor());
		JLabel symbolLabel = new JLabel("Symbol:");
		
		transactionTypeLabel = new JLabel("Transaction type:");
		
		transactionTypeField = new JComboBox();
		transactionTypeField.setRenderer(new ComboBoxModelRenderer());
		transactionTypeField.setEditor(new ComboBoxModelEditor());
		
		currencyLabel = new JLabel("Currency:");
		
		symbolField = new JComboBox();
		symbolField.setRenderer(new ComboBoxModelRenderer());
		symbolField.setEditor(new ComboBoxModelEditor());
		
		currencyField = new JComboBox();
		currencyField.setRenderer(new ComboBoxModelRenderer());
		currencyField.setEditor(new ComboBoxModelEditor());
		
		sectorField = new JComboBox();
		sectorField.setRenderer(new ComboBoxModelRenderer());
		sectorField.setEditor(new ComboBoxModelEditor());
		
		JLabel sectorLabel = new JLabel("Sector:");
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(labelList, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(yearLabel)
								.addComponent(yearField, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(symbolLabel)
								.addComponent(symbolField, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(transactionTypeLabel)
								.addComponent(transactionTypeField, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(currencyField, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
								.addComponent(currencyLabel, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(sectorLabel, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
								.addComponent(sectorField, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE))))
					.addGap(14))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addComponent(labelList, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(yearLabel)
								.addComponent(transactionTypeLabel)
								.addComponent(symbolLabel))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(yearField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(symbolField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(transactionTypeField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(currencyLabel)
								.addComponent(sectorLabel))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(currencyField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(sectorField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap())
		);
		setLayout(groupLayout);
	}
	
	public LabelsList getLabelList() {
    	return labelList;
    }

	public JComboBox getYearField() {
    	return yearField;
    }

	public JComboBox getSymbolField() {
    	return symbolField;
    }
	
	public JComboBox getTransactionTypeField() {
    	return transactionTypeField;
    }
}
