package com.proserus.stocks.view.symbols;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;

import com.proserus.stocks.events.Event;
import com.proserus.stocks.events.EventBus;
import com.proserus.stocks.events.EventListener;
import com.proserus.stocks.events.SwingEvents;
import com.proserus.stocks.model.symbols.CurrencyEnum;
import com.proserus.stocks.model.symbols.Symbol;
import com.proserus.stocks.view.common.AbstractDialog;
import com.proserus.stocks.view.common.ViewControllers;

public class AddEditSymbolPanelImpl extends AbstractAddEditSymbolPanel implements ActionListener, EventListener, KeyListener{

	private static final String REQUIRED_FIELD_S_MISSING = "Required field(s) missing";

	private static final String CANNOT_ADD_SYMBOL = "Cannot add symbol";
	
	private boolean disableWhenExist = true;

	public AddEditSymbolPanelImpl(boolean buttonVisible) {
		super();
		for (CurrencyEnum cur : CurrencyEnum.values()) {
			getCurrencyField().addItem(cur);
		}
		
		EventBus.getInstance().add(this, SwingEvents.CURRENCY_DEFAULT_CHANGED);
		
		if (buttonVisible) {
			getAddButton().addActionListener(this);
			getAddButton().setActionCommand("add");

			getAddCloseButton().addActionListener(this);
			getAddCloseButton().setActionCommand("addClose");

			getCloseButton().addActionListener(this);
			getCloseButton().setActionCommand("close");
		}else{
			getAddCloseButton().setVisible(false);
			getAddButton().setVisible(false);
			getCloseButton().setVisible(false);
		}
		
		getSymbolField().addKeyListener(this);
		getAddButton().addKeyListener(this);
		getAddCloseButton().addKeyListener(this);
		getCloseButton().addKeyListener(this);
		getCurrencyField().addKeyListener(this);
		getCompanyNameField().addKeyListener(this);
		getUseCustomPriceField().addKeyListener(this);
		getCompanyNameField().setDisabledTextColor(Color.BLACK);
	}
	
	@Override
	public String getName() {
		return getCompanyNameField().getText();
	}

	public void setName(String name) {
		getCompanyNameField().setText(name);
	}

	public CurrencyEnum getCurrency() {
		return (CurrencyEnum) getCurrencyField().getSelectedItem();
	}

	@Override
	public void setVisible(boolean flag) {
		super.setVisible(flag);
		if (flag) {
			getCurrencyField().setSelectedItem(ViewControllers.getCurrencyController().getDefaultCurrency());
			getSymbolField().requestFocus();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().startsWith("add")) {
			if(!getSymbolField().getText().isEmpty()){
					Symbol s = ViewControllers.getBoBuilder().getSymbol();
					s.setTicker(getSymbolField().getText());
					s.setCurrency((CurrencyEnum) getCurrencyField().getSelectedItem());
					s.setName(getCompanyNameField().getText());
					s.setCustomPriceFirst(getUseCustomPriceField().isSelected());

					ViewControllers.getController().addSymbol(s);
					
					getSymbolField().setText("");
					getCurrencyField().setSelectedItem("");
					getCompanyNameField().setText("");
					getUseCustomPriceField().setSelected(false);
			}else{
				JOptionPane.showConfirmDialog(this, REQUIRED_FIELD_S_MISSING, CANNOT_ADD_SYMBOL, JOptionPane.DEFAULT_OPTION,
				        JOptionPane.WARNING_MESSAGE);
			}
		}
		if (arg0.getActionCommand().endsWith("lose")) {
			((AbstractDialog) getParent().getParent().getParent().getParent()).dispose();
		}
	}
	
	
	private void updateNameWithCurrentSymbol() {
	    Symbol s = null;
	    Object o = getSymbolField().getText();
	    if (o instanceof String) {
	    	s = ViewControllers.getController().getSymbol(o.toString());
	    	if(s==null){
	    		s= new EmptySymbol();
	    	}
	    }
	    if(isDisableWhenExist()){
			boolean isNew = s instanceof EmptySymbol;
			getCompanyNameField().setEditable(isNew);
			getCompanyNameField().setEnabled(isNew);
			getAddButton().setEnabled(isNew);
			getUseCustomPriceField().setEnabled(isNew);
			getCurrencyField().setEnabled(isNew);
			getAddCloseButton().setEnabled(isNew);
			getCompanyNameField().setText(s.getName());
		}
    }


	@Override
	public void update(Event event, Object model) {
		if(SwingEvents.CURRENCY_DEFAULT_CHANGED.equals(event)){
			getCurrencyField().setSelectedItem(SwingEvents.CURRENCY_DEFAULT_CHANGED.resolveModel(model));
		}
	}

	@Override
    public void keyPressed(KeyEvent e) {
		((AbstractDialog) getParent().getParent().getParent().getParent()).keyPressed(e);
    }

	@Override
    public void keyReleased(KeyEvent arg0) {
		if (arg0.getSource().equals(getSymbolField()) && arg0.getKeyCode() != KeyEvent.VK_ESCAPE) {
			updateNameWithCurrentSymbol();
		}
    }
	
	@Override
    public void keyTyped(KeyEvent e) {
    }
	
	public boolean isDisableWhenExist() {
    	return disableWhenExist;
    }

	public void setDisableWhenExist(boolean disableWhenExist) {
    	this.disableWhenExist = disableWhenExist;
    }
}
