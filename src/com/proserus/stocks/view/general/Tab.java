package com.proserus.stocks.view.general;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import com.proserus.stocks.view.common.DialogImpl;
import com.proserus.stocks.view.common.ViewControllers;
import com.proserus.stocks.view.summaries.OverviewCurrencyTable;
import com.proserus.stocks.view.summaries.OverviewSymbolTable;
import com.proserus.stocks.view.summaries.PerformanceCurrencyTable;
import com.proserus.stocks.view.summaries.PerformanceSymbolTable;
import com.proserus.stocks.view.symbols.AddEditSymbolPanelImpl;
import com.proserus.stocks.view.symbols.SymbolsTable;
import com.proserus.stocks.view.transactions.AddTransactionPanelImpl;
import com.proserus.stocks.view.transactions.TransactionTable;

public class Tab extends JTabbedPane implements ActionListener {
	private static final String TABBED_PANE_SELECTED = "TabbedPane.selected";
	private static final String SYMBOLS = "Watch List";
	private static final String TRANSACTIONS = "Transactions";
	private static final String OVERVIEW = "Overview";

	public Tab() {

		JPanel panel = new JPanel(new BorderLayout());
		JScrollPane js2 = new JScrollPane(OverviewCurrencyTable.getInstance());
		js2.setBorder(BorderFactory.createLineBorder(Color.red));
		panel.add(js2, BorderLayout.SOUTH);
		panel.add(new JScrollPane(OverviewSymbolTable.getInstance()), BorderLayout.CENTER);
		addTab(OVERVIEW, panel);

		panel = new JPanel(new BorderLayout());
		js2 = new JScrollPane(PerformanceCurrencyTable.getInstance());
		js2.setBorder(BorderFactory.createLineBorder(Color.red));
		panel.add(js2, BorderLayout.SOUTH);
		panel.add(new JScrollPane(PerformanceSymbolTable.getInstance()), BorderLayout.CENTER);
		addTab("Performance", panel);

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JScrollPane ff = new JScrollPane(TransactionTable.getInstance());
//		ff.setBorder(new MatteBorder(null));
		FlowLayout flow = new FlowLayout();
		flow.setAlignment(FlowLayout.LEFT);
		// TransactionSymbolFilterPanel symbolFilter = new TransactionSymbolFilterPanel(TransactionTable.getInstance());
		// panel.add(symbolFilter, BorderLayout.NORTH);
		JButton button = new JButton("Add");
		button.setMnemonic(KeyEvent.VK_A);
		button.setActionCommand("addTransaction");
		button.addActionListener(this);
		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout(pan,BoxLayout.LINE_AXIS));
		pan.add(button);
		
		panel.add(pan, BorderLayout.NORTH);
		panel.add(ff, BorderLayout.CENTER);
		addTab(TRANSACTIONS, panel);

		
		
		pan = new JPanel();
		pan.setLayout(new BoxLayout(pan,BoxLayout.LINE_AXIS));
		
		button = new JButton("Add");
		button.setActionCommand("addSymbol");
		button.setMnemonic(KeyEvent.VK_A);
		button.addActionListener(this);
		pan.add(button);
		
		
		button = new JButton("Update Prices");
		button.setActionCommand("updatePrices");
		button.setMnemonic(KeyEvent.VK_P);
		button.addActionListener(this);
		pan.add(button);
		button.setMnemonic(KeyEvent.VK_P);
		
		button = new JButton("Update Old Prices");
		button.setActionCommand("updateOldPrices");
		button.setMnemonic(KeyEvent.VK_O);
		button.addActionListener(this);
		pan.add(button);
		button.setMnemonic(KeyEvent.VK_O);
		
		panel = new JPanel(new BorderLayout());
		panel.add(pan, BorderLayout.NORTH);
		// panel.add(new SettingsPanel(), BorderLayout.NORTH);
		panel.add(new JScrollPane(SymbolsTable.getInstance()), BorderLayout.CENTER);
//		panel.add(new AddSymbolsPanel(true), BorderLayout.SOUTH);
		addTab(SYMBOLS, panel);

		UIManager.put(TABBED_PANE_SELECTED, Color.black);
		setVisible(true);

		// GraphPanel demo = new GraphPanel("Graph");
		// demo.setVisible(true);
		// demo.setSize(800, 600);
		// TODO chart
		// addTab("Charts", demo);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() instanceof JButton) {
			if (arg0.getActionCommand().equals("addTransaction")) {
				new DialogImpl(new AddTransactionPanelImpl(),"Add a transaction").setVisibile(true);
			} else if (arg0.getActionCommand().equals("addSymbol")) {
				new DialogImpl(new AddEditSymbolPanelImpl(true),"Add a symbol").setVisibile(true);
			} else if (arg0.getActionCommand().equals("updatePrices")) {
				ViewControllers.getController().updatePrices();
			} else if (arg0.getActionCommand().equals("updateOldPrices")) {
				ViewControllers.getController().updateHistoricalPrices();
			}
		}
	}
}
