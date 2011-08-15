package com.proserus.stocks.view.transactions;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.michaelbaranov.microba.calendar.DatePicker;
import com.proserus.stocks.view.general.LabelsList;


public abstract class AbstractAddTransactionPanel extends JPanel{
	private DatePicker dateField;
	private JTextField priceField;
	private JLabel companyNameLabel;
	private JLabel priceLabel;
	private JLabel quantityLabel;
	private JTextField nameField;
	private JTextField commissionField;
	private JLabel reinvestLabel;
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
	private JTextField totalField;
	private JTextField quantityField;
	private JTextField reinvestPriceField;
	private JLabel reinvestPriceLabel;
	private JTextField reinvestQuantityField;
	private JLabel reinvestQuantityLabel;
	private JTextField reinvestTotalField;
	/**
	 * 
	 */
	private JLabel reinvestTotalLabel;

	public JTextField getReinvestPriceField() {
    	return reinvestPriceField;
    }

	public JLabel getReinvestPriceLabel() {
    	return reinvestPriceLabel;
    }
	
	public JLabel getReinvestLabel() {
    	return reinvestLabel;
    }

	public JTextField getReinvestQuantityField() {
    	return reinvestQuantityField;
    }

	public JLabel getReinvestQuantityLabel() {
    	return reinvestQuantityLabel;
    }

	public JTextField getReinvestTotalField() {
    	return reinvestTotalField;
    }

	public JLabel getReinvestTotalLabel() {
    	return reinvestTotalLabel;
    }

	public JTextField getTotalField() {
		return totalField;
	}
	
	public AbstractAddTransactionPanel() {
		
		JLabel dateLabel = new JLabel("Date (yyyy-MM-dd):");
		
		dateField = new DatePicker(new Date(),new SimpleDateFormat("yyyy-MM-dd"));
		//dateField.setcColumns(10);
		
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
		
		totalField = new JTextField();
		
		JLabel totalLabel = new JLabel("Total:");
		
		currencyField = new JComboBox();
		
		typeField = new JComboBox();
		
		typeLabel = new JLabel("Type:");
		
		symbolField = new JComboBox();
		
		quantityField = new JTextField();
		quantityField.setColumns(10);
		
		labelsList = new LabelsList();
		
		JLabel tagsLabel = new JLabel("Tags:");
		
		reinvestLabel = new JLabel("Reinvest (optional):");
		
		reinvestPriceField = new JTextField();
		reinvestPriceField.setColumns(10);
		
		reinvestPriceLabel = new JLabel("Price:");
		
		reinvestQuantityField = new JTextField();
		reinvestQuantityField.setColumns(10);
		
		reinvestQuantityLabel = new JLabel("Quantity:");
		
		reinvestTotalField = new JTextField();
		reinvestTotalField.setColumns(10);
		
		reinvestTotalLabel = new JLabel("Total:");
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
								.addComponent(dateLabel)
								.addComponent(reinvestLabel, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addGroup(groupLayout.createSequentialGroup()
											.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addComponent(priceLabel)
												.addComponent(priceField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
											.addPreferredGap(ComponentPlacement.RELATED)
											.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addComponent(quantityLabel)
												.addComponent(quantityField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
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
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(currencyLabel)
										.addComponent(totalLabel)
										.addComponent(currencyField, 0, 91, Short.MAX_VALUE)
										.addComponent(totalField, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)))
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(reinvestPriceLabel)
											.addGap(88))
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(reinvestPriceField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED)))
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(reinvestQuantityLabel)
											.addGap(71))
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(reinvestQuantityField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED)))
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(reinvestTotalField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(reinvestTotalLabel)))))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(282)
							.addComponent(addButton)
							.addGap(18)
							.addComponent(addCloseButton)
							.addGap(18)
							.addComponent(closeButton)))
					.addGap(30)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(labelsList, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE)
						.addComponent(tagsLabel))
					.addGap(42))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(15)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(currencyLabel)
							.addComponent(tagsLabel))
						.addComponent(companyNameLabel)
						.addComponent(symbolLabel)
						.addComponent(dateLabel))
					.addGap(8)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
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
								.addComponent(quantityLabel)
								.addComponent(priceLabel)
								.addComponent(typeLabel))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
									.addComponent(commissionField, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
									.addComponent(totalField, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
								.addComponent(typeField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(priceField, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
								.addComponent(quantityField, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup()
											.addPreferredGap(ComponentPlacement.UNRELATED)
											.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
												.addComponent(reinvestPriceLabel)
												.addComponent(reinvestQuantityLabel)))
										.addGroup(groupLayout.createSequentialGroup()
											.addGap(13)
											.addComponent(reinvestTotalLabel)))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
										.addComponent(reinvestTotalField, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
										.addComponent(reinvestQuantityField, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
										.addComponent(reinvestPriceField, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
									.addGap(39)
									.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
										.addComponent(addButton)
										.addComponent(addCloseButton)
										.addComponent(closeButton)))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(18)
									.addComponent(reinvestLabel)))
							.addGap(36))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(labelsList, 0, 0, Short.MAX_VALUE)
							.addContainerGap())))
		);
		setLayout(groupLayout);
	}
	
	public DatePicker getDateField() {
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
