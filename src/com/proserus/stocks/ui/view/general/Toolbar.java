package com.proserus.stocks.ui.view.general;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import com.proserus.stocks.ui.view.actions.AddSymbolAction;
import com.proserus.stocks.ui.view.actions.AddTransactionAction;
import com.proserus.stocks.ui.view.actions.ExportToCsvAction;
import com.proserus.stocks.ui.view.actions.ImportFromCsvAction;
import com.proserus.stocks.ui.view.actions.ShowSettingsAction;
import com.proserus.stocks.ui.view.actions.UpdateOldPricesAction;
import com.proserus.stocks.ui.view.actions.UpdatePriceAction;

public class Toolbar extends JToolBar{
	
	public Toolbar(){
        addTransaction();
		addSymbol();
		updateCurrentPrice();
		updateOldPrices();
		
		importCsv();
		exportCsv();
		colorSettings();
	}

	private void updateOldPrices() {
	    JButton button = new JButton();
		button.setActionCommand("updateOldPrices");
		button.setContentAreaFilled(false);
		button.setAction(new UpdateOldPricesAction());
		button.setToolTipText("Updates Historical Prices From Yahoo");
		button.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/HistoricalPrices.png")));
		button.setMnemonic(KeyEvent.VK_O);
		add(button);
    }

	private void updateCurrentPrice() {
	    JButton button = new JButton();
		button.setActionCommand("updatePrices");
		button.setContentAreaFilled(false);
		button.setAction(new UpdatePriceAction());
		button.setToolTipText("Updates Current Price From Yahoo");
		button.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/RefreshPrice.png")));
		button.setMnemonic(KeyEvent.VK_P);
		add(button);
    }

	private void addSymbol() {
	    JButton button = new JButton();
		button.setActionCommand("addSymbol");
		button.setContentAreaFilled(false);
		button.setAction(new AddSymbolAction());
		button.setToolTipText("Add a Symbol");
		button.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/AddSymbol.png")));
		button.setMnemonic(KeyEvent.VK_A);
		add(button);
    }

	private void addTransaction() {
	    JButton button = new JButton();
		button.setMnemonic(KeyEvent.VK_A);
		button.setContentAreaFilled(false);
		button.setAction(new AddTransactionAction());
		button.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/AddTransaction.png")));
		button.setActionCommand("addTransaction");
		button.setToolTipText("Add a Transaction");
		//button.addActionListener(this);
		add(button);
    }

	private void importCsv() {
	    JButton button;
	    button = new JButton();
		button.setActionCommand("import");
		button.setContentAreaFilled(false);
		button.setAction(new ImportFromCsvAction());
		button.setToolTipText("Import");
		button.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/Import.png")));
		add(button);
    }
	
	private void exportCsv() {
	    JButton button;
	    button = new JButton();
		button.setActionCommand("export");
		button.setContentAreaFilled(false);
		button.setAction(new ExportToCsvAction());
		button.setToolTipText("Export");
		button.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/Export.png")));
		add(button);
    }
	
	private void colorSettings() {
	    JButton button;
	    button = new JButton();
		button.setActionCommand("settings");
		button.setContentAreaFilled(false);
		button.setAction(new ShowSettingsAction());
		button.setToolTipText("Color Settings");
		button.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/Settings.png")));
		add(button);
    }

}