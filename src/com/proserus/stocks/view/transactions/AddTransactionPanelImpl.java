package com.proserus.stocks.view.transactions;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.joda.time.format.DateTimeFormat;

import com.proserus.stocks.bp.TransactionsBp;
import com.proserus.stocks.model.symbols.CurrencyEnum;
import com.proserus.stocks.model.symbols.DefaultCurrency;
import com.proserus.stocks.model.symbols.Symbol;
import com.proserus.stocks.model.transactions.Label;
import com.proserus.stocks.model.transactions.Transaction;
import com.proserus.stocks.model.transactions.TransactionType;
import com.proserus.stocks.utils.BigDecimalUtils;
import com.proserus.stocks.view.common.AbstractDialog;
import com.proserus.stocks.view.common.SortedComboBoxModel;
import com.proserus.stocks.view.common.ViewControllers;
import com.proserus.stocks.view.common.verifiers.DateVerifier;
import com.proserus.stocks.view.common.verifiers.NumberVerifier;
import com.proserus.stocks.view.common.verifiers.SymbolVerifier;
import com.proserus.stocks.view.symbols.EmptySymbol;

public class AddTransactionPanelImpl extends AbstractAddTransactionPanel implements ActionListener, Observer, KeyListener, FocusListener {
	private static final String REQUIRED_FIELD_S_MISSING = "Required field(s) missing";
	private static final String CANNOT_ADD_TRANSACTION = "Cannot add transaction";
	private static final String EMPTY_STR = "";

	public AddTransactionPanelImpl() {
		super();
		getDateField().requestFocus();
		for (CurrencyEnum cur : CurrencyEnum.values()) {
			getCurrencyField().addItem(cur);
		}
		getCurrencyField().setSelectedItem(DefaultCurrency.DEFAULT_CURRENCY);
		ViewControllers.getCurrencyController().addCurrenciesObserver(this);

		getAddButton().addActionListener(this);
		getAddButton().setActionCommand("add");

		getAddCloseButton().addActionListener(this);
		getAddCloseButton().setActionCommand("addClose");

		getCloseButton().addActionListener(this);
		getCloseButton().setActionCommand("close");
		
		getSymbolField().addActionListener(this);
		getSymbolField().setActionCommand("changeSymbol");
		
		getLabelsList().setAddEnabled(true);
		getLabelsList().setHorizontal(false);
		ViewControllers.getController().addLabelsObserver(getLabelsList());

		// getTypeField().addActionListener(this);
		// getTypeField().setActionCommand("type");
		for (TransactionType transactionType : TransactionType.values()) {
			getTypeField().addItem(transactionType);
		}

		ViewControllers.getController().addTransactionObserver(this);

		getSymbolField().addKeyListener(this);
		getAddButton().addKeyListener(this);
		getAddCloseButton().addKeyListener(this);
		getCloseButton().addKeyListener(this);
		getPriceField().addKeyListener(this);
		getQuantityField().addKeyListener(this);
		getCommissionField().addKeyListener(this);
		getDateField().addKeyListener(this);
		getCurrencyField().addKeyListener(this);
		getCompanyNameField().addKeyListener(this);
		getTypeField().addKeyListener(this);

		getSymbolField().setEditable(true);
		getSymbolField().getEditor().getEditorComponent().addKeyListener(this);
		((JTextField) (getSymbolField().getEditor().getEditorComponent())).setInputVerifier(new SymbolVerifier());
		getPriceField().setInputVerifier(new NumberVerifier());
		getQuantityField().setInputVerifier(new NumberVerifier());
		getCommissionField().setInputVerifier(new NumberVerifier());
		getDateField().setInputVerifier(new DateVerifier());
		getDateField().setToolTipText("yyyyMMdd  or  yyyy/MM/dd  or  yyyy-MM-dd");

		getPriceField().addFocusListener(this);
		getQuantityField().addFocusListener(this);
		getCommissionField().addFocusListener(this);
		
		getCompanyNameField().setDisabledTextColor(Color.BLACK);     	
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
		}else{
		if (arg0.getActionCommand().startsWith("add")) {
			addTransaction();
		}

		// else if (arg0.getActionCommand().equals(getTagsButton().getActionCommand())) {
		// ((AbstractDialog) getParent().getParent().getParent().getParent()).setModal(false);
		// if (jWindow == null) {
		// jWindow = new JWindow(Window.getInstance());
		// jWindow.setFocusable(true);
		// labl = new LabelsList(getTagsButton(), true, true, jWindow, false);
		// jWindow.getContentPane().add(labl);
		// jWindow.setSize(150, 117);
		// }
		// Point p = MouseInfo.getPointerInfo().getLocation();
		// jWindow.setLocation(p);
		// // jWindow.setAlwaysOnTop(true);
		// labl.setVisible(true);
		// jWindow.validate();
		// // jWindow.setAlwaysOnTop(true);
		// jWindow.setVisible(true);
		// // jWindow.setAlwaysOnTop(true);
		// jWindow.requestFocus();
		// labl.requestFocus();
		// // ((AbstractDialog) getParent().getParent().getParent().getParent()).setModal(true);
		// }

		if (arg0.getActionCommand().endsWith("lose")) {
			((AbstractDialog) getParent().getParent().getParent().getParent()).dispose();
		}
		}
	}

	protected void addTransaction() {
		boolean success = false;
		if (!getDateField().getText().isEmpty() && !getPriceField().getText().isEmpty() && !getQuantityField().getText().isEmpty()) {
			Object o = getSymbolField().getSelectedItem();
			if ((o instanceof String && !((String) o).isEmpty()) || o instanceof Symbol) {
				Transaction t = createTransaction(createSymbol(o), getLabelsList().getSelectedValues().values());
				ViewControllers.getController().addTransaction(t);
				success = true;
				emptyAllFields();
				getDateField().requestFocus();
			}
		}

		if (!success) {
			JOptionPane.showConfirmDialog(this, REQUIRED_FIELD_S_MISSING, CANNOT_ADD_TRANSACTION, JOptionPane.DEFAULT_OPTION,
			        JOptionPane.WARNING_MESSAGE);
		}
	}

	private Symbol createSymbol(Object o) {
		Symbol s;
		if (o instanceof String) {
			s = new Symbol();
			s.setTicker((String) o);
		} else {
			s = (Symbol) getSymbolField().getSelectedItem();
		}
		s.setCurrency((CurrencyEnum) getCurrencyField().getSelectedItem());
		s.setName(getCompanyNameField().getText());
		s.setCustomPriceFirst(false);
		return s;
	}

	private void emptyAllFields() {
		getDateField().setText(EMPTY_STR);
		getSymbolField().setSelectedIndex(0);
		getPriceField().setText(EMPTY_STR);
		getQuantityField().setText(EMPTY_STR);
		getCommissionField().setText(EMPTY_STR);
		getTypeField().setSelectedIndex(0);
		getTotalField().setText("0.00");
		getCompanyNameField().setName(EMPTY_STR);
	}

	private Transaction createTransaction(Symbol s, Collection<Label> lab) {
		Transaction t = new Transaction();

		t.setDateTime(DateTimeFormat.forPattern("yyyyMMdd").parseDateTime(getDateField().getText().replaceAll("[^0-9]", "")));
		t.setType((TransactionType) getTypeField().getSelectedItem());
		t.setSymbol(s);
		t.setPrice(new BigDecimal(Double.parseDouble(getPriceField().getText())));
		t.setQuantity(new BigDecimal(Double.parseDouble(getQuantityField().getText())));
		if (getCommissionField().getText().isEmpty()) {
			getCommissionField().setText("0.00");
		}
		t.setCommission(new BigDecimal(Double.parseDouble(getCommissionField().getText())));
		t.setLabels(lab);
		return t;
	}

	@Override
	public void update(Observable transactions, Object UNUSED) {
		if (transactions instanceof TransactionsBp) {
			getSymbolField().setModel(new SortedComboBoxModel(ViewControllers.getController().getSymbols().toArray()));
			getSymbolField().addItem(new EmptySymbol());
			getSymbolField().setSelectedIndex(0);
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getComponent().getParent() instanceof JComboBox && arg0.getKeyCode() != KeyEvent.VK_ESCAPE) {
		} else {
			((AbstractDialog) getParent().getParent().getParent().getParent()).keyPressed(arg0);
		}
	}

	private void updateNameWithCurrentSymbol() {
	    Symbol s = null;
	    Object o = getSymbolField().getEditor().getItem();
	    if (o instanceof String) {
	    	s = ViewControllers.getController().getSymbol(o.toString());
	    	if(s==null){
	    		s= new EmptySymbol();
	    	}
	    } else if (o instanceof Symbol) {
	    	s = (Symbol) o;
	    }
	    
	    boolean isNew = s instanceof EmptySymbol;
    	getCompanyNameField().setEditable(isNew);
    	getCompanyNameField().setEnabled(isNew);
    	getCurrencyField().setEnabled(isNew);
    	getCompanyNameField().setText(s.getName());
    }

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (!getQuantityField().getText().isEmpty() && !getPriceField().getText().isEmpty()) {
		}
		
		if (arg0.getComponent().getParent() instanceof JComboBox && arg0.getKeyCode() != KeyEvent.VK_ESCAPE) {
			updateNameWithCurrentSymbol();
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		if (arg0.getComponent().getParent() instanceof JComboBox && arg0.getKeyCode() != KeyEvent.VK_ESCAPE) {
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusLost(FocusEvent e) {
		if (!getQuantityField().getText().isEmpty() && !getPriceField().getText().isEmpty()) {
			double value = Double.parseDouble(getQuantityField().getText()) * Double.parseDouble(getPriceField().getText());
			if (!getCommissionField().getText().isEmpty()) {
				value += Double.parseDouble(getCommissionField().getText());
			}
			BigDecimal dec = new BigDecimal(value);
			getTotalField().setText(String.valueOf(BigDecimalUtils.setDecimalWithScale(dec).doubleValue()));
		} else {
			getTotalField().setText("0.00");
		}
	}
}
