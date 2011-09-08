package com.proserus.stocks.ui.view.symbols;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;


public abstract class AbstractAddEditSymbolPanel extends JPanel{
	private JTextField symbolField;
	private JLabel companyNameLabel;
	private JTextField companyNameField;
	private JLabel currencyLabel;
	private JButton addButton;
	private JComboBox currencyField;
	private JCheckBox useCustomPriceField;
	private JButton addCloseButton;
	private JButton closeButton;
	
	public AbstractAddEditSymbolPanel() {
		
		JLabel symbolLabel = new JLabel("Symbol:");
		
		symbolField = new JTextField();
		symbolField.setColumns(10);
		
		companyNameLabel = new JLabel("Company Name:");
		
		companyNameField = new JTextField();
		companyNameField.setColumns(10);
		
		currencyLabel = new JLabel("Currency:");
		
		currencyField = new JComboBox();
		
		useCustomPriceField = new JCheckBox("");
		
		JLabel useCustomPricesLabel = new JLabel("Use custom prices");
		
		addButton = new JButton("Add");
		addCloseButton = new JButton("Add & Close");
		
		closeButton = new JButton("Close");
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(symbolLabel)
								.addComponent(symbolField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(companyNameLabel)
								.addComponent(companyNameField, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(currencyField, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
								.addComponent(currencyLabel))
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(18)
									.addComponent(useCustomPricesLabel))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(58)
									.addComponent(useCustomPriceField))))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(129)
							.addComponent(addButton, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(addCloseButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(closeButton, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(14, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(useCustomPricesLabel, Alignment.TRAILING)
						.addComponent(currencyLabel, Alignment.TRAILING)
						.addComponent(companyNameLabel, Alignment.TRAILING)
						.addComponent(symbolLabel, Alignment.TRAILING))
					.addGap(3)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(symbolField, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
							.addComponent(companyNameField, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
							.addComponent(currencyField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(useCustomPriceField))
					.addPreferredGap(ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(addButton)
						.addComponent(addCloseButton)
						.addComponent(closeButton))
					.addGap(10))
		);
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


	public JCheckBox getUseCustomPriceField() {
    	return useCustomPriceField;
    }
}
