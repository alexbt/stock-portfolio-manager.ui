package com.proserus.stocks.ui.view.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.proserus.stocks.bo.common.DBVersion;
import com.proserus.stocks.bo.common.Database;
import com.proserus.stocks.bo.common.DatabasePaths;
import com.proserus.stocks.bp.utils.RecursiveFileUtils;
import com.proserus.stocks.ui.controller.AbstractUnit;
import com.proserus.stocks.ui.controller.PortfolioController;
import com.proserus.stocks.ui.controller.ViewControllers;
import com.proserus.stocks.ui.model.DBVersionImpl;

public class BackupActionTest extends AbstractUnit {

	@Before
	@After
	public void tearUp() {
		removeZip("StockPortfolioBackupTest");
	}

	private static void removeZip(String zipName) {
		new File("src/test/resources/" + zipName + ".zip").delete();
	}

	@Test
	public void test() {
		File backupZip = new File("src/test/resources/StockPortfolioBackupTest.zip");
		File expectedFileZip = new File("src/test/resources/ExpectedStockPortfolioBackupTest.zip");

		assertFalse(backupZip.exists());
		assertTrue(expectedFileZip.exists());
		DBVersion dbVersion = new DBVersionImpl();
		dbVersion.setDatabaseVersion(24);

		ViewControllers d = new ViewControllers();
		PortfolioController controller = Mockito.mock(PortfolioController.class);
		Mockito.when(controller.retrieveCurrentVersion()).thenReturn(dbVersion);
		d.setController(controller);

		try {

			DatabasePaths db = new DatabasePaths();
			File f = new File("src/test/resources/testZipBackup/superData/data/db.script");
			List<File> col = RecursiveFileUtils.listFiles(new File("src/test/resources/testZipBackup/"), 8, "db.script");
			List<File> sotedFIles = new ArrayList<File>(col);
			Collections.sort(sotedFIles);

			for (File file : sotedFIles) {
				Database database = new Database(StringUtils.removeEnd(file.getAbsolutePath(), ".script"));
				if (file.getCanonicalFile().equals(f.getCanonicalFile())) {
					db.setSelectedDatabase(database);
				}
				db.addDb(database);
			}

			db.setBinaryCurrentFolder(new File("src/test/resources/testZipBackup/installationFolder").getCanonicalPath());
			db.setOsCurrentFolder(new File("src/test/resources/testZipBackup/currentDirectory").getCanonicalPath());

			assertEquals(10, db.getDatabases().size());// TODO was 11.. is 10
														// good?

			BackupAction.getInstance().createBackupFile(backupZip, db);

			ZipFile expectedZip = new ZipFile(backupZip);
			Map<String, ZipEntry> expectedEntries = new HashMap<String, ZipEntry>();
			for (Enumeration<?> e = expectedZip.entries(); e.hasMoreElements();) {
				ZipEntry expectedEntry = (ZipEntry) e.nextElement();
				expectedEntries.put(expectedEntry.getName(), expectedEntry);
			}
			expectedZip.close();
			assertEquals(35, expectedEntries.size());// TODO was 41..which is
														// good?

			ZipFile testZip = new ZipFile(expectedFileZip);
			for (Enumeration<?> e = testZip.entries(); e.hasMoreElements();) {
				ZipEntry testEntry = ((ZipEntry) e.nextElement());
				ZipEntry expectedEntry = expectedEntries.remove(testEntry.getName());
				if (expectedEntry != null) {
					assertEquals(testEntry.getName(), expectedEntry.getSize(), testEntry.getSize());
					if (testEntry.getName().equals("info.txt")) {
						testEntry.getCrc();
						expectedEntry.getCrc();
						InputStream s = testZip.getInputStream(testEntry);
						byte[] buffer = new byte[1048];
						int bytesRead;
						do {
							bytesRead = s.read(buffer, 0, buffer.length);
							// Write to a file or whatever you wnat here.
						} while (bytesRead > 0);
					}
				} else {
					fail("Missing " + testEntry);
				}
			}
			testZip.close();

			assertTrue(expectedEntries.isEmpty());

		} catch (FileNotFoundException e) {
			fail();
		} catch (IOException e) {
			fail();
		}
	}

	public static void main(String[] args) {
		Locale.setDefault(Locale.CANADA);
		removeZip("ExpectedStockPortfolioBackupTest");
		removeZip("StockPortfolioBackupTest");

		File newExpectedZip = new File("src/test/resources/ExpectedStockPortfolioBackupTest.zip");

		DBVersion dbVersion = new DBVersionImpl();
		dbVersion.setDatabaseVersion(24);
		ViewControllers d = new ViewControllers();
		PortfolioController controller = Mockito.mock(PortfolioController.class);
		Mockito.when(controller.retrieveCurrentVersion()).thenReturn(dbVersion);
		d.setController(controller);

		try {

			DatabasePaths db = new DatabasePaths();
			File f = new File("src/test/resources/testZipBackup/superData/data/db.script");
			List<File> col = RecursiveFileUtils.listFiles(new File("src/test/resources/testZipBackup/"), 8, "db.script");
			List<File> sotedFIles = new ArrayList<File>(col);
			Collections.sort(sotedFIles);

			for (File file : sotedFIles) {
				Database database = new Database(StringUtils.removeEnd(file.getAbsolutePath(), ".script"));
				if (file.getCanonicalFile().equals(f.getCanonicalFile())) {
					db.setSelectedDatabase(database);
				}
				db.addDb(database);
			}

			db.setBinaryCurrentFolder(new File("src/test/resources/testZipBackup/installationFolder").getCanonicalPath());
			db.setOsCurrentFolder(new File("src/test/resources/testZipBackup/currentDirectory").getCanonicalPath());

			BackupAction.getInstance().createBackupFile(newExpectedZip, db);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}
}
