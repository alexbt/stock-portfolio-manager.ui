package com.proserus.stocks.ui.controller;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.proserus.stocks.bo.common.Database;
import com.proserus.stocks.bo.common.DatabasePaths;
import com.proserus.stocks.bp.dao.PersistenceManager;
import com.proserus.stocks.bp.events.ModelChangeEvents;
import com.proserus.stocks.bp.services.DatabaseVersionningBp;

public class ExportCsvTest extends AbstractUIUnit {

	@Test
	public void test() throws IOException {
		Injector inject = Guice.createInjector(new GuiceModuleMock());
		inject.getInstance(ViewControllers.class);

		DatabasePaths databases = new DatabasePaths();
		databases.setSelectedDatabase(new Database("src/test/resources/testUIdb/data/db"));
		ModelChangeEvents.DATABASE_SELECTED.fire(databases);
		inject.getInstance(DatabaseVersionningBp.class).setIgnorePopop(true);
		ViewControllers.getController().checkDatabaseVersion();

		String exportContent = ViewControllers.getController().exportTransactions();

		inject.getInstance(PersistenceManager.class).close();
		File expectedExportFile = new File("src/test/resources/expectedExport.csv");
		assertEquals(FileUtils.readFileToString(expectedExportFile), exportContent);
	}

	public static void main(String[] arg) throws IOException {

		Injector inject = Guice.createInjector(new GuiceModuleMock());
		inject.getInstance(ViewControllers.class);

		DatabasePaths databases = new DatabasePaths();
		databases.setSelectedDatabase(new Database("src/test/resources/testUIdb/data/db"));
		ModelChangeEvents.DATABASE_SELECTED.fire(databases);
		inject.getInstance(DatabaseVersionningBp.class).setIgnorePopop(true);
		ViewControllers.getController().checkDatabaseVersion();

		String exportContent = ViewControllers.getController().exportTransactions();
		File expectedExportFile = new File("src/test/resources/expectedExport.csv");
		expectedExportFile.delete();
		FileUtils.writeStringToFile(expectedExportFile, exportContent);
	}
}
