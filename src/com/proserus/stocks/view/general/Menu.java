package com.proserus.stocks.view.general;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import com.proserus.stocks.controllers.iface.PortfolioController;
import com.proserus.stocks.dao.PersistenceManager;
import com.proserus.stocks.view.common.DialogImpl;
import com.proserus.stocks.view.common.ViewControllers;
import com.proserus.stocks.view.summaries.OverviewSymbolModel;
import com.proserus.stocks.view.summaries.OverviewSymbolTable;
import com.proserus.stocks.view.summaries.PerformanceCurrencyModel;
import com.proserus.stocks.view.summaries.PerformanceCurrencyTable;
import com.proserus.stocks.view.summaries.PerformanceSymbolModel;
import com.proserus.stocks.view.summaries.PerformanceSymbolTable;
import com.proserus.stocks.view.symbols.AddEditSymbolPanelImpl;
import com.proserus.stocks.view.symbols.SymbolsTable;
import com.proserus.stocks.view.symbols.SymbolsTableModel;
import com.proserus.stocks.view.transactions.AddTransactionPanelImpl;
import com.proserus.stocks.view.transactions.TransactionTable;
import com.proserus.stocks.view.transactions.TransactionTableModel;

public class Menu extends JMenuBar implements ActionListener {
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
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Imports transactions");
		menuItem.addActionListener(this);
		menuItem.setName(IMPORT);
		menu.add(menuItem);

		// a group of JMenuItems
		menuItem = new JMenuItem(EXPORT, KeyEvent.VK_X);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Exports transactions");
		menuItem.addActionListener(this);
		menuItem.setName(EXPORT);
		menu.add(menuItem);
		
		menu.add(new JSeparator());
		
		// a group of JMenuItems
		menuItem = new JMenuItem(CLOSE, KeyEvent.VK_W);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(CLOSES_THE_APPLICATION);
		menuItem.addActionListener(this);
		menuItem.setName(CLOSE);
		menu.add(menuItem);

		// Build the first menu.
		menu = new JMenu("Actions");
		menu.setMnemonic(KeyEvent.VK_I);
		add(menu);

		// a group of JMenuItems
		menuItem = new JMenuItem("Add Transaction", KeyEvent.VK_T);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Adds a Transaction");
		menuItem.addActionListener(this);
		menuItem.setName("addTransaction");
		menu.add(menuItem);

		menuItem = new JMenuItem("Add Symbol", KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Adds a Symbol");
		menuItem.addActionListener(this);
		menuItem.setName("addSymbol");
		menu.add(menuItem);

		menu.add(new JSeparator());

		menuItem = new JMenuItem("Get Current Prices", KeyEvent.VK_P);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Get Current Prices");
		menuItem.addActionListener(this);
		menuItem.setName("updatePrices");
		menu.add(menuItem);

		menuItem = new JMenuItem("Get Old Prices", KeyEvent.VK_O);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Get Old Prices");
		menuItem.addActionListener(this);
		menuItem.setName("updateOldPrices");
		menu.add(menuItem);

		initMenuTables();

		menu = new JMenu(HELP);
		menu.setMnemonic(KeyEvent.VK_H);

		add(menu);

		// a group of JMenuItems
		menuItem = new JMenuItem(GUI_SETTINGS, KeyEvent.VK_C);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(BASIC_GRAPHICAL_USER_INTERFACE_SETTINGS);
		menuItem.addActionListener(this);
		menuItem.setName(SETTINGS);
		menu.add(menuItem);
		menu.add(new JSeparator());

		menuItem = new JMenuItem(ABOUT, KeyEvent.VK_B);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(CLOSES_THE_APPLICATION);
		menuItem.setName(ABOUT);
		menuItem.addActionListener(this);
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
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Show/Hide Filters");
		menuItem.addActionListener(this);
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
			menuItem.setActionCommand("" + i);
			menuItem.setSelected(true);
			menuItem.addActionListener(OverviewSymbolTable.getInstance());
			submenu.add(menuItem);
			i++;
		}

		submenu = new JMenu("Overview - Currencies");
		menu.add(submenu);
		submenu.setMnemonic(KeyEvent.VK_O);

		i = 0;
		for (String str : OverviewSymbolModel.COLUMN_NAMES) {
			JMenuItem menuItem = new JCheckBoxMenuItem(str);
			menuItem.setActionCommand("" + i);
			menuItem.setSelected(true);
			menuItem.addActionListener(OverviewSymbolTable.getInstance());
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
			menuItem.setActionCommand("" + i);
			menuItem.setSelected(true);
			menuItem.addActionListener(TransactionTable.getInstance());
			submenu.add(menuItem);
			i++;
		}
	}

	private void createViewSymbol(JMenu menu) {
		int i;
		JMenu submenu;
		menu.add(new JSeparator());

		submenu = new JMenu("Symbols");
		menu.add(submenu);
		submenu.setMnemonic(KeyEvent.VK_S);

		i = 0;
		for (String str : SymbolsTableModel.COLUMN_NAMES) {
			JMenuItem menuItem = new JCheckBoxMenuItem(str);
			menuItem.setActionCommand("" + i);
			menuItem.setSelected(true);
			menuItem.addActionListener(SymbolsTable.getInstance());
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
			JMenuItem menuItem = new JCheckBoxMenuItem(str);
			menuItem.setActionCommand("" + i);
			menuItem.setSelected(true);
			menuItem.addActionListener(PerformanceSymbolTable.getInstance());
			submenu.add(menuItem);
			i++;
		}

		submenu = new JMenu("Performance - Currencies");
		menu.add(submenu);
		submenu.setMnemonic(KeyEvent.VK_P);

		i = 0;
		for (String str : PerformanceCurrencyModel.COLUMN_NAMES) {
			JMenuItem menuItem = new JCheckBoxMenuItem(str);
			menuItem.setActionCommand("" + i);
			menuItem.setSelected(true);
			menuItem.addActionListener(PerformanceCurrencyTable.getInstance());
			submenu.add(menuItem);
			i++;
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JMenuItem menuItem = (JMenuItem) arg0.getSource();
		if (menuItem.getName().compareTo(CLOSE) == 0) {
			PersistenceManager.close();
			System.exit(0);

		} else if (menuItem.getName().compareTo(IMPORT) == 0) {
			
			//TODO move code to reusable location
			JFileChooser fc = new JFileChooser(new File("stock-portfolio_export.csv"));
			fc.addChoosableFileFilter(new CsvFileFilter());
			fc.setApproveButtonText("Import");
			fc.showOpenDialog(window);
			File file = fc.getSelectedFile();
			if(file!=null){
				controller.importTransactions(file);
			}

		} else if (menuItem.getName().compareTo(EXPORT) == 0) {
			//TODO move code to reusable location
			
			JFileChooser fc = new JFileChooser(new File("stock-portfolio_export.csv"));
			fc.addChoosableFileFilter(new CsvFileFilter());
			fc.setApproveButtonText("Export");
			fc.showSaveDialog(window);
			File file = fc.getSelectedFile();
			
			if(file != null){
				
				if(!file.getName().endsWith(".csv"))
				{
					file = new File(file.getPath() + ".csv");
				}
				
				ByteArrayOutputStream baos = controller.exportTransactions();
				
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(file);
					fos.write(baos.toByteArray());
					fos.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
	            }
			}
		} else if (menuItem.getName().compareTo(HELP) == 0) {
			throw new AssertionError();
		} else if (menuItem.getName().compareTo("Versions") == 0) {
			throw new AssertionError();
			// VersionsDialog.getInstance().setVisible(true);

		} else if (menuItem.getName().compareTo("addTransaction") == 0) {
			new DialogImpl(new AddTransactionPanelImpl(), "Add a transaction").setVisibile(true);

		} else if (menuItem.getName().compareTo("addSymbol") == 0) {
			new DialogImpl(new AddEditSymbolPanelImpl(true), "Add a symbol").setVisibile(true);

		} else if (menuItem.getName().compareTo("updatePrices") == 0) {
			ViewControllers.getController().updatePrices();

		} else if (menuItem.getName().compareTo("updateOldPrices") == 0) {
			ViewControllers.getController().updateHistoricalPrices();

		} else if (menuItem.getName().compareTo(ABOUT) == 0) {
			//TODO get icon from window.. but in fact, should be moved to a more logical reusable place..
			JOptionPane.showMessageDialog(window, MESSAGE, "About Proserus Stocks Portfolio", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(
			        getClass().getClassLoader().getResource("images/LogoProserus.gif")));

		} else if (menuItem.getName().compareTo(SETTINGS) == 0) {
			ColorSettingsDialog.getInstance().setVisible(true);
		} else if (menuItem.getName().compareTo("showHideFilters") == 0) {
			FilterPanelImpl.getInstance().setVisible(!FilterPanelImpl.getInstance().isVisible());
		}
	}
}

//TODO move this code.
class CsvFileFilter extends FileFilter{
	private static final String COMMA_SEPARATED_VALUE_EXPORT_FILE = "Comma Separated Value export file";
	private static final String CSV_EXTENSION = ".csv";

	@Override
    public boolean accept(File pathname) {
	    return pathname.isDirectory() || pathname.getPath().endsWith(CSV_EXTENSION);
    }

	@Override
    public String getDescription() {
	   return COMMA_SEPARATED_VALUE_EXPORT_FILE;
    }
    
}
