package com.proserus.stocks.ui.view.general;

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

import com.proserus.stocks.ui.view.actions.RemoveSymbolAction;
import com.proserus.stocks.ui.view.actions.RemoveTransactionAction;
import com.proserus.stocks.ui.view.actions.ShowEditSymbolAction;
import com.proserus.stocks.ui.view.summaries.OverviewCurrencyTable;
import com.proserus.stocks.ui.view.summaries.OverviewSymbolTable;
import com.proserus.stocks.ui.view.summaries.PerformanceCurrencyTable;
import com.proserus.stocks.ui.view.summaries.PerformanceSymbolTable;
import com.proserus.stocks.ui.view.symbols.SymbolsTable;
import com.proserus.stocks.ui.view.transactions.GraphsPanel;
import com.proserus.stocks.ui.view.transactions.TransactionTable;

public class Tab extends JTabbedPane {
	private static final long serialVersionUID = 201404042021L;
	private static final String TABBED_PANE_SELECTED = "TabbedPane.selected";
	private static final String SYMBOLS = "Symbols / Watch List";
	private static final String TRANSACTIONS = "Transactions";
	private static final String OVERVIEW = "Overview";

	public Tab() {
        addOverviewTab();
		addPerformanceTab();
        addTransactionTab();
		addSymbolTab();
		addChartsTab();

		UIManager.put(TABBED_PANE_SELECTED, Color.black);
		setVisible(true);

	}

	private void addSymbolTab() {
	    JPanel panel;
	    JButton button;
	    JPanel pan;
	    pan = new JPanel();
		pan.setLayout(new BoxLayout(pan,BoxLayout.LINE_AXIS));
		
		button = new JButton();
		button.setActionCommand("removeSymbol");
		button.setContentAreaFilled(false);
		button.setAction(new RemoveSymbolAction());
		button.setToolTipText("Remove Selected Symbol");
		button.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/Remove.png")));
		pan.add(button);
		
		button = new JButton();
		button.setActionCommand("editSymbol");
		button.setContentAreaFilled(false);
		button.setAction(new ShowEditSymbolAction());
		button.setToolTipText("Edit Selected Symbol");
		button.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/EditSymbol.png")));
		pan.add(button);
		
		
		panel = new JPanel(new BorderLayout());
		panel.add(pan, BorderLayout.NORTH);
		// panel.add(new SettingsPanel(), BorderLayout.NORTH);
		panel.add(new JScrollPane(SymbolsTable.getInstance()), BorderLayout.CENTER);
//		panel.add(new AddSymbolsPanel(true), BorderLayout.SOUTH);
		addTab(SYMBOLS, panel);
    }

	private void addPerformanceTab() {
	    JPanel panel;
	    JScrollPane js2;
	    panel = new JPanel(new BorderLayout());
		js2 = new JScrollPane(PerformanceCurrencyTable.getInstance());
		js2.setBorder(BorderFactory.createLineBorder(Color.black));
		panel.add(js2, BorderLayout.SOUTH);
		panel.add(new JScrollPane(PerformanceSymbolTable.getInstance()), BorderLayout.CENTER);
		addTab("Performance", panel);
    }

	private void addOverviewTab() {
	    JPanel panel = new JPanel(new BorderLayout());
		JScrollPane js2 = new JScrollPane(OverviewCurrencyTable.getInstance());
		js2.setBorder(BorderFactory.createLineBorder(Color.black));
		panel.add(js2, BorderLayout.SOUTH);
		panel.add(new JScrollPane(OverviewSymbolTable.getInstance()), BorderLayout.CENTER);
		addTab(OVERVIEW, panel);
    }
	
	private void addChartsTab() {
		GraphsPanel panel = new GraphsPanel();
		addChangeListener(panel);
		addTab("Graphs", panel);
    }


	private void addTransactionTab() {
	    JPanel panel;
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
    }
}
