package com.proserus.stocks.ui.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.proserus.stocks.bo.common.Database;
import com.proserus.stocks.bo.common.DatabasePaths;
import com.proserus.stocks.bo.symbols.CurrencyEnum;
import com.proserus.stocks.bo.symbols.SectorEnum;
import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bp.dao.PersistenceManager;
import com.proserus.stocks.bp.events.ModelChangeEvents;
import com.proserus.stocks.bp.services.DatabaseVersionningBp;
import com.proserus.stocks.ui.model.SymbolImpl;

public class AddSymbolTest extends AbstractUIUnit {

	@Test
	public void test() {
		Injector inject = Guice.createInjector(new GuiceModuleMock());
		inject.getInstance(ViewControllers.class);

		DatabasePaths databases = new DatabasePaths();
		databases.setSelectedDatabase(new Database("src/test/resources/testUIdb/data/db"));
		ModelChangeEvents.DATABASE_SELECTED.fire(databases);
		inject.getInstance(DatabaseVersionningBp.class).setIgnorePopop(true);
		ViewControllers.getController().checkDatabaseVersion();
		int i = 0;
		while (i < 2) {
			i++;
			Symbol s = new SymbolImpl();
			s.setCurrency(CurrencyEnum.CANADIAN);
			s.setCustomPriceFirst(false);
			s.setName("TestSymbol");
			s.setPrice(new BigDecimal("45.32"), 2014);
			s.setSector(SectorEnum.FINANCE);
			s.setTicker("TS2");
			s = ViewControllers.getController().addSymbol(s);
			assertNotNull(s);
			assertNotNull(s.getId());
			assertEquals(CurrencyEnum.CANADIAN, s.getCurrency());
			assertEquals(false, s.isCustomPriceFirst());
			assertEquals("TestSymbol", s.getName());
			assertEquals("45.32", s.getPrice(2014).getCustomPrice().setScale(2).toPlainString());
			assertEquals(SectorEnum.FINANCE, s.getSector());
			assertEquals("ts2", s.getTicker());
			assertEquals(s, ViewControllers.getController().getSymbol("TS2"));
			assertEquals(s, ViewControllers.getController().getSymbol("ts2"));
			ViewControllers.getController().remove(s);
			assertEquals(null, ViewControllers.getController().getSymbol("TS2"));
		}
		assertEquals(2, i);
		inject.getInstance(PersistenceManager.class).close();
	}
}
