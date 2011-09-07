package com.proserus.stocks;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import org.jfree.data.time.Year;

import com.google.inject.Inject;
import com.proserus.stocks.bp.AnalysisBp;
import com.proserus.stocks.bp.FilterBp;
import com.proserus.stocks.bp.ImportExportBp;
import com.proserus.stocks.bp.LabelsBp;
import com.proserus.stocks.bp.SharedFilter;
import com.proserus.stocks.bp.SymbolsBp;
import com.proserus.stocks.bp.TransactionsBp;
import com.proserus.stocks.controllers.CurrencyControllerImpl;
import com.proserus.stocks.dao.PersistenceManager;
import com.proserus.stocks.events.SwingEvents;
import com.proserus.stocks.model.symbols.CurrencyEnum;
import com.proserus.stocks.model.symbols.HistoricalPrice;
import com.proserus.stocks.model.symbols.Symbol;
import com.proserus.stocks.model.transactions.Label;
import com.proserus.stocks.model.transactions.Transaction;
import com.proserus.stocks.view.common.ViewControllers;

public class PortfolioController{
	@Inject private SymbolsBp symbolsBp;
	@Inject private LabelsBp labelsBp;
	@Inject private TransactionsBp transactionsBp;
	@Inject private AnalysisBp analysisBp;
	@Inject private SharedFilter sharedFilter;//TODO Merge sharedFilter and/or filterBp..
	@Inject private FilterBp filterBp;
	@Inject private ImportExportBp importExportBp;
	@Inject private CurrencyControllerImpl currencyController;
	
	@Inject
	private PersistenceManager persistenceManager;
	
	public PortfolioController() {
	}

	public boolean updateSymbol(Symbol symbol) {
		boolean val = symbolsBp.updateSymbol(symbol);
		analysisBp.recalculate(filterBp);
		
		SwingEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(ViewControllers.getSharedFilter(),true));
		SwingEvents.SYMBOL_ANALYSIS_UPDATED.fire(analysisBp.getSymbolAnalysis());
		SwingEvents.SYMBOLS_UPDATED.fire(symbolsBp.get());
		SwingEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp.getCurrencyAnalysis());
		return val;
	}

	
	
	public boolean remove(Symbol s) {
		if (transactionsBp.contains(s)) {
			return false;
		}
		symbolsBp.remove(s);
		return true;
	}

	
	
	public Year getFirstYear() {
		return transactionsBp.getFirstYear();
	}

	
	public Symbol addSymbol(Symbol symbol) {
		currencyController.setDefaultCurrency(((CurrencyEnum) symbol.getCurrency()));
		symbol =  symbolsBp.add(symbol);
		SwingEvents.SYMBOLS_UPDATED.fire(symbolsBp.get());
		return symbol;
	}

	public Symbol getSymbol(String ticker) {
		return symbolsBp.getSymbol(ticker);
	}

	
	public void setCustomFilter(String custom) {
		throw new AssertionError();
	}
	
	public void refresh(){
		//analysisBp.recalculate(new FilterBp());
		refreshFilteredData(ViewControllers.getSharedFilter());
		refreshOther();
	}
	
	public void refreshOther(){
		SwingEvents.SYMBOLS_UPDATED.fire(symbolsBp.get());
		SwingEvents.LABELS_UPDATED.fire(labelsBp.get());
	}
	
	public void refreshFilteredData(FilterBp filterBp){
		analysisBp.recalculate(filterBp);
		SwingEvents.SYMBOL_ANALYSIS_UPDATED.fire(analysisBp.getSymbolAnalysis());
		SwingEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp.getCurrencyAnalysis());
		SwingEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(filterBp,true));
	}
	
	public Transaction addTransaction(Transaction t) {
		currencyController.setDefaultCurrency(((CurrencyEnum) t.getSymbol().getCurrency()));
		Symbol s = addSymbol(t.getSymbol());
		t.setSymbol(s);
		t = transactionsBp.add(t);
		analysisBp.recalculate(sharedFilter);
		
		SwingEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(ViewControllers.getSharedFilter(),true));
		SwingEvents.SYMBOL_ANALYSIS_UPDATED.fire(analysisBp.getSymbolAnalysis());
		SwingEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp.getCurrencyAnalysis());
		return t;
	}

	
	public void remove(Transaction t) {
		for (Object o : t.getLabelsValues().toArray()) {
			t.removeLabel((Label) o);
		}
		transactionsBp.remove(t);
		analysisBp.recalculate(sharedFilter);
		
		SwingEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(ViewControllers.getSharedFilter(),true));
		SwingEvents.SYMBOL_ANALYSIS_UPDATED.fire(analysisBp.getSymbolAnalysis());
		SwingEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp.getCurrencyAnalysis());
	}

	
	public void remove(Label label) {
		Collection<Transaction> transactions = transactionsBp.getTransactionsByLabel(label);
		for (Transaction t : transactions) {
			t.removeLabel(label);
		}
		labelsBp.remove(label);
		analysisBp.recalculate(sharedFilter);
		
		SwingEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(ViewControllers.getSharedFilter(),true));
		SwingEvents.SYMBOL_ANALYSIS_UPDATED.fire(analysisBp.getSymbolAnalysis());
		SwingEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp.getCurrencyAnalysis());
	}

	
	public void updateTransaction(Transaction t){
		transactionsBp.updateTransaction(t);
		analysisBp.recalculate(sharedFilter);
		
		SwingEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(ViewControllers.getSharedFilter(),true));
		SwingEvents.SYMBOL_ANALYSIS_UPDATED.fire(analysisBp.getSymbolAnalysis());
		SwingEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp.getCurrencyAnalysis());
	}

	
	public Label addLabel(Label label) {
		Label l = labelsBp.add(label);
		analysisBp.recalculate(sharedFilter);
		
		SwingEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(ViewControllers.getSharedFilter(),true));
		SwingEvents.SYMBOL_ANALYSIS_UPDATED.fire(analysisBp.getSymbolAnalysis());
		SwingEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp.getCurrencyAnalysis());
		SwingEvents.LABELS_UPDATED.fire(labelsBp.get());
		return l;
	}

	
	public Collection<Label> getLabels() {
		return labelsBp.get();
	}

	
	public void updatePrices() {
		symbolsBp.updatePrices();
		analysisBp.recalculate(sharedFilter);
		
		SwingEvents.SYMBOLS_CURRENT_PRICE_UPDATED.fire();
		SwingEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(ViewControllers.getSharedFilter(),true));
		SwingEvents.SYMBOL_ANALYSIS_UPDATED.fire(analysisBp.getSymbolAnalysis());
		SwingEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp.getCurrencyAnalysis());
	}

	
	public void updateHistoricalPrices() {
		symbolsBp.updateHistoricalPrices();
		analysisBp.recalculate(sharedFilter);
		
		SwingEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(ViewControllers.getSharedFilter(),true));
		SwingEvents.SYMBOL_ANALYSIS_UPDATED.fire(analysisBp.getSymbolAnalysis());
		SwingEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp.getCurrencyAnalysis());
		SwingEvents.SYMBOLS_HISTORICAL_PRICE_UPDATED.fire();
	}

	
	public void update(HistoricalPrice hPrice) {
		symbolsBp.update(hPrice);
		analysisBp.recalculate(sharedFilter);
		
		SwingEvents.TRANSACTION_UPDATED.fire(transactionsBp.getTransactions(ViewControllers.getSharedFilter(),true));
		SwingEvents.SYMBOL_ANALYSIS_UPDATED.fire(analysisBp.getSymbolAnalysis());
		SwingEvents.CURRENCY_ANALYSIS_UPDATED.fire(analysisBp.getCurrencyAnalysis());
	}

	
    public ByteArrayOutputStream exportTransactions(FilterBp filter) {
	    try {
	        return importExportBp.exportTransactions(transactionsBp.getTransactions(filter, true));
        } catch (IOException e) {
        }
        return null;
    }

	
    public ByteArrayOutputStream exportTransactions() {
		try {
	        return importExportBp.exportTransactions(transactionsBp.getTransactions());
        } catch (IOException e) {
        	int i=0;
        }
        return null;
    }

	
    public void importTransactions(File file) {
		try {
	        importExportBp.importTransactions(file);
	        //refresh();
        } catch (FileNotFoundException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
		
    }
public void cleanup(){
	System.exit(0);
	persistenceManager.close();
}

	
    public void setSelection(Transaction t) {
    	SwingEvents.TRANSACTION_SELECTION_CHANGED.fire(t);
    }

	
    public void setSelection(Symbol s) {
    	SwingEvents.SYMBOL_SELECTION_CHANGED.fire(s);
    }
}
