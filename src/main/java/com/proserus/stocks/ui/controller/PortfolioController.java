package com.proserus.stocks.ui.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.proserus.stocks.bo.common.DBVersion;
import com.proserus.stocks.bo.common.Database;
import com.proserus.stocks.bo.common.DatabasePaths;
import com.proserus.stocks.bo.symbols.CurrencyEnum;
import com.proserus.stocks.bo.symbols.DefaultCurrency;
import com.proserus.stocks.bo.symbols.HistoricalPrice;
import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bo.transactions.Label;
import com.proserus.stocks.bo.transactions.Transaction;
import com.proserus.stocks.bo.utils.PathUtils;
import com.proserus.stocks.bp.dao.PersistenceManager;
import com.proserus.stocks.bp.events.ModelChangeEvents;
import com.proserus.stocks.bp.model.Filter;
import com.proserus.stocks.bp.services.AnalysisBp;
import com.proserus.stocks.bp.services.DatabaseVersionningBp;
import com.proserus.stocks.bp.services.ImportExportBp;
import com.proserus.stocks.bp.services.LabelsBp;
import com.proserus.stocks.bp.services.SymbolsBp;
import com.proserus.stocks.bp.services.TransactionsBp;
import com.proserus.stocks.bp.utils.RecursiveFileUtils;
import com.proserus.stocks.ui.dbutils.DatabaseUpgradeEnum;
import com.proserus.stocks.ui.dbutils.DatabaseVersionningBpImpl;

public class PortfolioController {
	private final static Logger LOGGER = LoggerFactory.getLogger(PortfolioController.class);

	@Inject
	private SymbolsBp symbolsBp;
	@Inject
	private LabelsBp labelsBp;
	@Inject
	private TransactionsBp transactionsBp;
	@Inject
	private AnalysisBp analysisBp;
	@Inject
	private ImportExportBp importExportBp;
	@Inject
	private DatabaseVersionningBp databaseVersionningBp;
	private DefaultCurrency currencies = new DefaultCurrency();

	@Inject
	Filter filter;

	@Inject
	private PersistenceManager persistenceManager;

	public PortfolioController() {
	}

	public boolean updateSymbol(Symbol symbol) {
		LOGGER.info("Updating {}", new Object[] { symbol });
		boolean val = symbolsBp.updateSymbol(symbol);
		analysisBp.recalculate(filter);

		ModelChangeEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(filter, true));
		ModelChangeEvents.SYMBOL_ANALYSIS_UPDATED.fire(analysisBp.getSymbolAnalysis());
		ModelChangeEvents.SYMBOLS_UPDATED.fire(symbolsBp.get(filter));
		ModelChangeEvents.SYMBOLS_LIST_UPDATED.fire(symbolsBp.get());
		ModelChangeEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp.getCurrencyAnalysis());
		return val;
	}

	public void calculateSectorAnalysis() {
		LOGGER.info("calculateSectorAnalysis");
		analysisBp.calculateBySector(filter);
		ModelChangeEvents.SECTOR_ANALYSIS_UPDATED.fire(analysisBp.getSectorAnalysis());
	}

	public void calculateLabelAnalysis() {
		LOGGER.info("calculateLabelAnalysis");
		analysisBp.calculateByLabel(filter);
		ModelChangeEvents.LABEL_ANALYSIS_UPDATED.fire(analysisBp.getLabelAnalysis());
	}

	public void calculateYearAnalysis() {
		LOGGER.info("calculateYearAnalysis");
		analysisBp.calculateByYear(filter, getFirstYear());
		ModelChangeEvents.YEAR_ANALYSIS_UPDATED.fire(analysisBp.getYearAnalysis());
	}

	public boolean remove(Symbol s) {
		LOGGER.info("Removing {}", new Object[] { s });
		if (transactionsBp.contains(s)) {
			return false;
		}
		symbolsBp.remove(s);
		ModelChangeEvents.SYMBOLS_UPDATED.fire(symbolsBp.get(filter));
		ModelChangeEvents.SYMBOLS_LIST_UPDATED.fire(symbolsBp.get());
		return true;
	}

	public int getFirstYear() {
		return transactionsBp.getFirstYear();
	}

	public Symbol addSymbol(Symbol symbol) {
		LOGGER.info("Adding {}", new Object[] { symbol });
		setDefaultCurrency(((CurrencyEnum) symbol.getCurrency()));
		symbol = symbolsBp.add(symbol);
		ModelChangeEvents.SYMBOLS_UPDATED.fire(symbolsBp.get(filter));
		ModelChangeEvents.SYMBOLS_LIST_UPDATED.fire(symbolsBp.get());
		return symbol;
	}

	public Symbol getSymbol(String ticker) {
		Validate.notNull(ticker);
		Validate.notEmpty(ticker);

		ticker = ticker.toLowerCase();
		LOGGER.info("retriving symbol {}", new Object[] { ticker });
		return symbolsBp.getSymbol(ticker);
	}

	public Collection<Symbol> getSymbols() {
		LOGGER.info("retriving all symbols");
		return symbolsBp.get();
	}

	public void setCustomFilter(String custom) {
		throw new AssertionError();
	}

	public void refresh() {
		LOGGER.info("refresh");
		// analysisBp.recalculate(new FilterBp());
		refreshFilteredData();
		refreshOther();
	}

	public void refreshOther() {
		LOGGER.info("refreshOther");
		ModelChangeEvents.SYMBOLS_UPDATED.fire(symbolsBp.get(filter));
		ModelChangeEvents.SYMBOLS_LIST_UPDATED.fire(symbolsBp.get());
		ModelChangeEvents.LABELS_UPDATED.fire(labelsBp.get());
	}

	public void refreshFilteredData() {
		LOGGER.info("refreshFilteredData");
		analysisBp.recalculate(filter);
		ModelChangeEvents.SYMBOL_ANALYSIS_UPDATED.fire(analysisBp.getSymbolAnalysis());
		ModelChangeEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp.getCurrencyAnalysis());
		ModelChangeEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(filter, true));
		ModelChangeEvents.FILTER_SYMBOLS.fire(symbolsBp.get(filter));
	}

	public Transaction addTransaction(Transaction t) {
		LOGGER.info("Adding {}", new Object[] { t });
		setDefaultCurrency(((CurrencyEnum) t.getSymbol().getCurrency()));
		Symbol s = addSymbol(t.getSymbol());
		t.setSymbol(s);
		t = transactionsBp.add(t);
		analysisBp.recalculate(filter);

		ModelChangeEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(filter, true));
		ModelChangeEvents.SYMBOL_ANALYSIS_UPDATED.fire(analysisBp.getSymbolAnalysis());
		ModelChangeEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp.getCurrencyAnalysis());
		return t;
	}

	public void remove(Transaction t) {
		LOGGER.info("Removing {}", new Object[] { t });
		for (Object o : t.getLabelsValues().toArray()) {
			t.removeLabel((Label) o);
		}
		transactionsBp.remove(t);
		analysisBp.recalculate(filter);

		ModelChangeEvents.SYMBOL_ANALYSIS_UPDATED.fire(analysisBp.getSymbolAnalysis());
		ModelChangeEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp.getCurrencyAnalysis());
		ModelChangeEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(filter, true));
	}

	public void remove(Label label) {
		LOGGER.info("Removing {}", new Object[] { label });
		Collection<Transaction> transactions = transactionsBp.getTransactionsByLabel(label);
		for (Transaction t : transactions) {
			t.removeLabel(label);
		}
		labelsBp.remove(label);
		ModelChangeEvents.LABELS_UPDATED.fire(labelsBp.get());
		analysisBp.recalculate(filter);

		ModelChangeEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(filter, true));
		ModelChangeEvents.SYMBOL_ANALYSIS_UPDATED.fire(analysisBp.getSymbolAnalysis());
		ModelChangeEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp.getCurrencyAnalysis());
	}

	public void updateTransaction(Transaction t) {
		LOGGER.info("Updating {}", new Object[] { t });
		transactionsBp.updateTransaction(t);
		analysisBp.recalculate(filter);

		ModelChangeEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(filter, true));
		ModelChangeEvents.SYMBOL_ANALYSIS_UPDATED.fire(analysisBp.getSymbolAnalysis());
		ModelChangeEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp.getCurrencyAnalysis());
	}

	public Label addLabel(Label label) {
		LOGGER.info("Adding {}", new Object[] { label });
		Label l = labelsBp.add(label);
		analysisBp.recalculate(filter);

		ModelChangeEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(filter, true));
		ModelChangeEvents.SYMBOL_ANALYSIS_UPDATED.fire(analysisBp.getSymbolAnalysis());
		ModelChangeEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp.getCurrencyAnalysis());
		ModelChangeEvents.LABELS_UPDATED.fire(labelsBp.get());
		return l;
	}

	public Collection<Label> getLabels() {
		LOGGER.info("Retrieving all label");
		return labelsBp.get();
	}

	public void updatePrices() {
		LOGGER.info("updatePrices");
		symbolsBp.updatePrices(filter);
		analysisBp.recalculate(filter);

		ModelChangeEvents.SYMBOLS_CURRENT_PRICE_UPDATED.fire();
		ModelChangeEvents.SYMBOLS_UPDATED.fire(symbolsBp.get(filter));
		ModelChangeEvents.SYMBOLS_LIST_UPDATED.fire(symbolsBp.get());
		ModelChangeEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(filter, true));
		ModelChangeEvents.SYMBOL_ANALYSIS_UPDATED.fire(analysisBp.getSymbolAnalysis());
		ModelChangeEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp.getCurrencyAnalysis());
	}

	public void updateHistoricalPrices() {
		symbolsBp.updateHistoricalPrices(filter);
		analysisBp.recalculate(filter);

		ModelChangeEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(filter, true));
		ModelChangeEvents.SYMBOL_ANALYSIS_UPDATED.fire(analysisBp.getSymbolAnalysis());
		ModelChangeEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp.getCurrencyAnalysis());
		ModelChangeEvents.SYMBOLS_HISTORICAL_PRICE_UPDATED.fire();
	}

	public void update(HistoricalPrice hPrice) {
		symbolsBp.update(hPrice);
		analysisBp.recalculate(filter);

		ModelChangeEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(filter, true));
		ModelChangeEvents.SYMBOL_ANALYSIS_UPDATED.fire(analysisBp.getSymbolAnalysis());
		ModelChangeEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp.getCurrencyAnalysis());
	}

	public String exportTransactions(boolean isFiltered) {
		try {
			if (isFiltered) {
				return importExportBp.exportTransactions(transactionsBp.getTransactions(filter, true));
			} else {
				return importExportBp.exportTransactions(transactionsBp.getTransactions());
			}
		} catch (IOException e) {
		}
		return null;
	}

	public String exportTransactions() {
		return exportTransactions(false);
	}

	public void importTransactions(File file) {
		try {
			importExportBp.importTransactions(file, getDefaultCurrency());
			refresh();
			// refresh();
		} catch (FileNotFoundException e) {
			// TODO
		}

	}

	public void cleanup() {
		LOGGER.info("cleanup");
		persistenceManager.close();
	}

	public PersistenceManager getPersistenceManager() {
		return persistenceManager;
	}

	public void setSelection(Transaction t) {
		LOGGER.info("setSelection {}", new Object[] { t });
		ModelChangeEvents.TRANSACTION_SELECTION_CHANGED.fire(t);
	}

	public void setSelection(Symbol s) {
		LOGGER.info("setSelection {}", new Object[] { s });
		ModelChangeEvents.SYMBOL_SELECTION_CHANGED.fire(s);
	}

	public CurrencyEnum getDefaultCurrency() {
		CurrencyEnum def = currencies.getDefault();
		LOGGER.info("get default {}", new Object[] { def });
		return def;
	}

	public void setDefaultCurrency(CurrencyEnum currency) {
		LOGGER.info("set default {}", new Object[] { currency });
		currencies.setDefault(currency);
		ModelChangeEvents.CURRENCY_DEFAULT_CHANGED.fire(currencies.getDefault());
		currencies.save();
	}

	public void checkDatabaseVersion() {
		DBVersion v = databaseVersionningBp.retrieveCurrentVersion();
		if (v.getDatabaseVersion() < DatabaseUpgradeEnum.getLatestVersion()) {
			databaseVersionningBp.upgrade(v);
		}
	}

	public DBVersion retrieveCurrentVersion() {
		return databaseVersionningBp.retrieveCurrentVersion();
	}

	public void checkNewVersion() {
		Double version = databaseVersionningBp.retrieveLatestVersion(DatabaseVersionningBpImpl.LATEST_VERSION_URL);
		if (version != null) {
			if (databaseVersionningBp.isNewerVersion(version)) {
				JOptionPane.showMessageDialog(null, "The version " + version
						+ " is now available!\nYou can download it from the project's website", "A new version is now available!",
						JOptionPane.INFORMATION_MESSAGE, null);
				databaseVersionningBp.writeVersion(new Properties(), String.valueOf(version));
			}
		}
	}

	public void checkDatabaseDuplicate() {
		final DatabasePaths dbPaths = new DatabasePaths();

		dbPaths.setBinaryCurrentFolder(PathUtils.getInstallationFolder());
		dbPaths.setOsCurrentFolder(PathUtils.getCurrentFolder());

		boolean isBinAndOsSame = new File(dbPaths.getBinaryCurrentFolder()).equals(new File(dbPaths.getOsCurrentFolder()));

		List<File> col = RecursiveFileUtils.listFiles(new File(dbPaths.getBinaryCurrentFolder()), 3, "db.script");
		if (!isBinAndOsSame) {
			col.addAll(RecursiveFileUtils.listFiles(new File(dbPaths.getOsCurrentFolder()), 3, "db.script"));
		}
		if (col.isEmpty()) {
			dbPaths.addDb(new Database(dbPaths.getBinaryCurrentFolder() + System.getProperty("file.separator") + "data"
					+ System.getProperty("file.separator") + "db"));
		} else {
			for (File file : col) {
				dbPaths.addDb(new Database(StringUtils.removeEnd(file.getAbsolutePath(), ".script")));
			}
		}

		LOGGER.info("Databases detected: " + dbPaths.getDatabases());
		ModelChangeEvents.DATABASE_DETECTED.fire(dbPaths);
	}

	public void deleteAll() {
		ModelChangeEvents.CURRENT_DATABASE_DELETED.fire();
		System.exit(0);
	}
}
