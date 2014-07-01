package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proserus.stocks.bo.common.Database;
import com.proserus.stocks.bo.common.DatabasePaths;
import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventBus;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.ModelChangeEvents;
import com.proserus.stocks.bp.utils.RecursiveFileUtils;
import com.proserus.stocks.ui.controller.ViewControllers;
import com.proserus.stocks.ui.view.general.Version;

public class BackupAction extends AbstractAction implements EventListener {

	private static final String NEWLINE = "\r\n";

	private static final String EQUALS = " = ";

	private static final String SELECTED_DATABASE_VERSION = NEWLINE + "Selected database version" + EQUALS;

	private static final String APPLICATION_VERSION = "Application version" + EQUALS;

	private static final String STOCK_PORTFOLIO_BACKUP_ZIP = "stock-portfolio_backup.zip";

	private static final String SAVE_BACKUP = "Save backup";

	private static final String ZIP_EXTENSION = ".zip";

	private static final String DATA_FOLDER = System.getProperty("file.separator") + "data";

	private static final String TAB = "\t";

	private static final String FORWARD_SLASH = "/";

	private static final String BACKSLASH = "\\";

	private static final String EMPTY_STR = "";

	private static final String[] EXTENSIONS = new String[] { "properties", "log", "script", "csv" };

	private static final String EXT = ".txt";

	private static final String TIMESTAMP = "yyyy-MM-dd HH:mm:ss";

	private static final String LAST_MODIFIED_SIZE_FILE = "Last modified" + TAB + "Size" + TAB + "File";

	private static final String DB = NEWLINE + "DB";

	private static final String SELECTED_DATABASE = NEWLINE + "Selected database" + EQUALS;

	private static final String INSTALLATION_FOLDER = NEWLINE + "Installation folder" + EQUALS;

	private static final String CURRENT_FOLDER = NEWLINE + "Current Folder" + EQUALS;

	private static final String INFO_TXT = "info" + EXT;

	private static final String BINARY_CURRENT = "_installationFolder";

	private static final String UTF8 = "UTF-8";

	private static final long serialVersionUID = 201404031810L;

	private static Logger LOGGER = LoggerFactory.getLogger(BackupAction.class);

	private static final BackupAction singleton = new BackupAction();

	private DatabasePaths db;

	private BackupAction() {
		EventBus.getInstance().add(this, ModelChangeEvents.DATABASE_DETECTED);
		EventBus.getInstance().add(this, ModelChangeEvents.DATABASE_SELECTED);
	}

	public static BackupAction getInstance() {
		return singleton;
	}

	@Override
	public void actionPerformed(ActionEvent action) {

		JFileChooser fc = new JFileChooser(new File(STOCK_PORTFOLIO_BACKUP_ZIP));
		fc.addChoosableFileFilter(new ZipFileFilter());
		fc.setDialogTitle(SAVE_BACKUP);
		fc.showDialog(ViewControllers.getWindow(), SAVE_BACKUP);
		File backupZipFile = fc.getSelectedFile();

		if (backupZipFile != null) {
			if (!backupZipFile.getName().toLowerCase().endsWith(ZIP_EXTENSION)) {
				backupZipFile = new File(backupZipFile.getPath() + ZIP_EXTENSION);
			}
			try {
				createBackupFile(backupZipFile, db);

			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Failed to create backup archive " + backupZipFile.getName(),
						"Error creating backup archive", JOptionPane.ERROR_MESSAGE, null);
				LOGGER.error("Failed to create backup archive {}", new Object[] { backupZipFile.getName() }, e);
			}
		}
	}

	protected void createBackupFile(File backupZipFile, DatabasePaths db) throws FileNotFoundException, IOException {
		FileOutputStream dest = new FileOutputStream(backupZipFile);

		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

		List<Database> sortedList = new ArrayList<Database>(db.getDatabases());
		Collections.sort(sortedList);

		writeMeta(db.getCurrentFolder(), db.getInstallationFolder(), db.getSelectedDatabase(), sortedList, out);

		addFiles(out, db.getInstallationFolder(), BINARY_CURRENT, true);
		listFiles(out, db.getInstallationFolder(), BINARY_CURRENT);

		int i = 1;
		for (Database dat : sortedList) {
			String name = "db" + (i++) + "_" + dat.getParentFolder().getName();
			addFiles(out, dat.getParentFolder().getAbsolutePath(), name, false);
			listFiles(out, dat.getParentFolder().getAbsolutePath(), name);
		}

		out.close();
	}

	private void writeMeta(String osCurrentPath, String binCurrentPath, Database selectedDatabase, List<Database> sortedList,
			ZipOutputStream out) throws IOException {
		ZipEntry entry = new ZipEntry(INFO_TXT);
		out.putNextEntry(entry);
		out.write((APPLICATION_VERSION + Version.VERSION + Version.VERSION_SUFFIX + " - Build: " + Version.TIMESTAMP).getBytes(UTF8));
		out.write((INSTALLATION_FOLDER + binCurrentPath).getBytes(UTF8));
		out.write((CURRENT_FOLDER + osCurrentPath).getBytes(UTF8));
		out.write((SELECTED_DATABASE + selectedDatabase.getPath()).getBytes(UTF8));
		out.write((SELECTED_DATABASE_VERSION + ViewControllers.getController().retrieveCurrentVersion().getDatabaseVersion())
				.getBytes(UTF8));

		int i = 1;
		for (Database db : sortedList) {
			out.write((DB + (i++) + EQUALS + db.getPath()).getBytes(UTF8));
		}
		out.flush();
	}

	private void listFiles(ZipOutputStream out, String filename, String prefix) throws IOException {
		File file = new File(filename);
		if (!file.isDirectory()) {
			return;
		}
		Collection<File> allFiles = RecursiveFileUtils.listFiles(file, 10);
		SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP);

		ZipEntry entry = new ZipEntry(prefix + "/listing.txt");
		out.putNextEntry(entry);
		out.write(LAST_MODIFIED_SIZE_FILE.getBytes(UTF8));
		List<String> folders = new ArrayList<String>();
		for (File f : allFiles) {
			folders.add(NEWLINE + sdf.format(f.lastModified()) + TAB + f.getAbsolutePath() + TAB
					+ FileUtils.byteCountToDisplaySize(f.length()));
		}
		Collections.sort(folders);
		Collections.reverse(folders);

		for (String line : folders) {
			out.write(line.getBytes(UTF8));
			out.flush();
		}
	}

	private void addFiles(ZipOutputStream out, String rootFolder, String prefix, boolean recursive) throws IOException {
		File rootFolderFile = new File(rootFolder);
		if (!rootFolderFile.isDirectory()) {
			return;
		}

		List<File> filesForZip = RecursiveFileUtils.listFiles(rootFolderFile, 4, EXTENSIONS);

		for (File f : filesForZip) {
			String filename = f.getAbsolutePath().replace(rootFolderFile.getAbsolutePath(), EMPTY_STR);
			filename = StringUtils.removeStart(filename, FORWARD_SLASH);
			filename = StringUtils.removeStart(filename, BACKSLASH);
			ZipEntry entry = new ZipEntry(prefix + System.getProperty("file.separator") + filename);
			out.putNextEntry(entry);
			out.write(FileUtils.readFileToByteArray(f));
			out.flush();
		}

		if (!recursive && new File(rootFolder + DATA_FOLDER).exists()) {
			addFiles(out, rootFolder + DATA_FOLDER, prefix + DATA_FOLDER, recursive);
		}

	}

	@Override
	public void update(Event event, Object model) {
		if (ModelChangeEvents.DATABASE_SELECTED.equals(event) || ModelChangeEvents.DATABASE_DETECTED.equals(event)) {
			db = ModelChangeEvents.DATABASE_DETECTED.resolveModel(model);
		}
	}
}

class ZipFileFilter extends FileFilter {
	private static final String ZIP_BACKUP_FILE = "Zip backup file";
	private static final String ZIP_EXTENSION = ".zip";

	@Override
	public boolean accept(File pathname) {
		return pathname.isDirectory() || pathname.getPath().endsWith(ZIP_EXTENSION);
	}

	@Override
	public String getDescription() {
		return ZIP_BACKUP_FILE;
	}

}
