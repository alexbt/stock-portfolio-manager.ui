package com.proserus.stocks.view.general;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import com.proserus.stocks.view.actions.RemoveSymbolAction;
import com.proserus.stocks.view.actions.RemoveTransactionAction;
import com.proserus.stocks.view.summaries.OverviewCurrencyTable;
import com.proserus.stocks.view.summaries.OverviewSymbolTable;
import com.proserus.stocks.view.summaries.PerformanceCurrencyTable;
import com.proserus.stocks.view.summaries.PerformanceSymbolTable;
import com.proserus.stocks.view.symbols.SymbolsTable;
import com.proserus.stocks.view.transactions.TransactionTable;

public class Tab extends JTabbedPane {
	private static final String TABBED_PANE_SELECTED = "TabbedPane.selected";
	private static final String SYMBOLS = "Symbols / Watch List";
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
		JButton button;

		//button.addActionListener(this);
		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout(pan,BoxLayout.LINE_AXIS));
		
		button = new JButton();
		button.setActionCommand("removeTransaction");
		button.setContentAreaFilled(false);
		button.setAction(new RemoveTransactionAction());
		button.setToolTipText("Remove Selected Transaction");
		button.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/Remove.png")));
		pan.add(button);
		
		
		panel.add(pan, BorderLayout.NORTH);
		panel.add(ff, BorderLayout.CENTER);
		addTab(TRANSACTIONS, panel);

		
		
		pan = new JPanel();
		pan.setLayout(new BoxLayout(pan,BoxLayout.LINE_AXIS));
		
		button = new JButton();
		button.setActionCommand("removeSymbol");
		button.setContentAreaFilled(false);
		button.setAction(new RemoveSymbolAction());
		button.setToolTipText("Remove Selected Symbol");
		button.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/Remove.png")));
		pan.add(button);
		
		
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
}
