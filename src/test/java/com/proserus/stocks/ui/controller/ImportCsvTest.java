package com.proserus.stocks.ui.controller;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.proserus.stocks.bo.common.Database;
import com.proserus.stocks.bo.common.DatabasePaths;
import com.proserus.stocks.bp.dao.PersistenceManager;
import com.proserus.stocks.bp.events.ModelChangeEvents;
import com.proserus.stocks.bp.services.DatabaseVersionningBp;
import com.proserus.stocks.bp.utils.DateUtils;

public class ImportCsvTest extends AbstractUIUnit {

	@Test
	public void test() throws IOException {
		File originalDbFile = new File("src/test/resources/TestUIdb/data/db.script");

		importFile(originalDbFile);
		File expectedFile = new File("src/test/resources/expectedDbImportCsv.script");

		List<String> expectedLines = FileUtils.readLines(expectedFile);
		List<String> nowLines = FileUtils.readLines(originalDbFile);
		int i = -1;
		for (String line : nowLines) {
			i++;
			if (line.startsWith("INSERT INTO HISTORICALPRICE VALUES(52,") && line.endsWith("," + DateUtils.getCurrentYear() + ")")) {
				continue;
			}
			assertEquals(expectedLines.get(i), line);
		}
		// assertEquals(FileUtils.readFileToString(expectedFile),
		// FileToString(originalDbFile));
	}

	private void importFile(File originalDbFile) throws IOException {
		Injector inject = Guice.createInjector(new GuiceModuleMock());
		inject.getInstance(ViewControllers.class);

		DatabasePaths databases = new DatabasePaths();
		databases.setSelectedDatabase(new Database("src/test/resources/testUIdb/data/db"));
		ModelChangeEvents.DATABASE_SELECTED.fire(databases);
		inject.getInstance(DatabaseVersionningBp.class).setIgnorePopop(true);
		ViewControllers.getController().checkDatabaseVersion();

		ViewControllers.getController().importTransactions(new File("src/test/resources/Portfolio.csv"));

		inject.getInstance(PersistenceManager.class).close();
	}

	public static void main(String[] arg) throws IOException {
		File originalDbFile = new File("src/test/resources/TestUIdb/data/db.script");

		new ImportCsvTest().importFile(originalDbFile);
		FileUtils.copyFile(new File("src/test/resources/TestUIdb/data/db.script"),
				new File("src/test/resources/expectedDbImportCsv.script"));
	}
}
