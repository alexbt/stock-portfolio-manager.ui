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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.proserus.stocks.bo.common.DatabasePaths;
import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventBus;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.SwingEvents;
import com.proserus.stocks.ui.controller.ViewControllers;
import com.proserus.stocks.ui.view.general.Window;

public class BackupAction extends AbstractAction implements EventListener {
	private static final String STOCK_PORTFOLIO_BACKUP_ZIP = "stock-portfolio_backup.zip";

	private static final String SAVE_BACKUP = "Save backup";

	private static final String ZIP_EXTENSION = ".zip";

	private static final String DATA = "\\data";

	private static final String TAB = "\t";

	private static final String FORWARD_SLASH = "/";

	private static final String BACKSLASH = "\\";

	private static final String EMPTY_STR = "";

	private static final String[] EXTENSIONS = new String[] { "properties",
			"log", "script", "csv" };

	private static final String EXT = ".txt";

	private static final String TIMESTAMP = "yyyy-MM-dd HH:mm:ss";

	private static final String LAST_MODIFIED_SIZE_FILE = "Last modified" + TAB
			+ "Size" + TAB + "File";

	private static final String NEWLINE = "\r\n";

	private static final String EQUALS = " = ";

	private static final String DB = NEWLINE + "DB";

	private static final String SELECTED_DATABASE = NEWLINE
			+ "Selected database" + EQUALS;

	private static final String CLASSLOADER_CURRENT = NEWLINE
			+ "Classloader current" + EQUALS;

	private static final String OS_CURRENT = "OS Current" + EQUALS;

	private static final String INFO_TXT = "info" + EXT;

	private static final String DATA_DB = "\\data\\db";

	private static final String BINARY_CURRENT = "binaryCurrent";

	private static final String UTF8 = "UTF-8";

	private static final long serialVersionUID = 201404031810L;

	private static final BackupAction singleton = new BackupAction();

	private DatabasePaths db;

	private BackupAction() {
		EventBus.getInstance().add(this, SwingEvents.DATABASE_DETECTED);
		EventBus.getInstance().add(this, SwingEvents.DATABASE_SELECTED);
	}

	public static BackupAction getInstance() {
		return singleton;
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		try {
			JFileChooser fc = new JFileChooser(new File(
					STOCK_PORTFOLIO_BACKUP_ZIP));
			fc.addChoosableFileFilter(new ZipFileFilter());
			fc.setDialogTitle(SAVE_BACKUP);
			fc.showDialog(ViewControllers.getWindow(), SAVE_BACKUP);
			File backupZipFile = fc.getSelectedFile();

			if (backupZipFile != null) {
				if (!backupZipFile.getName().toLowerCase().endsWith(ZIP_EXTENSION)) {
					backupZipFile = new File(backupZipFile.getPath() + ZIP_EXTENSION);
				}

				FileOutputStream dest = new FileOutputStream(backupZipFile);

				ZipOutputStream out = new ZipOutputStream(
						new BufferedOutputStream(dest));

				writeMeta(db.getOsCurrentFolder(), db.getBinaryCurrentFolder(),
						db.getSelectedDatabase(), db.getDatabases(), out);

				addFiles(out, db.getBinaryCurrentFolder(), BINARY_CURRENT, true);
				listFiles(out, db.getBinaryCurrentFolder(), BINARY_CURRENT);

				int i = 0;
				for (String dbFolder : db.getDatabases()) {
					i++;
					dbFolder = StringUtils.removeEnd(dbFolder, DATA_DB);
					addFiles(out, dbFolder, "db" + i, false);
					listFiles(out, dbFolder, "db" + i);
				}

				out.close();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writeMeta(String osCurrentPath, String binCurrentPath,
			String selectedDatabase, Set<String> databases, ZipOutputStream out)
			throws IOException {
		ZipEntry entry = new ZipEntry(INFO_TXT);
		out.putNextEntry(entry);
		out.write((OS_CURRENT + osCurrentPath).getBytes(UTF8));
		out.write((CLASSLOADER_CURRENT + binCurrentPath).getBytes(UTF8));
		out.write((SELECTED_DATABASE + selectedDatabase).getBytes(UTF8));

		int i = 0;
		for (String db : databases) {
			i++;
			out.write((DB + i + EQUALS + db).getBytes(UTF8));
		}
		out.flush();
	}

	private void listFiles(ZipOutputStream out, String filename, String prefix)
			throws IOException {
		File file = new File(filename);
		Collection<File> allFiles = FileUtils.listFiles(file, null, true);
		SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP);

		ZipEntry entry = new ZipEntry(prefix + EXT);
		out.putNextEntry(entry);
		out.write(LAST_MODIFIED_SIZE_FILE.getBytes(UTF8));
		List<String> folders = new ArrayList<String>();
		for (File f : allFiles) {
			folders.add(NEWLINE + sdf.format(f.lastModified()) + TAB
					+ f.getAbsolutePath() + TAB
					+ FileUtils.byteCountToDisplaySize(f.length()));
		}
		Collections.sort(folders);
		Collections.reverse(folders);

		for (String line : folders) {
			out.write(line.getBytes(UTF8));
			out.flush();
		}
	}

	private void addFiles(ZipOutputStream out, String rootFolder,
			String prefix, boolean recursive) throws IOException {
		File rootFolderFile = new File(rootFolder);
		Collection<File> filesForZip = FileUtils.listFiles(rootFolderFile,
				EXTENSIONS, recursive);

		for (File f : filesForZip) {
			String filename = f.getAbsolutePath().replace(
					rootFolderFile.getAbsolutePath(), EMPTY_STR);
			filename = StringUtils.removeStart(filename, FORWARD_SLASH);
			filename = StringUtils.removeStart(filename, BACKSLASH);
			ZipEntry entry = new ZipEntry(prefix + BACKSLASH + filename);
			out.putNextEntry(entry);
			out.write(FileUtils.readFileToByteArray(f));
			out.flush();
		}

		if (!recursive && new File(rootFolder + DATA).exists()) {
			addFiles(out, rootFolder + DATA, prefix + DATA, recursive);
		}
	}

	@Override
	public void update(Event event, Object model) {
		if (SwingEvents.DATABASE_SELECTED.equals(event)
				|| SwingEvents.DATABASE_DETECTED.equals(event)) {
			db = SwingEvents.DATABASE_DETECTED.resolveModel(model);
		}
	}

}

class ZipFileFilter extends FileFilter {
	private static final String ZIP_BACKUP_FILE = "Zip backup file";
	private static final String ZIP_EXTENSION = ".zip";

	@Override
	public boolean accept(File pathname) {
		return pathname.isDirectory()
				|| pathname.getPath().endsWith(ZIP_EXTENSION);
	}

	@Override
	public String getDescription() {
		return ZIP_BACKUP_FILE;
	}

}
