package com.proserus.stocks.ui.view.transactions;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyVetoException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.joda.time.DateTime;

import com.proserus.stocks.bo.symbols.CurrencyEnum;
import com.proserus.stocks.bo.symbols.DefaultCurrency;
import com.proserus.stocks.bo.symbols.SectorEnum;
import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bo.transactions.Label;
import com.proserus.stocks.bo.transactions.Transaction;
import com.proserus.stocks.bo.transactions.TransactionType;
import com.proserus.stocks.bo.utils.BigDecimalUtils;
import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventBus;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.SwingEvents;
import com.proserus.stocks.ui.controller.ViewControllers;
import com.proserus.stocks.ui.view.common.AbstractDialog;
import com.proserus.stocks.ui.view.common.CurrencyComboRenderer;
import com.proserus.stocks.ui.view.common.SortedComboBoxModel;
import com.proserus.stocks.ui.view.common.verifiers.NumberVerifier;
import com.proserus.stocks.ui.view.symbols.EmptySymbol;

public class AddTransactionPanelImpl extends AbstractAddTransactionPanel
		implements ActionListener, EventListener, KeyListener {
	private static final long serialVersionUID = 201404031812L;
	private static final String REQUIRED_FIELD_S_MISSING = "Required field(s) missing or invalid";
	private static final String CANNOT_ADD_TRANSACTION = "Cannot add transaction";
	private static final String EMPTY_STR = "";

	public AddTransactionPanelImpl() {
		super();

		enableReinvestmentFields(false);

		getCurrencyField().setRenderer(new CurrencyComboRenderer());
		for (CurrencyEnum cur : CurrencyEnum.values()) {
		    if (cur.isVisible()) {
		        getCurrencyField().addItem(cur);
		    }
		}
		getCurrencyField().setSelectedItem(DefaultCurrency.DEFAULT_CURRENCY);

		for (SectorEnum sector : SectorEnum.retrieveSortedSectors()) {
		        getSectorField().addItem(sector);
        }
		getSectorField().setSelectedItem(SectorEnum.UNKNOWN);

		getCurrencyField().setMaximumRowCount(12);
		getSectorField().setMaximumRowCount(12);

		getTotalField().addKeyListener(this);
		getPriceField().addKeyListener(this);
		getQuantityField().addKeyListener(this);
		getReinvestTotalField().addKeyListener(this);
		getReinvestPriceField().addKeyListener(this);
		getReinvestQuantityField().addKeyListener(this);

		getAddButton().addActionListener(this);
		getAddButton().setActionCommand("add");

		getAddCloseButton().addActionListener(this);
		getAddCloseButton().setActionCommand("addClose");

		getCloseButton().addActionListener(this);
		getCloseButton().setActionCommand("close");

		getSymbolField().addActionListener(this);
		getSymbolField().setActionCommand("changeSymbol");

		getTypeField().addActionListener(this);
		getTypeField().setActionCommand("changeType");

		getLabelsList().setAddEnabled(true);
		getLabelsList().setHorizontal(false);

		EventBus.getInstance().add(getLabelsList(), SwingEvents.LABELS_UPDATED);
		EventBus.getInstance().add(this, SwingEvents.SYMBOLS_UPDATED);

		for (TransactionType transactionType : TransactionType.values()) {
		    if(transactionType.isVisible()){
		        getTypeField().addItem(transactionType);
		    }
		}

		getSymbolField().addKeyListener(this);
		getAddButton().addKeyListener(this);
		getAddCloseButton().addKeyListener(this);
		getCloseButton().addKeyListener(this);
		getCommissionField().addKeyListener(this);
		getDateField().addKeyListener(this);
		getCurrencyField().addKeyListener(this);
		getSectorField().addKeyListener(this);
		getCompanyNameField().addKeyListener(this);
		getTypeField().addKeyListener(this);

		getSymbolField().setEditable(true);
		getSymbolField().getEditor().getEditorComponent().addKeyListener(this);
		getSymbolField().setMaximumRowCount(12);
		// ((JTextField)
		// (getSymbolField().getEditor().getEditorComponent())).setInputVerifier(new
		// SymbolVerifier());

		NumberVerifier verif = new NumberVerifier();
		getPriceField().setInputVerifier(verif);
		getTotalField().setInputVerifier(verif);
		getQuantityField().setInputVerifier(verif);
		getCommissionField().setInputVerifier(verif);
		getReinvestPriceField().setInputVerifier(verif);
		getReinvestTotalField().setInputVerifier(verif);
		getReinvestQuantityField().setInputVerifier(verif);
		getDateField().setToolTipText("Format is yyyy-MM-dd");

		getCompanyNameField().setDisabledTextColor(Color.BLACK);

		getDateField().requestFocus();
	}

	private void enableReinvestmentFields(boolean enable) {
		if (!enable) {
			emptyReinvestmentFields();
		}
		getReinvestLabel().setEnabled(enable);
		getReinvestPriceField().setEnabled(enable);
		getReinvestPriceLabel().setEnabled(enable);
		getReinvestQuantityField().setEnabled(enable);
		getReinvestQuantityLabel().setEnabled(enable);
		getReinvestTotalField().setEnabled(enable);
		getReinvestTotalLabel().setEnabled(enable);
	}

	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		if (aFlag) {
			getDateField().requestFocus();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (arg0.getActionCommand().startsWith("changeSymbol")) {
			updateNameWithCurrentSymbol();
		} else if (arg0.getActionCommand().equals("add")) {
			addTransaction();
		} else if (arg0.getActionCommand().startsWith("changeType")) {
			enableReinvestmentFields(((JComboBox) arg0.getSource())
					.getSelectedItem().equals(TransactionType.DIVIDEND));
		}

		if (arg0.getActionCommand().equals("addClose")) {
			if (addTransaction()) {
				emptySymbolSpecificFields();
				((AbstractDialog) getParent().getParent().getParent()
						.getParent()).dispose();
			}
		} else if (arg0.getActionCommand().equals("close")) {
			emptySymbolSpecificFields();
			((AbstractDialog) getParent().getParent().getParent().getParent())
					.dispose();
		}
	}

	protected boolean addTransaction() {
		boolean success = false;

		if (!getPriceField().getBackground().equals(Color.red)
				&& getDateField().getDate() != null
				&& !getPriceField().getText().isEmpty()
				&& !getQuantityField().getText().isEmpty()) {
			if (getCommissionField().getText().isEmpty()) {
				getCommissionField().setText("0.00");
			}

			Object o = getSymbolField().getSelectedItem();
			if ((o instanceof String && !((String) o).isEmpty())
					|| o instanceof Symbol) {
				Symbol s = createSymbol(o);
				Transaction t = createTransaction(s, getDateField().getDate(),
						(TransactionType) getTypeField().getSelectedItem(),
						getPriceField().getText(),
						getQuantityField().getText(), getCommissionField()
								.getText(), getLabelsList().getSelectedValues()
								.values());
				ViewControllers.getController().addTransaction(t);

				if (t.getType().equals(TransactionType.DIVIDEND)
						&& !getReinvestPriceField().getBackground().equals(
								Color.red)
						&& !getReinvestPriceField().getText().isEmpty()
						&& !getReinvestQuantityField().getText().isEmpty()) {
					Transaction t2 = createTransaction(s, getDateField()
							.getDate(), TransactionType.BUY,
							getReinvestPriceField().getText(),
							getReinvestQuantityField().getText(), "0.00",
							getLabelsList().getSelectedValues().values());
					ViewControllers.getController().addTransaction(t2);
				}

				success = true;
				emptyAllFields();
				getDateField().requestFocus();
			}
		}

		if (!success) {
			JOptionPane.showConfirmDialog(this, REQUIRED_FIELD_S_MISSING,
					CANNOT_ADD_TRANSACTION, JOptionPane.DEFAULT_OPTION,
					JOptionPane.WARNING_MESSAGE);
		}

		return success;
	}

	private Symbol createSymbol(Object o) {
		Symbol s;
		if (o instanceof String) {
			s = ViewControllers.getBoBuilder().getSymbol();
			s.setTicker((String) o);
		} else {
			s = (Symbol) getSymbolField().getSelectedItem();
		}
		s.setCurrency((CurrencyEnum) getCurrencyField().getSelectedItem());
		s.setSector((SectorEnum) getSectorField().getSelectedItem());
		s.setName(getCompanyNameField().getText());
		s.setCustomPriceFirst(false);
		return s;
	}

	private void emptyAllFields() {
		try {
			getDateField().setDate(Calendar.getInstance().getTime());
		} catch (PropertyVetoException e) {
		}
		getPriceField().setText(EMPTY_STR);
		getQuantityField().setText(EMPTY_STR);
		getCommissionField().setText(EMPTY_STR);
		getTotalField().setText(EMPTY_STR);
		getTotalField().setEnabled(true);
		getPriceField().setEnabled(true);
		getQuantityField().setEnabled(true);
		emptyReinvestmentFields();
	}

	private void emptyReinvestmentFields() {
		getReinvestTotalField().setText(EMPTY_STR);
		getReinvestPriceField().setText(EMPTY_STR);
		getReinvestQuantityField().setText(EMPTY_STR);
		getReinvestTotalField().setEnabled(true);
		getReinvestPriceField().setEnabled(true);
		getReinvestQuantityField().setEnabled(true);
	}

	private void emptySymbolSpecificFields() {
		getSymbolField().setSelectedItem("");
		getCompanyNameField().setName(EMPTY_STR);
		getTypeField().setSelectedIndex(0);
	}

	private Transaction createTransaction(Symbol s, Date date,
			TransactionType type, String price, String quantity,
			String commission, Collection<Label> lab) {
		Transaction t = ViewControllers.getBoBuilder().getTransaction();
		t.setDateTime(new DateTime(date));//TODO remove Joda
		t.setType(type);
		t.setSymbol(s);
		t.setPrice(new BigDecimal(Double.parseDouble(price)));
		t.setQuantity(new BigDecimal(Double.parseDouble(quantity)));
		t.setCommission(new BigDecimal(Double.parseDouble(commission)));
		t.setLabels(lab);
		return t;
	}

	@Override
	public void update(Event event, Object model) {
		if (SwingEvents.SYMBOLS_UPDATED.equals(event)) {
			populateSymbolDropdown(SwingEvents.SYMBOLS_UPDATED
					.resolveModel(model));
		}
	}

	private void populateSymbolDropdown(Collection<Symbol> col) {
		Object previous = getSymbolField().getEditor().getItem();
		getSymbolField().setModel(new SortedComboBoxModel(col.toArray()));
		//getSymbolField().addItem(new EmptySymbol());

		if (previous instanceof String && !((String) previous).isEmpty()) {
			getSymbolField().setSelectedItem(
					ViewControllers.getController()
							.getSymbol((String) previous));
		} else {
		    getSymbolField().setSelectedIndex(-1);
			//getSymbolField().setSelectedItem("");
		}

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getComponent().getParent() instanceof JComboBox
				&& arg0.getKeyCode() != KeyEvent.VK_ESCAPE) {
		} else {
			((AbstractDialog) getParent().getParent().getParent().getParent())
					.keyPressed(arg0);
		}
	}

	private void updateNameWithCurrentSymbol() {
		Symbol s = null;
		Object o = getSymbolField().getEditor().getItem();
		if (o instanceof String) {
			if (!((String)o).isEmpty()) {
				s = ViewControllers.getController().getSymbol((String)o);
			}
			if (s == null) {
				s = new EmptySymbol();
			}
		} else if (o instanceof Symbol) {
			s = (Symbol) o;
		}

		boolean isNew = s instanceof EmptySymbol;
		getCompanyNameField().setEditable(isNew);
		getCompanyNameField().setEnabled(isNew);
		getCurrencyField().setEnabled(isNew);
		getSectorField().setEnabled(isNew);
		getSectorField().setSelectedItem(s.getSector());
		getCurrencyField().setSelectedItem(s.getCurrency());
		getCompanyNameField().setText(s.getName());
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.isConsumed())
			if (!getQuantityField().getText().isEmpty()
					&& !getPriceField().getText().isEmpty()) {
			}

		// TODO remove arg0.getComponent().getParent()!=null after testing
		if (arg0.getComponent().getParent() != null
				&& arg0.getComponent().getParent().equals(getSymbolField())
				&& arg0.getKeyCode() != KeyEvent.VK_ESCAPE) {
			updateNameWithCurrentSymbol();
		}

		autoCalculateTotal(arg0, getCommissionField(), getPriceField(),
				getQuantityField(), getTotalField());
		autoComputeFields(arg0, getCommissionField(), getPriceField(),
				getTotalField(), getQuantityField());
		autoComputeFields(arg0, getCommissionField(), getQuantityField(),
				getTotalField(), getPriceField());

		JTextField comm = new JTextField("0");
		autoCalculateTotal(arg0, comm, getReinvestPriceField(),
				getReinvestQuantityField(), getReinvestTotalField());
		autoComputeFields(arg0, comm, getReinvestPriceField(),
				getReinvestTotalField(), getReinvestQuantityField());
		autoComputeFields(arg0, comm, getReinvestQuantityField(),
				getReinvestTotalField(), getReinvestPriceField());

		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			addTransaction();
		}
	}

	private void autoCalculateTotal(KeyEvent arg0, JTextField source1,
			JTextField source2, JTextField source3, JTextField dest) {
		if ((arg0.getSource().equals(source1)
				|| arg0.getSource().equals(source2) || arg0.getSource().equals(
				source3))
				&& source2.isEnabled() && source3.isEnabled()) {
			try {
				if (!arg0.getSource().equals(source1)) {
					new BigDecimal(((JTextField) arg0.getSource()).getText());
				}
				if (!source2.getText().isEmpty()
						&& !source3.getText().isEmpty()) {
					dest.setEnabled(false);
					dest.setFont(dest.getFont().deriveFont(Font.BOLD));
					dest.setText(String.valueOf(calculateTotal(source2.getText(),
							source3.getText(), source1.getText())));
					return;
				}
			} catch (NumberFormatException e) {

			}

			if (!dest.isEnabled() && !arg0.getSource().equals(source1)) {
				dest.setEnabled(true);
				dest.setFont(dest.getFont().deriveFont(Font.PLAIN));
				dest.setText("");
			}
		}
	}

	private void autoComputeFields(KeyEvent arg0, JTextField source1,
			JTextField source2, JTextField source3, JTextField dest) {
		if ((arg0.getSource().equals(source1)
				|| arg0.getSource().equals(source2) || arg0.getSource().equals(
				source3))
				&& source2.isEnabled() && source3.isEnabled()) {
			try {
				new BigDecimal(((JTextField) arg0.getSource()).getText());
				if (!source2.getText().isEmpty()
						&& !source3.getText().isEmpty()) {
					dest.setEnabled(false);
					dest.setFont(dest.getFont().deriveFont(Font.BOLD));
					dest.setText(String.valueOf(calculatePriceQuantity(source2.getText(),
							source1.getText(), source3.getText())));
					return;
				}
			} catch (NumberFormatException e) {

			}
			if (!dest.isEnabled() && !arg0.getSource().equals(source1)) {
				dest.setEnabled(true);
				dest.setFont(dest.getFont().deriveFont(Font.PLAIN));
				dest.setText("");
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	private BigDecimal calculatePriceQuantity(String divisor,
			String commission, String total) {
		if (commission.isEmpty()) {
			commission = "0";
		}

		return BigDecimalUtils.setDecimalWithScale(new BigDecimal(total))
				.subtract(new BigDecimal(commission))
				.divide(new BigDecimal(divisor), BigDecimal.ROUND_UP);
	}

	private BigDecimal calculateTotal(String price, String quantity,
			String commission) {
		if (commission.isEmpty()) {
			commission = "0";
		}
		return new BigDecimal(price).multiply(new BigDecimal(quantity)).add(
				new BigDecimal(commission));
	}
}
