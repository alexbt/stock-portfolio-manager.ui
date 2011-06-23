package com.proserus.stocks.view.transactions;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.proserus.stocks.view.general.LabelsList;


public abstract class AbstractAddTransactionPanel extends JPanel{
	private JTextField dateField;
	private JTextField priceField;
	private JLabel companyNameLabel;
	private JLabel priceLabel;
	private JLabel quantityLabel;
	private JTextField nameField;
	private JTextField commissionField;
	private JLabel currencyLabel;
	private JLabel commisionLabel;
	private JComboBox typeField;
	private JLabel typeLabel;
	JComboBox currencyField;
	JButton addButton;
	JButton addCloseButton;
	JButton closeButton;
	private JPanel labelsList;
	private JComboBox symbolField;
	private JLabel totalField;
	public JLabel getTotalField() {
    	return totalField;
    }

	private JTextField quantityField;
	
	public AbstractAddTransactionPanel() {
		
		JLabel dateLabel = new JLabel("Date (yyyyMMdd):");
		
		dateField = new JTextField();
		dateField.setColumns(10);
		
		JLabel symbolLabel = new JLabel("Symbol:");
		
		priceField = new JTextField();
		priceField.setColumns(10);
		
		companyNameLabel = new JLabel("Name:");
		
		priceLabel = new JLabel("Price:");
		
		quantityLabel = new JLabel("Quantity:");
		
		nameField = new JTextField();
		nameField.setColumns(10);
		
		commissionField = new JTextField();
		commissionField.setColumns(10);
		
		currencyLabel = new JLabel("Currency:");
		
		commisionLabel = new JLabel("Commission:");
		
		addButton = new JButton("Add");
		
		 addCloseButton = new JButton("Add & Close");
		
		 closeButton = new JButton("Close");
		
		totalField = new JLabel("0.00");
		
		JLabel totalLabel = new JLabel("Total:");
		
		currencyField = new JComboBox();
		
		typeField = new JComboBox();
		
		typeLabel = new JLabel("Type:");
		
		symbolField = new JComboBox();
		
		quantityField = new JTextField();
		quantityField.setColumns(10);
		
		labelsList = new LabelsList();
		
		JLabel tagsLabel = new JLabel("Tags:");
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
									.addComponent(typeField, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(dateField, 139, 139, Short.MAX_VALUE))
								.addComponent(typeLabel)
								.addComponent(dateLabel))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(quantityLabel)
										.addComponent(quantityField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(priceLabel)
										.addComponent(priceField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(commissionField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(commisionLabel)))
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(symbolField, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
										.addComponent(symbolLabel))
									.addGap(18)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(nameField, GroupLayout.PREFERRED_SIZE, 274, GroupLayout.PREFERRED_SIZE)
										.addComponent(companyNameLabel))))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(currencyLabel)
								.addComponent(totalLabel)
								.addComponent(currencyField, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(10)
									.addComponent(totalField))))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(280)
							.addComponent(addButton)
							.addGap(18)
							.addComponent(addCloseButton)
							.addGap(18)
							.addComponent(closeButton)))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(labelsList, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE)
						.addComponent(tagsLabel))
					.addContainerGap(16, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
							.addComponent(currencyLabel)
							.addComponent(companyNameLabel)
							.addComponent(symbolLabel)
							.addComponent(dateLabel))
						.addComponent(tagsLabel))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(currencyField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(nameField, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
								.addComponent(symbolField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(dateField, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
							.addGap(22)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(totalLabel)
								.addComponent(commisionLabel)
								.addComponent(priceLabel)
								.addComponent(quantityLabel)
								.addComponent(typeLabel))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
									.addComponent(commissionField, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
									.addComponent(totalField))
								.addComponent(typeField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(quantityField, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
								.addComponent(priceField, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
							.addGap(19)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(closeButton)
								.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
									.addComponent(addCloseButton)
									.addComponent(addButton))))
						.addComponent(labelsList, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		setLayout(groupLayout);
	}
	
	public JTextField getDateField() {
    	return dateField;
    }

	public JComboBox getSymbolField() {
    	return symbolField;
    }

	public JTextField getQuantityField() {
    	return quantityField;
    }

	public JTextField getPriceField() {
    	return priceField;
    }

	public JTextField getCompanyNameField() {
    	return nameField;
    }

	public JTextField getCommissionField() {
    	return commissionField;
    }

	public JComboBox getTypeField() {
    	return typeField;
    }

	public JComboBox getCurrencyField() {
    	return currencyField;
    }
	
	public LabelsList getLabelsList() {
    	return (LabelsList)labelsList;
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
}
