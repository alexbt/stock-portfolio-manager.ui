package com.proserus.stocks.ui.view.general;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AbstractFilterPanel extends JPanel{
	private LabelsList labelList;
	private JComboBox yearField;
	private JComboBox symbolField;
	private JLabel transactionTypeLabel;

	private JComboBox transactionTypeField;
	
	public AbstractFilterPanel() {
		
		labelList = new LabelsList();
		JLabel yearLabel = new JLabel("Year:");
		yearField = new JComboBox();
		JLabel symbolLabel = new JLabel("Symbol:");
		
		symbolField = new JComboBox();
		
		transactionTypeLabel = new JLabel("Transaction type:");
		
		transactionTypeField = new JComboBox();
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(labelList, GroupLayout.DEFAULT_SIZE, 10, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(yearLabel)
							.addGap(18)
							.addComponent(yearField, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
							.addGap(42)
							.addComponent(symbolLabel)
							.addGap(18)
							.addComponent(symbolField, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
							.addGap(45)
							.addComponent(transactionTypeLabel)
							.addGap(18)
							.addComponent(transactionTypeField, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addComponent(labelList, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(symbolLabel)
						.addComponent(symbolField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(yearField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(yearLabel)
						.addComponent(transactionTypeLabel)
						.addComponent(transactionTypeField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(10, Short.MAX_VALUE))
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
