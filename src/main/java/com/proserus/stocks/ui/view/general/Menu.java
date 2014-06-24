package com.proserus.stocks.ui.view.general;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventBus;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.ModelChangeEvents;
import com.proserus.stocks.ui.view.actions.AddSymbolAction;
import com.proserus.stocks.ui.view.actions.AddTransactionAction;
import com.proserus.stocks.ui.view.actions.BackupAction;
import com.proserus.stocks.ui.view.actions.CloseApplicationAction;
import com.proserus.stocks.ui.view.actions.DeleteAllAction;
import com.proserus.stocks.ui.view.actions.ExportToCsvAction;
import com.proserus.stocks.ui.view.actions.ImportFromCsvAction;
import com.proserus.stocks.ui.view.actions.RemoveSymbolAction;
import com.proserus.stocks.ui.view.actions.RemoveTransactionAction;
import com.proserus.stocks.ui.view.actions.ShowAboutAction;
import com.proserus.stocks.ui.view.actions.ShowEditSymbolAction;
import com.proserus.stocks.ui.view.actions.ShowHideFiltersAction;
import com.proserus.stocks.ui.view.actions.ShowHideTableColumnsAction;
import com.proserus.stocks.ui.view.actions.ShowSettingsAction;
import com.proserus.stocks.ui.view.actions.UpdateOldPricesAction;
import com.proserus.stocks.ui.view.actions.UpdatePriceAction;
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

public class Menu extends JMenuBar implements EventListener {
	private static final String BACKS_UP_THE_PORTFOLIO_AND_LOGS = "Backs up the portfolio and logs";
	private static final String BACKUP_PORTFOLIO = "Backup portfolio";
	private static final String DELETE_CURRENT_PORTFOLIO = "Delete current portfolio";
	private static final long serialVersionUID = 201404041920L;
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

	private static Menu menu = new Menu();

	static public Menu getInstance() {
		return menu;
	}

	private JMenuItem editSymbolMenuItem;
	private JMenuItem removeSymbolMenuItem;

	private Menu() {
		// Build the first menu.
		JMenu menu = new JMenu(FILE);
		menu.setMnemonic(KeyEvent.VK_F);

		add(menu);

		// a group of JMenuItems
		JMenuItem menuItem = new JMenuItem(IMPORT, ImportFromCsvAction.keyEvent);
		menuItem.setAction(ImportFromCsvAction.getInstance());
		menuItem.setText(IMPORT);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				ImportFromCsvAction.keyEvent, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				"Imports transactions");
		menuItem.setName(IMPORT);
		menu.add(menuItem);

		// a group of JMenuItems
		menuItem = new JMenuItem(EXPORT, ExportToCsvAction.keyEvent);
		menuItem.setAction(ExportToCsvAction.getInstance());
		menuItem.setText(EXPORT);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				ExportToCsvAction.keyEvent, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				"Exports transactions");
		menuItem.setName(EXPORT);
		menu.add(menuItem);

		menu.add(new JSeparator());

		// a group of JMenuItems
		menuItem = new JMenuItem(BACKUP_PORTFOLIO);
		menuItem.setAction(BackupAction.getInstance());
		menuItem.setText(BACKUP_PORTFOLIO);
		menuItem.getAccessibleContext().setAccessibleDescription(
				BACKS_UP_THE_PORTFOLIO_AND_LOGS);
		menuItem.setName(BACKUP_PORTFOLIO);
		menu.add(menuItem);

		// a group of JMenuItems
		menuItem = new JMenuItem(DELETE_CURRENT_PORTFOLIO);
		menuItem.setAction(DeleteAllAction.getInstance());
		menuItem.setText(DELETE_CURRENT_PORTFOLIO);
		menuItem.getAccessibleContext().setAccessibleDescription(
				"Deletes current portfolio and all its entries");
		menuItem.setName(DELETE_CURRENT_PORTFOLIO);
		menu.add(menuItem);

		menu.add(new JSeparator());
		// a group of JMenuItems
		menuItem = new JMenuItem(CLOSE, CloseApplicationAction.keyEvent);
		menuItem.setAction(CloseApplicationAction.getInstance());
		menuItem.setText(CLOSE);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				CloseApplicationAction.keyEvent, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				CLOSES_THE_APPLICATION);
		menuItem.setName(CLOSE);
		menu.add(menuItem);

		// Build the first menu.
		menu = new JMenu("Actions");
		menu.setMnemonic(KeyEvent.VK_I);
		add(menu);

		// a group of JMenuItems
		menuItem = new JMenuItem();
		menuItem.setAction(AddTransactionAction.getInstance());
		menuItem.setText("Add Transaction");
		menuItem.setMnemonic(AddTransactionAction.keyEvent);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				AddTransactionAction.keyEvent, KeyEvent.CTRL_DOWN_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				"Adds a Transaction");
		menuItem.setName("addTransaction");
		menu.add(menuItem);

		// a group of JMenuItems
		menuItem = new JMenuItem();
		menuItem.setAction(RemoveTransactionAction.getInstance());
		menuItem.setText("Remove Transaction");
		menuItem.setMnemonic(RemoveTransactionAction.keyEvent);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				RemoveTransactionAction.keyEvent, KeyEvent.CTRL_DOWN_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				"Removes a Transaction");
		menuItem.setName("removeTransaction");
		menu.add(menuItem);

		menu.add(new JSeparator());

		menuItem = new JMenuItem("Add Symbol");
		menuItem.setAction(AddSymbolAction.getInstance());
		menuItem.setText("Add Symbol");
		menuItem.setMnemonic(AddSymbolAction.keyEvent);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				AddSymbolAction.keyEvent, KeyEvent.CTRL_DOWN_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				"Adds a Symbol");
		menuItem.setName("addSymbol");
		menu.add(menuItem);

		editSymbolMenuItem = new JMenuItem("Edit Symbol");
		editSymbolMenuItem.setAction(ShowEditSymbolAction.getInstance());
		editSymbolMenuItem.setText("Edit Symbol");
		editSymbolMenuItem.setMnemonic(ShowEditSymbolAction.keyEvent);
		editSymbolMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				ShowEditSymbolAction.keyEvent, ActionEvent.CTRL_MASK));
		editSymbolMenuItem.getAccessibleContext().setAccessibleDescription(
				"Edits a Symbol");
		editSymbolMenuItem.setName("editSymbol");
		editSymbolMenuItem.setEnabled(false);
		menu.add(editSymbolMenuItem);

		removeSymbolMenuItem = new JMenuItem("Remove Symbol");
		removeSymbolMenuItem.setAction(RemoveSymbolAction.getInstance());
		removeSymbolMenuItem.setText("Remove Symbol");
		removeSymbolMenuItem.setMnemonic(RemoveSymbolAction.keyEvent);
		removeSymbolMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				RemoveSymbolAction.keyEvent, ActionEvent.CTRL_MASK));
		removeSymbolMenuItem.getAccessibleContext().setAccessibleDescription(
				"Removes a Symbol");
		removeSymbolMenuItem.setName("removeSymbol");
		removeSymbolMenuItem.setEnabled(false);
		menu.add(removeSymbolMenuItem);

		menu.add(new JSeparator());

		menuItem = new JMenuItem("Get Current Prices",
				UpdatePriceAction.keyEvent);
		menuItem.setAction(UpdatePriceAction.getInstance());
		menuItem.setText("Get Current Prices");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				UpdatePriceAction.keyEvent, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				"Get Current Prices");
		menuItem.setName("updatePrices");
		menu.add(menuItem);

		menuItem = new JMenuItem("Get Historical Prices",
				UpdateOldPricesAction.keyEvent);
		menuItem.setAction(UpdateOldPricesAction.getInstance());
		menuItem.setText("Get Historical Prices");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				UpdateOldPricesAction.keyEvent, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				"Get Historical Prices");
		menuItem.setName("updateOldPrices");
		menu.add(menuItem);

		initMenuTables();

		menu = new JMenu(HELP);
		menu.setMnemonic(KeyEvent.VK_H);

		add(menu);

		// a group of JMenuItems
		menuItem = new JMenuItem(GUI_SETTINGS, ShowSettingsAction.keyEvent);
		menuItem.setAction(ShowSettingsAction.getInstance());
		menuItem.setText(GUI_SETTINGS);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				ShowSettingsAction.keyEvent, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				BASIC_GRAPHICAL_USER_INTERFACE_SETTINGS);
		menuItem.setName(SETTINGS);
		menu.add(menuItem);
		menu.add(new JSeparator());

		menuItem = new JMenuItem(ABOUT, ShowAboutAction.keyEvent);
		menuItem.setAction(ShowAboutAction.getInstance());
		menuItem.setText(ABOUT);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				ShowAboutAction.keyEvent, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				CLOSES_THE_APPLICATION);
		menuItem.setName(ABOUT);
		menu.add(menuItem);

		EventBus.getInstance().add(this, ModelChangeEvents.SYMBOL_SELECTION_CHANGED);

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
		JMenuItem menuItem = new JMenuItem("Show/Hide Filters",
				ShowHideFiltersAction.keyEvent);
		menuItem.setAction(ShowHideFiltersAction.getInstance());
		menuItem.setText("Show/Hide Filters");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				ShowHideFiltersAction.keyEvent, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				"Show/Hide Filters");
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
			menuItem.setAction(new ShowHideTableColumnsAction(
					OverviewSymbolTable.getInstance()));
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
			menuItem.setAction(new ShowHideTableColumnsAction(
					OverviewCurrencyTable.getInstance()));
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
			menuItem.setAction(new ShowHideTableColumnsAction(TransactionTable
					.getInstance()));
			menuItem.setText(str);
			menuItem.setActionCommand("" + i);
			menuItem.setSelected(true);
			submenu.add(menuItem);
			i++;
		}
	}

	private void createViewSymbol(JMenu menu) {
		int i;
		menu.add(new JSeparator());
		JMenu submenu;

		submenu = new JMenu("Symbols / Watch List");
		menu.add(submenu);
		submenu.setMnemonic(KeyEvent.VK_S);

		i = 0;
		for (String str : SymbolsTableModel.COLUMN_NAMES) {
			JMenuItem menuItem = new JCheckBoxMenuItem(str);
			menuItem.setAction(new ShowHideTableColumnsAction(SymbolsTable
					.getInstance()));
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
			menuItem.setAction(new ShowHideTableColumnsAction(
					PerformanceSymbolTable.getInstance()));
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
			menuItem.setAction(new ShowHideTableColumnsAction(
					PerformanceCurrencyTable.getInstance()));
			menuItem.setActionCommand("" + i);
			menuItem.setSelected(true);
			menuItem.setText(str);
			submenu.add(menuItem);
			i++;
		}
	}

	@Override
	public void update(Event event, Object model) {
		if (ModelChangeEvents.SYMBOL_SELECTION_CHANGED.equals(event)) {
			Symbol s = ModelChangeEvents.SYMBOL_SELECTION_CHANGED.resolveModel(model);
			if (ModelChangeEvents.SYMBOL_SELECTION_CHANGED.resolveModel(model) != null) {
				editSymbolMenuItem.setText("Edit symbol '" + s.getTicker()
						+ "'");
				removeSymbolMenuItem.setText("Remove symbol '" + s.getTicker()
						+ "'");
			} else {
				editSymbolMenuItem.setText("Edit symbol");
				removeSymbolMenuItem.setText("Remove symbol");
			}
			editSymbolMenuItem.setEnabled(s != null);
			removeSymbolMenuItem.setEnabled(s != null);

		}
	}
}

// TODO move this code.

