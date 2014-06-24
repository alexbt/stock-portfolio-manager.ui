package com.proserus.stocks.ui.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.proserus.stocks.bo.common.Database;
import com.proserus.stocks.bo.common.DatabasePaths;
import com.proserus.stocks.bo.symbols.CurrencyEnum;
import com.proserus.stocks.bo.symbols.SectorEnum;
import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bo.transactions.Label;
import com.proserus.stocks.bo.transactions.Transaction;
import com.proserus.stocks.bo.transactions.TransactionType;
import com.proserus.stocks.bp.dao.PersistenceManager;
import com.proserus.stocks.bp.events.ModelChangeEvents;
import com.proserus.stocks.bp.services.DatabaseVersionningBp;
import com.proserus.stocks.bp.services.TransactionsBp;
import com.proserus.stocks.ui.model.LabelImpl;
import com.proserus.stocks.ui.model.SymbolImpl;
import com.proserus.stocks.ui.model.TransactionImpl;

public class AddTransactionTest extends AbstractUIUnit {

	@Test
	public void test() {
		Injector inject = Guice.createInjector(new GuiceModuleMock());
		inject.getInstance(ViewControllers.class);
		TransactionsBp transactionBp = inject.getInstance(TransactionsBp.class);
		DatabasePaths databases = new DatabasePaths();
		databases.setSelectedDatabase(new Database("src/test/resources/testUIdb/data/db"));
		ModelChangeEvents.DATABASE_SELECTED.fire(databases);
		inject.getInstance(DatabaseVersionningBp.class).setIgnorePopop(true);
		ViewControllers.getController().checkDatabaseVersion();
		int i = 0;
		while (i < 2) {
			i++;
			Transaction t = new TransactionImpl();
			t.setCommission(new BigDecimal("9.99"));
			t.setPrice(new BigDecimal("9.99"));
			t.setQuantity(new BigDecimal("10"));
			t.setCalendar(Calendar.getInstance());
			Symbol s = new SymbolImpl();
			s.setCurrency(CurrencyEnum.CANADIAN);
			s.setCustomPriceFirst(false);
			s.setName("TestSymbol");
			s.setPrice(new BigDecimal("45.32"), 2014);
			s.setSector(SectorEnum.FINANCE);
			s.setTicker("TS");

			t.setSymbol(s);
			t.setType(TransactionType.BUY);
			t = ViewControllers.getController().addTransaction(t);
			for (Transaction transaction : transactionBp.getTransactions()) {
				if (transaction.getId().equals(t.getId())) {
					t = transaction;
					break;
				}
			}

			assertNotNull(t);
			assertNotNull(t.getId());
			assertNotNull(t.getSymbol().getId());
			assertEquals(t.getSymbol(), ViewControllers.getController().getSymbol("ts"));
			assertEquals("9.99", t.getCommission().setScale(2).toPlainString());
			assertEquals("9.99", t.getPrice().setScale(2).toPlainString());
			assertEquals("10.00", t.getQuantity().setScale(2).toPlainString());
			assertEquals(CurrencyEnum.CANADIAN, t.getSymbol().getCurrency());
			assertEquals("TestSymbol", t.getSymbol().getName());
			assertEquals("45.32", t.getSymbol().getPrice(2014).getCustomPrice().setScale(2).toPlainString());
			assertEquals(false, t.getSymbol().isCustomPriceFirst());
			assertEquals(SectorEnum.FINANCE, t.getSymbol().getSector());
			assertEquals("ts", t.getSymbol().getTicker());
			s = ViewControllers.getController().getSymbol(t.getSymbol().getTicker());
			assertEquals(s, ViewControllers.getController().getSymbol("ts"));
			assertFalse(ViewControllers.getController().remove(s));
			assertEquals(s, ViewControllers.getController().getSymbol("ts"));
			ViewControllers.getController().remove(t);
			assertEquals(s, ViewControllers.getController().getSymbol("ts"));
			assertTrue(ViewControllers.getController().remove(s));
			assertEquals(null, ViewControllers.getController().getSymbol("ts"));
		}
		assertEquals(2, i);
		inject.getInstance(PersistenceManager.class).close();
	}

	// @Test
	@Ignore
	public void testLabelEmpty() {
		Injector inject = Guice.createInjector(new GuiceModuleMock());
		inject.getInstance(ViewControllers.class);
		TransactionsBp transactionBp = inject.getInstance(TransactionsBp.class);
		DatabasePaths databases = new DatabasePaths();
		databases.setSelectedDatabase(new Database("src/test/resources/testUIdb/data/db"));
		ModelChangeEvents.DATABASE_SELECTED.fire(databases);
		inject.getInstance(DatabaseVersionningBp.class).setIgnorePopop(true);
		ViewControllers.getController().checkDatabaseVersion();
		int i = 0;
		Transaction t = new TransactionImpl();
		t.setCommission(new BigDecimal("9.99"));
		t.setPrice(new BigDecimal("9.99"));
		t.setQuantity(new BigDecimal("10"));
		t.setCalendar(Calendar.getInstance());
		Label l = new LabelImpl();
		l.setName("");
		t.addLabel(l);
		Symbol s = new SymbolImpl();
		s.setCurrency(CurrencyEnum.CANADIAN);
		s.setCustomPriceFirst(false);
		s.setName("TestSymbol");
		s.setPrice(new BigDecimal("45.32"), 2014);
		s.setSector(SectorEnum.FINANCE);
		s.setTicker("TS");

		t.setSymbol(s);
		t.setType(TransactionType.BUY);
		t = ViewControllers.getController().addTransaction(t);
		for (Transaction transaction : transactionBp.getTransactions()) {
			if (transaction.getId().equals(t.getId())) {
				t = transaction;
				break;
			}
		}

		inject.getInstance(PersistenceManager.class).close();
	}
}
