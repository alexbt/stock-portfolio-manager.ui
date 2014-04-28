package com.proserus.stocks.ui.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jfree.data.time.Year;

import com.google.inject.Inject;
import com.proserus.stocks.bo.common.DBVersion;
import com.proserus.stocks.bo.common.DatabasePaths;
import com.proserus.stocks.bo.symbols.CurrencyEnum;
import com.proserus.stocks.bo.symbols.DefaultCurrency;
import com.proserus.stocks.bo.symbols.HistoricalPrice;
import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bo.transactions.Label;
import com.proserus.stocks.bo.transactions.Transaction;
import com.proserus.stocks.bo.utils.PathUtils;
import com.proserus.stocks.bp.dao.PersistenceManager;
import com.proserus.stocks.bp.events.SwingEvents;
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
	private final static Logger LOGGER = Logger
			.getLogger(PortfolioController.class.getName());

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
		boolean val = symbolsBp.updateSymbol(symbol);
		analysisBp.recalculate(filter);

		SwingEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(
				filter, true));
		SwingEvents.SYMBOL_ANALYSIS_UPDATED
				.fire(analysisBp.getSymbolAnalysis());
		SwingEvents.SYMBOLS_UPDATED.fire(symbolsBp.get(filter));
		SwingEvents.SYMBOLS_LIST_UPDATED.fire(symbolsBp.get());
		SwingEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp
				.getCurrencyAnalysis());
		return val;
	}

	public void calculateSectorAnalysis() {
		analysisBp.calculateBySector(filter);
		SwingEvents.SECTOR_ANALYSIS_UPDATED
				.fire(analysisBp.getSectorAnalysis());
	}

	public void calculateLabelAnalysis() {
		analysisBp.calculateByLabel(filter);
		SwingEvents.LABEL_ANALYSIS_UPDATED.fire(analysisBp.getLabelAnalysis());
	}

	public void calculateYearAnalysis() {
		analysisBp.calculateByYear(filter, getFirstYear());
		SwingEvents.YEAR_ANALYSIS_UPDATED.fire(analysisBp.getYearAnalysis());
	}

	public boolean remove(Symbol s) {
		if (transactionsBp.contains(s)) {
			return false;
		}
		symbolsBp.remove(s);
		SwingEvents.SYMBOLS_UPDATED.fire(symbolsBp.get(filter));
		SwingEvents.SYMBOLS_LIST_UPDATED.fire(symbolsBp.get());
		return true;
	}

	public Year getFirstYear() {// FIXME Year JFree
		return transactionsBp.getFirstYear();
	}

	public Symbol addSymbol(Symbol symbol) {
		setDefaultCurrency(((CurrencyEnum) symbol.getCurrency()));
		symbol = symbolsBp.add(symbol);
		SwingEvents.SYMBOLS_UPDATED.fire(symbolsBp.get(filter));
		SwingEvents.SYMBOLS_LIST_UPDATED.fire(symbolsBp.get());
		return symbol;
	}

	public Symbol getSymbol(String ticker) {
		return symbolsBp.getSymbol(ticker);
	}

	public void setCustomFilter(String custom) {
		throw new AssertionError();
	}

	public void refresh() {
		// analysisBp.recalculate(new FilterBp());
		refreshFilteredData();
		refreshOther();
	}

	public void refreshOther() {
		SwingEvents.SYMBOLS_UPDATED.fire(symbolsBp.get(filter));
		SwingEvents.SYMBOLS_LIST_UPDATED.fire(symbolsBp.get());
		SwingEvents.LABELS_UPDATED.fire(labelsBp.get());
	}

	public void refreshFilteredData() {
		analysisBp.recalculate(filter);
		SwingEvents.SYMBOL_ANALYSIS_UPDATED
				.fire(analysisBp.getSymbolAnalysis());
		SwingEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp
				.getCurrencyAnalysis());
		SwingEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(
				filter, true));
		SwingEvents.FILTER_SYMBOLS.fire(symbolsBp.get(filter));
	}

	public Transaction addTransaction(Transaction t) {
		setDefaultCurrency(((CurrencyEnum) t.getSymbol().getCurrency()));
		Symbol s = addSymbol(t.getSymbol());
		t.setSymbol(s);
		t = transactionsBp.add(t);
		analysisBp.recalculate(filter);

		SwingEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(
				filter, true));
		SwingEvents.SYMBOL_ANALYSIS_UPDATED
				.fire(analysisBp.getSymbolAnalysis());
		SwingEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp
				.getCurrencyAnalysis());
		return t;
	}

	public void remove(Transaction t) {
		for (Object o : t.getLabelsValues().toArray()) {
			t.removeLabel((Label) o);
		}
		transactionsBp.remove(t);
		analysisBp.recalculate(filter);

		SwingEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(
				filter, true));
		SwingEvents.SYMBOL_ANALYSIS_UPDATED
				.fire(analysisBp.getSymbolAnalysis());
		SwingEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp
				.getCurrencyAnalysis());
	}

	public void remove(Label label) {
		Collection<Transaction> transactions = transactionsBp
				.getTransactionsByLabel(label);
		for (Transaction t : transactions) {
			t.removeLabel(label);
		}
		labelsBp.remove(label);
		SwingEvents.LABELS_UPDATED.fire(labelsBp.get());
		analysisBp.recalculate(filter);

		SwingEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(
				filter, true));
		SwingEvents.SYMBOL_ANALYSIS_UPDATED
				.fire(analysisBp.getSymbolAnalysis());
		SwingEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp
				.getCurrencyAnalysis());
	}

	public void updateTransaction(Transaction t) {
		transactionsBp.updateTransaction(t);
		analysisBp.recalculate(filter);

		SwingEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(
				filter, true));
		SwingEvents.SYMBOL_ANALYSIS_UPDATED
				.fire(analysisBp.getSymbolAnalysis());
		SwingEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp
				.getCurrencyAnalysis());
	}

	public Label addLabel(Label label) {
		Label l = labelsBp.add(label);
		analysisBp.recalculate(filter);

		SwingEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(
				filter, true));
		SwingEvents.SYMBOL_ANALYSIS_UPDATED
				.fire(analysisBp.getSymbolAnalysis());
		SwingEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp
				.getCurrencyAnalysis());
		SwingEvents.LABELS_UPDATED.fire(labelsBp.get());
		return l;
	}

	public Collection<Label> getLabels() {
		return labelsBp.get();
	}

	public void updatePrices() {
		symbolsBp.updatePrices(filter);
		analysisBp.recalculate(filter);

		SwingEvents.SYMBOLS_CURRENT_PRICE_UPDATED.fire();
		SwingEvents.SYMBOLS_UPDATED.fire(symbolsBp.get(filter));
		SwingEvents.SYMBOLS_LIST_UPDATED.fire(symbolsBp.get());
		SwingEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(
				filter, true));
		SwingEvents.SYMBOL_ANALYSIS_UPDATED
				.fire(analysisBp.getSymbolAnalysis());
		SwingEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp
				.getCurrencyAnalysis());
	}

	public void updateHistoricalPrices() {
		symbolsBp.updateHistoricalPrices(filter);
		analysisBp.recalculate(filter);

		SwingEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(
				filter, true));
		SwingEvents.SYMBOL_ANALYSIS_UPDATED
				.fire(analysisBp.getSymbolAnalysis());
		SwingEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp
				.getCurrencyAnalysis());
		SwingEvents.SYMBOLS_HISTORICAL_PRICE_UPDATED.fire();
	}

	public void update(HistoricalPrice hPrice) {
		symbolsBp.update(hPrice);
		analysisBp.recalculate(filter);

		SwingEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(
				filter, true));
		SwingEvents.SYMBOL_ANALYSIS_UPDATED
				.fire(analysisBp.getSymbolAnalysis());
		SwingEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp
				.getCurrencyAnalysis());
	}

	public ByteArrayOutputStream exportTransactions(boolean isFiltered) {
		try {
			if (isFiltered) {
				return importExportBp.exportTransactions(transactionsBp
						.getTransactions(filter, true));
			} else {
				return importExportBp.exportTransactions(transactionsBp
						.getTransactions());
			}
		} catch (IOException e) {
		}
		return null;
	}

	public ByteArrayOutputStream exportTransactions() {
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
		persistenceManager.close();
	}

	public void setSelection(Transaction t) {
		SwingEvents.TRANSACTION_SELECTION_CHANGED.fire(t);
	}

	public void setSelection(Symbol s) {
		SwingEvents.SYMBOL_SELECTION_CHANGED.fire(s);
	}

	public CurrencyEnum getDefaultCurrency() {
		return currencies.getDefault();
	}

	public void setDefaultCurrency(CurrencyEnum currency) {
		currencies.setDefault(currency);
		SwingEvents.CURRENCY_DEFAULT_CHANGED.fire(currencies.getDefault());
		currencies.save();
	}

	public void checkDatabaseVersion() {
		DBVersion v = databaseVersionningBp.retrieveCurrentVersion();
		if (v.getDatabaseVersion() < DatabaseUpgradeEnum.getLatestVersion()) {
			databaseVersionningBp.upgrade(v);
		}
	}

	public void checkNewVersion() {
		Double version = databaseVersionningBp
				.retrieveLatestVersion(DatabaseVersionningBpImpl.LATEST_VERSION_URL);
		if (version != null) {
			if (databaseVersionningBp.isNewerVersion(version)) {
				JOptionPane
						.showMessageDialog(
								null,
								"The version "
										+ version
										+ " is now available!\nYou can download it from the project's website",
								"A new version is now available!",
								JOptionPane.INFORMATION_MESSAGE, null);
				databaseVersionningBp.writeVersion(new Properties(),
						String.valueOf(version));
			}
		}
	}

	public void checkDatabaseDuplicate() {
		final DatabasePaths dbPaths = new DatabasePaths();
		
		dbPaths.setBinaryCurrentFolder(PathUtils.getInstallationFolder());
		dbPaths.setOsCurrentFolder(PathUtils.getCurrentFolder());

		LOGGER.info("binaryCurrentFolder : " + dbPaths.getBinaryCurrentFolder());
		LOGGER.info("osCurrentFolder: " + dbPaths.getOsCurrentFolder());
		boolean isBinAndOsSame = new File(dbPaths.getBinaryCurrentFolder())
				.equals(new File(dbPaths.getOsCurrentFolder()));

		List<String> col = RecursiveFileUtils.listFiles(
				new File(dbPaths.getBinaryCurrentFolder()), "db.script", 3);
		if (!isBinAndOsSame) {
			col.addAll(RecursiveFileUtils.listFiles(
					new File(dbPaths.getOsCurrentFolder()), "db.script", 3));
		}
		if (col.isEmpty()) {
			col.add(dbPaths.getBinaryCurrentFolder() + "\\data\\db");
		}
		Collections.sort(col);
		for (String file : col) {
			dbPaths.addDb(StringUtils.removeEnd(file, ".script"));
		}

		LOGGER.info("Databases detected: " + dbPaths.getDatabases());
		SwingEvents.DATABASE_DETECTED.fire(dbPaths);
	}

	public void deleteAll() {
		SwingEvents.CURRENT_DATABASE_DELETED.fire();
		System.exit(0);
	}
}
