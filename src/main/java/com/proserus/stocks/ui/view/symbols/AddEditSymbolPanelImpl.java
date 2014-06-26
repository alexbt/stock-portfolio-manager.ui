package com.proserus.stocks.ui.view.symbols;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;

import com.proserus.stocks.bo.symbols.CurrencyEnum;
import com.proserus.stocks.bo.symbols.SectorEnum;
import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventBus;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.ModelChangeEvents;
import com.proserus.stocks.ui.controller.ViewControllers;
import com.proserus.stocks.ui.view.common.AbstractDialog;
import com.proserus.stocks.ui.view.general.ComboBoxModelRenderer;

public class AddEditSymbolPanelImpl extends AbstractAddEditSymbolPanel implements ActionListener, EventListener, KeyListener {
	private static final long serialVersionUID = 201404041920L;

	private static final String REQUIRED_FIELD_S_MISSING = "Required field(s) missing";

	private static final String CANNOT_ADD_SYMBOL = "Cannot add symbol";

	private boolean disableWhenExist = true;

	public AddEditSymbolPanelImpl(boolean buttonVisible) {
		super();
		for (CurrencyEnum cur : CurrencyEnum.values()) {
			if (cur.isVisible()) {
				getCurrencyField().addItem(cur);
			}
		}
		getCurrencyField().setRenderer(new ComboBoxModelRenderer());
		getCurrencyField().setMaximumRowCount(12);

		for (SectorEnum sector : SectorEnum.retrieveSortedSectors()) {
			getSectorField().addItem(sector);
		}
		getSectorField().setMaximumRowCount(12);
		getSectorField().setSelectedItem(SectorEnum.UNKNOWN);

		EventBus.getInstance().add(this, ModelChangeEvents.CURRENCY_DEFAULT_CHANGED);

		if (buttonVisible) {
			getAddButton().addActionListener(this);
			getAddButton().setActionCommand("add");

			getAddCloseButton().addActionListener(this);
			getAddCloseButton().setActionCommand("addClose");

			getCloseButton().addActionListener(this);
			getCloseButton().setActionCommand("close");
		} else {
			getAddCloseButton().setVisible(false);
			getAddButton().setVisible(false);
			getCloseButton().setVisible(false);
		}

		getSymbolField().addKeyListener(this);
		getAddButton().addKeyListener(this);
		getAddCloseButton().addKeyListener(this);
		getCloseButton().addKeyListener(this);
		getCurrencyField().addKeyListener(this);
		getSectorField().addKeyListener(this);
		getCompanyNameField().addKeyListener(this);
		getUseCustomPriceField().addKeyListener(this);
		getCompanyNameField().setDisabledTextColor(Color.BLACK);
		getSymbolField().setDisabledTextColor(Color.BLACK);

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
			getCurrencyField().setSelectedItem(ViewControllers.getController().getDefaultCurrency());
			getSymbolField().requestFocus();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().startsWith("add")) {
			if (!addSymbol()) {
				return;
			}
		}
		// covers 'add & close' + 'close' buttons
		if (arg0.getActionCommand().endsWith("lose")) {
			((AbstractDialog) getParent().getParent().getParent().getParent()).dispose();
		}
	}

	private boolean addSymbol() {
		if (!getSymbolField().getText().isEmpty()) {
			Symbol s = ViewControllers.getBoBuilder().getSymbol();
			s.setTicker(getSymbolField().getText());
			s.setCurrency((CurrencyEnum) getCurrencyField().getSelectedItem());
			s.setSector((SectorEnum) getSectorField().getSelectedItem());
			s.setName(getCompanyNameField().getText());
			s.setCustomPriceFirst(getUseCustomPriceField().isSelected());
			// s.setSector(SectorEnum.UNKNOWN);

			ViewControllers.getController().addSymbol(s);

			getSymbolField().setText("");
			getCurrencyField().setSelectedItem("");
			getSectorField().setSelectedItem(SectorEnum.UNKNOWN);
			getCompanyNameField().setText("");
			getUseCustomPriceField().setSelected(false);
			return true;
		} else {
			getSymbolField().setBackground(Color.red);
			JOptionPane.showConfirmDialog(this, REQUIRED_FIELD_S_MISSING, CANNOT_ADD_SYMBOL, JOptionPane.DEFAULT_OPTION,
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
	}

	private void updateNameWithCurrentSymbol() {
		Symbol s = null;
		getSymbolField().setBackground(Color.white);
		Object o = getSymbolField().getText().trim();
		if (o instanceof String) {
			s = ((String) o).isEmpty() ? null : ViewControllers.getController().getSymbol((String) o);
			if (s == null) {
				s = new EmptySymbol();
			}
		}
		if (isDisableWhenExist()) {
			boolean isNew = s instanceof EmptySymbol;
			getCompanyNameField().setEditable(isNew);
			getCompanyNameField().setEnabled(isNew);
			getAddButton().setEnabled(isNew);
			getUseCustomPriceField().setEnabled(isNew);
			getCurrencyField().setEnabled(isNew);
			getSectorField().setEnabled(isNew);
			getAddCloseButton().setEnabled(isNew);
			getCompanyNameField().setText(s.getName());
		}
	}

	@Override
	public void update(Event event, Object model) {
		if (ModelChangeEvents.CURRENCY_DEFAULT_CHANGED.equals(event)) {
			getCurrencyField().setSelectedItem(ModelChangeEvents.CURRENCY_DEFAULT_CHANGED.resolveModel(model));
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

		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			addSymbol();
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
