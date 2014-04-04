package com.proserus.stocks.ui.view.symbols;
import java.awt.Component;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;


public abstract class AbstractAddEditSymbolPanel extends JPanel{
	private JTextField symbolField;
	private JLabel companyNameLabel;
	private JComboBox currencyField;
	private JLabel currencyLabel;
	private JButton addButton;
	private JCheckBox useCustomPriceField;
	private JButton addCloseButton;
	private JButton closeButton;
	private JTextField companyNameField;
	private JComboBox sectorField;


	private JLabel sectorLabel;
	
	public AbstractAddEditSymbolPanel() {
		
		JLabel symbolLabel = new JLabel("Symbol:");
		
		symbolField = new JTextField();
		symbolField.setColumns(10);
		
		companyNameLabel = new JLabel("Company Name (Optional):");
		
		currencyLabel = new JLabel("Currency:");
		
		useCustomPriceField = new JCheckBox("");
		
		JLabel useCustomPricesLabel = new JLabel("Use custom prices");
		
		addButton = new JButton("Add");
		addCloseButton = new JButton("Add & Close");
		
		closeButton = new JButton("Close");
		
		companyNameField = new JTextField();
		companyNameField.setColumns(10);
		
		sectorField = new JComboBox();
		
		sectorLabel = new JLabel("Sector:");
		
		currencyField = new JComboBox();
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
									.addComponent(symbolLabel)
									.addComponent(currencyField, 0, 97, Short.MAX_VALUE)
									.addComponent(symbolField, 0, 0, Short.MAX_VALUE))
								.addComponent(currencyLabel))
							.addGap(22)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(companyNameLabel)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(sectorField, GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE)
										.addComponent(sectorLabel))
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addGroup(groupLayout.createSequentialGroup()
											.addGap(45)
											.addComponent(useCustomPriceField)
											.addGap(53))
										.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
											.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
											.addComponent(useCustomPricesLabel)
											.addGap(8))))
								.addComponent(companyNameField, GroupLayout.PREFERRED_SIZE, 275, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED, 53, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(72)
							.addComponent(addButton, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(addCloseButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(closeButton, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(symbolLabel)
								.addComponent(companyNameLabel)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(32)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(symbolField, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
								.addComponent(companyNameField, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(currencyLabel)
								.addComponent(sectorLabel))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(currencyField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(sectorField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(11))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(useCustomPricesLabel)
							.addGap(9)
							.addComponent(useCustomPriceField)))
					.addPreferredGap(ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(addButton)
						.addComponent(addCloseButton)
						.addComponent(closeButton))
					.addContainerGap())
		);
		groupLayout.linkSize(SwingConstants.VERTICAL, new Component[] {addButton, addCloseButton, closeButton});
		groupLayout.linkSize(SwingConstants.VERTICAL, new Component[] {symbolField, companyNameField});
		groupLayout.linkSize(SwingConstants.VERTICAL, new Component[] {symbolLabel, companyNameLabel, currencyLabel, useCustomPricesLabel, sectorLabel});
		groupLayout.linkSize(SwingConstants.HORIZONTAL, new Component[] {addButton, addCloseButton, closeButton});
		setLayout(groupLayout);
	}
	
	
	public JTextField getSymbolField() {
    	return symbolField;
    }
	public JLabel getCompanyNameLabel() {
    	return companyNameLabel;
    }
	public JTextField getCompanyNameField() {
    	return companyNameField;
    }
	public JLabel getCurrencyLabel() {
    	return currencyLabel;
    }
	public JButton getAddButton() {
    	return addButton;
    }


	public JButton getAddCloseButton() {
    	return addCloseButton;
    }


	public JButton getCloseButton() {
    	return closeButton;
    }

	public JComboBox getCurrencyField() {
    	return currencyField;
    }
	
	public JComboBox getSectorField() {
    	return sectorField;
    }


	public JCheckBox getUseCustomPriceField() {
    	return useCustomPriceField;
    }
}
