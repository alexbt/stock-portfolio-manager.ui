package com.proserus.stocks.ui.view.general;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import com.proserus.stocks.ui.controller.PortfolioController;
import com.proserus.stocks.ui.view.actions.AddSymbolAction;
import com.proserus.stocks.ui.view.actions.AddTransactionAction;
import com.proserus.stocks.ui.view.actions.CloseApplicationAction;
import com.proserus.stocks.ui.view.actions.ExportToCsvAction;
import com.proserus.stocks.ui.view.actions.ImportFromCsvAction;
import com.proserus.stocks.ui.view.actions.ShowAboutAction;
import com.proserus.stocks.ui.view.actions.ShowHideFiltersAction;
import com.proserus.stocks.ui.view.actions.ShowHideTableColumnsAction;
import com.proserus.stocks.ui.view.actions.ShowSettingsAction;
import com.proserus.stocks.ui.view.actions.UpdateOldPricesAction;
import com.proserus.stocks.ui.view.actions.UpdatePriceAction;
import com.proserus.stocks.ui.view.common.ViewControllers;
import com.proserus.stocks.ui.view.summaries.OverviewCurrencyModel;
import com.proserus.stocks.ui.view.summaries.OverviewCurrencyTable;
import com.proserus.stocks.ui.view.summaries.OverviewSymbolModel;
import com.proserus.stocks.ui.view.summaries.OverviewSymbolTable;
import com.proserus.stocks.ui.view.summaries.PerformanceCurrencyModel;
import com.proserus.stocks.ui.view.summaries.PerformanceCurrencyTable;
import com.proserus.stocks.ui.view.summaries.PerformanceSymbolModel;
import com.proserus.stocks.ui.view.summaries.PerformanceSymbolTable;
import com.proserus.stocks.ui.view.symbols.SymbolsTable;
import com.proserus.stocks.ui.view.symbols.SymbolsTableModel;
import com.proserus.stocks.ui.view.transactions.TransactionTable;
import com.proserus.stocks.ui.view.transactions.TransactionTableModel;

public class Menu extends JMenuBar {
	private static final String ABOUT = "About";

	private static final String HELP = "Help";

	private static final String SETTINGS = "Settings";

	private static final String BASIC_GRAPHICAL_USER_INTERFACE_SETTINGS = "Basic graphical user interface settings";

	private static final String GUI_SETTINGS = "Color Settings";

	private static final String CLOSES_THE_APPLICATION = "Closes the application";

	private static final String CLOSE = "Close";

	private static final String IMPORT = "Import";

	private static final String EXPORT = "Export";

	private static final String FILE = "File";

	private Window window = ViewControllers.getWindow();

	private PortfolioController controller = ViewControllers.getController();

	private static Menu menu = new Menu();
	private static String MESSAGE = "Stock Portfolio Manager was created by:\n" + "Alex Bélisle-Turcot\n" + "alex.belisleturcot@proserus.com\n\n"
	        + "Version: " + Version.VERSION + "\n" + "Build: " + Version.TIMESTAMP + "\n\n" + "This product includes/uses:\n"
	        + "   - Joda-Time: joda-time.sourceforge.net\n" + "   - Hibernate: hibernate.org\n" + "   - HSQLDB: hsqldb.org\n"
	        + "   - log4j: logging.apache.org\n" + "   - ant: ant.apache.org"
	        + "   - JFreeChart: jfree.org/jfreechart (included in the build but not used for now)\n"
	        + "   - JFree: jfree.org/jcommon (included in the build but not used for now)\n" + "   - dom4j: dom4j.org\n"
	        + "   - Apachage common logging: commons.apache.org/logging\n" + "   - Yahoo Finance API: finance.yahoo.com/d/quotes.csv\n"
	        + "   - Yahoo iChart API: ichart.finance.yahoo.com/table.csv"
	        + "\n\n(c) Copyright Groupe Proserus Inc. 2009 - 2011. All rights reserved.";

	static public Menu getInstance() {
		return menu;
	}

	private Menu() {
		// Build the first menu.
		JMenu menu = new JMenu(FILE);
		menu.setMnemonic(KeyEvent.VK_F);

		add(menu);

		
		// a group of JMenuItems
		JMenuItem menuItem = new JMenuItem(IMPORT, KeyEvent.VK_I);
		menuItem.setAction(new ImportFromCsvAction());
		menuItem.setText(IMPORT);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Imports transactions");
		menuItem.setName(IMPORT);
		menu.add(menuItem);

		// a group of JMenuItems
		menuItem = new JMenuItem(EXPORT, KeyEvent.VK_X);
		menuItem.setAction(new ExportToCsvAction());
		menuItem.setText(EXPORT);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Exports transactions");
		menuItem.setName(EXPORT);
		menu.add(menuItem);
		
		menu.add(new JSeparator());
		
		// a group of JMenuItems
		menuItem = new JMenuItem(CLOSE, KeyEvent.VK_W);
		menuItem.setAction(new CloseApplicationAction());
		menuItem.setText(CLOSE);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(CLOSES_THE_APPLICATION);
		menuItem.setName(CLOSE);
		menu.add(menuItem);

		// Build the first menu.
		menu = new JMenu("Actions");
		menu.setMnemonic(KeyEvent.VK_I);
		add(menu);

		// a group of JMenuItems
		menuItem = new JMenuItem();
		menuItem.setAction(new AddTransactionAction());
		menuItem.setText("Add Transaction");
		menuItem.setMnemonic(KeyEvent.VK_T);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Adds a Transaction");
		menuItem.setName("addTransaction");
		menu.add(menuItem);

		menuItem = new JMenuItem("Add Symbol", KeyEvent.VK_S);
		menuItem.setAction(new AddSymbolAction());
		menuItem.setText("Add Symbol");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Adds a Symbol");
		menuItem.setName("addSymbol");
		menu.add(menuItem);

		menu.add(new JSeparator());

		menuItem = new JMenuItem("Get Current Prices", KeyEvent.VK_P);
		menuItem.setAction(new UpdatePriceAction());
		menuItem.setText("Get Current Prices");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Get Current Prices");
		menuItem.setName("updatePrices");
		menu.add(menuItem);

		menuItem = new JMenuItem("Get Old Prices", KeyEvent.VK_O);
		menuItem.setAction(new UpdateOldPricesAction());
		menuItem.setText("Get Old Prices");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Get Old Prices");
		menuItem.setName("updateOldPrices");
		menu.add(menuItem);

		initMenuTables();

		menu = new JMenu(HELP);
		menu.setMnemonic(KeyEvent.VK_H);

		add(menu);

		// a group of JMenuItems
		menuItem = new JMenuItem(GUI_SETTINGS, KeyEvent.VK_C);
		menuItem.setAction(new ShowSettingsAction());
		menuItem.setText(GUI_SETTINGS);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(BASIC_GRAPHICAL_USER_INTERFACE_SETTINGS);
		menuItem.setName(SETTINGS);
		menu.add(menuItem);
		menu.add(new JSeparator());

		menuItem = new JMenuItem(ABOUT, KeyEvent.VK_B);
		menuItem.setAction(new ShowAboutAction());
		menuItem.setText(ABOUT);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(CLOSES_THE_APPLICATION);
		menuItem.setName(ABOUT);
		menu.add(menuItem);

	}

	public void initMenuTables() {
		JMenu menu;

		menu = new JMenu("View");
		menu.setMnemonic(KeyEvent.VK_V);

		createViewFilters(menu);
		// -----
		createViewOverview(menu);
		createViewPerformance(menu);
		createViewTransaction(menu);
		createViewSymbol(menu);

		add(menu);
	}

	private void createViewFilters(JMenu menu) {
		JMenuItem menuItem = new JMenuItem("Show/Hide Filters", KeyEvent.VK_F);
		menuItem.setAction(new ShowHideFiltersAction());
		menuItem.setText("Show/Hide Filters");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Show/Hide Filters");
		menuItem.setName("showHideFilters");
		menu.add(menuItem);
		menu.add(new JSeparator());
	}

	private void createViewOverview(JMenu menu) {
		int i;
		JMenu submenu;
		submenu = new JMenu("Overview - Symbols");
		menu.add(submenu);
		submenu.setMnemonic(KeyEvent.VK_O);

		i = 0;
		for (String str : OverviewSymbolModel.COLUMN_NAMES) {
			JMenuItem menuItem = new JCheckBoxMenuItem(str);
			menuItem.setAction(new ShowHideTableColumnsAction(OverviewSymbolTable.getInstance()));
			menuItem.setText(str);
			menuItem.setActionCommand("" + i);
			menuItem.setSelected(true);
			submenu.add(menuItem);
			i++;
		}

		submenu = new JMenu("Overview - Currencies");
		menu.add(submenu);
		submenu.setMnemonic(KeyEvent.VK_O);

		i = 0;
		for (String str : OverviewCurrencyModel.COLUMN_NAMES) {
			JMenuItem menuItem = new JCheckBoxMenuItem(str);
			menuItem.setAction(new ShowHideTableColumnsAction(OverviewCurrencyTable.getInstance()));
			menuItem.setText(str);
			menuItem.setActionCommand("" + i);
			menuItem.setSelected(true);
			submenu.add(menuItem);
			i++;
		}
	}

	private void createViewTransaction(JMenu menu) {
		JMenu submenu;
		menu.add(new JSeparator());

		int i = 0;
		submenu = new JMenu("Transactions");
		menu.add(submenu);
		submenu.setMnemonic(KeyEvent.VK_T);

		for (String str : TransactionTableModel.COLUMN_NAMES) {
			JMenuItem menuItem = new JCheckBoxMenuItem(str);
			menuItem.setAction(new ShowHideTableColumnsAction(TransactionTable.getInstance()));
			menuItem.setText(str);
			menuItem.setActionCommand("" + i);
			menuItem.setSelected(true);
			submenu.add(menuItem);
			i++;
		}
	}

	private void createViewSymbol(JMenu menu) {
		int i;
		JMenu submenu;
		menu.add(new JSeparator());

		submenu = new JMenu("Symbols / Watch List");
		menu.add(submenu);
		submenu.setMnemonic(KeyEvent.VK_S);

		i = 0;
		for (String str : SymbolsTableModel.COLUMN_NAMES) {
			JMenuItem menuItem = new JCheckBoxMenuItem(str);
			menuItem.setAction(new ShowHideTableColumnsAction(SymbolsTable.getInstance()));
			menuItem.setText(str);
			menuItem.setActionCommand("" + i);
			menuItem.setSelected(true);
			submenu.add(menuItem);
			i++;
		}
	}

	private void createViewPerformance(JMenu menu) {
		int i;
		JMenu submenu;
		menu.add(new JSeparator());
		// -----

		// Build the first menu.

		submenu = new JMenu("Performance - Symbols");
		menu.add(submenu);
		submenu.setMnemonic(KeyEvent.VK_P);

		i = 0;
		for (String str : PerformanceSymbolModel.COLUMN_NAMES) {
			JMenuItem menuItem = new JCheckBoxMenuItem();
			menuItem.setAction(new ShowHideTableColumnsAction(PerformanceSymbolTable.getInstance()));
			menuItem.setText(str);
			menuItem.setName(str);
			menuItem.setActionCommand("" + i);
			menuItem.setSelected(true);
			submenu.add(menuItem);
			i++;
		}

		submenu = new JMenu("Performance - Currencies");
		menu.add(submenu);
		submenu.setMnemonic(KeyEvent.VK_P);

		i = 0;
		for (String str : PerformanceCurrencyModel.COLUMN_NAMES) {
			JMenuItem menuItem = new JCheckBoxMenuItem(str);
			menuItem.setAction(new ShowHideTableColumnsAction(PerformanceCurrencyTable.getInstance()));
			menuItem.setActionCommand("" + i);
			menuItem.setSelected(true);
			menuItem.setText(str);
			submenu.add(menuItem);
			i++;
		}
	}
}

//TODO move this code.
