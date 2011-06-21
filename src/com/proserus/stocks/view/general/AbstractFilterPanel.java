package com.proserus.stocks.view.general;

import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;

public class AbstractFilterPanel extends JPanel{
	private LabelsList labelList;
	private JComboBox yearField;
	private JComboBox symbolField;
	
	public AbstractFilterPanel() {
		
		labelList = new LabelsList();
		JLabel yearLabel = new JLabel("Year:");
		yearField = new JComboBox();
		JLabel symbolLabel = new JLabel("Symbol:");
		
		symbolField = new JComboBox();
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(labelList, GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(yearLabel)
							.addGap(18)
							.addComponent(yearField, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
							.addGap(42)
							.addComponent(symbolLabel)
							.addGap(18)
							.addComponent(symbolField, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addComponent(labelList, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(symbolLabel)
						.addComponent(symbolField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(yearField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(yearLabel))
					.addContainerGap(46, Short.MAX_VALUE))
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
}
