package com.proserus.stocks.ui.controller;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.proserus.stocks.bo.common.Database;
import com.proserus.stocks.bo.common.DatabasePaths;
import com.proserus.stocks.bo.symbols.CurrencyEnum;
import com.proserus.stocks.bo.symbols.SectorEnum;
import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bo.transactions.Transaction;
import com.proserus.stocks.bo.transactions.TransactionType;
import com.proserus.stocks.bp.dao.PersistenceManager;
import com.proserus.stocks.bp.events.SwingEvents;
import com.proserus.stocks.bp.services.DatabaseVersionningBp;
import com.proserus.stocks.ui.SwingModule;
import com.proserus.stocks.ui.model.SymbolImpl;
import com.proserus.stocks.ui.model.TransactionImpl;

public class AddTransactionTest {

    @Before
    @After
    public void clean() throws IOException {
        File dbFile = new File("src/test/resources/TestUIdb/data/db.script");
        dbFile.delete();
        FileUtils.copyFile(new File("src/test/resources/TestUIdb/data/untoucheddb.script"), dbFile, true);
        new File("src/test/resources/TestUIdb/data/db.properties").delete();
    }
    
    @Test
    public void test() {
        Injector inject = Guice.createInjector(new SwingModule());
        inject.getInstance(ViewControllers.class);

        DatabasePaths databases = new DatabasePaths();
        databases.setSelectedDatabase(new Database("src/test/resources/testUIdb/data/db"));
        SwingEvents.DATABASE_SELECTED.fire(databases);
        inject.getInstance(DatabaseVersionningBp.class).setIgnorePopop(true);
        ViewControllers.getController().checkDatabaseVersion();
        int i = 0;
        while (i < 10) {
            i++;
            Transaction t = new TransactionImpl();
            t.setCommission(new BigDecimal("9.99"));
            t.setPrice(new BigDecimal("9.99"));
            t.setQuantity(new BigDecimal("10"));
            t.setDate(Calendar.getInstance().getTime());
            Symbol s = new SymbolImpl();
            s.setCurrency(CurrencyEnum.CAD);
            s.setCustomPriceFirst(false);
            s.setName("TestSymbol");
            s.setPrice(new BigDecimal("45.32"), 2014);
            s.setSector(SectorEnum.FINANCE);
            s.setTicker("TS");

            t.setSymbol(s);
            t.setType(TransactionType.BUY);

            t = ViewControllers.getController().addTransaction(t);
            assertNotNull(t);
            assertNotNull(t.getId());
            assertNotNull(t.getSymbol().getId());
            ViewControllers.getController().remove(t.getSymbol());
            ViewControllers.getController().remove(t);
        }
        assertEquals(10, i);
        inject.getInstance(PersistenceManager.class).close();
    }
}
