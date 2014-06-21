package com.proserus.stocks.ui.controller;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.proserus.stocks.bo.common.Database;
import com.proserus.stocks.bo.common.DatabasePaths;
import com.proserus.stocks.bp.dao.PersistenceManager;
import com.proserus.stocks.bp.events.SwingEvents;
import com.proserus.stocks.bp.services.DatabaseVersionningBp;
import com.proserus.stocks.ui.SwingModule;

public class ImportCsvTest {

    @Before
    @After
    public void clean() throws IOException {
        File dbFile = new File("src/test/resources/TestUIdb/data/db.script");
        dbFile.delete();
        FileUtils.copyFile(new File("src/test/resources/TestUIdb/data/untoucheddb.script"), dbFile, true);
        new File("src/test/resources/TestUIdb/data/db.properties").delete();
    }

    @Test
    public void test() throws IOException {
        File originalDbFile = new File("src/test/resources/TestUIdb/data/db.script");

        importFile(originalDbFile);
        File expectedFile = new File("src/test/resources/expectedDbImportCsv.script");
        assertEquals(FileUtils.readFileToString(expectedFile), FileUtils.readFileToString(originalDbFile));
    }

    private void importFile(File originalDbFile) throws IOException {
        Injector inject = Guice.createInjector(new SwingModule());
        inject.getInstance(ViewControllers.class);

        DatabasePaths databases = new DatabasePaths();
        databases.setSelectedDatabase(new Database("src/test/resources/testUIdb/data/db"));
        SwingEvents.DATABASE_SELECTED.fire(databases);
        inject.getInstance(DatabaseVersionningBp.class).setIgnorePopop(true);
        ViewControllers.getController().checkDatabaseVersion();

        ViewControllers.getController().importTransactions(new File("src/test/resources/Portfolio.csv"));

        inject.getInstance(PersistenceManager.class).close();
    }

    public static void main(String[] arg) throws IOException {
        File originalDbFile = new File("src/test/resources/TestUIdb/data/db.script");

        new ImportCsvTest().importFile(originalDbFile);
        FileUtils.copyFile(new File("src/test/resources/TestUIdb/data/db.script"), new File("src/test/resources/expectedDbImportCsv.script"));
    }
}
