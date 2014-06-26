package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.proserus.stocks.ui.controller.PortfolioController;
import com.proserus.stocks.ui.controller.ViewControllers;
import com.proserus.stocks.ui.view.general.Window;

public class ImportFromCsvAction extends AbstractAction {
	private static final String DEFAULT_CSV_FILENAME = "stock-portfolio_export.csv";
	private static final String IMPORT_BUTTON = "Import";
	private static final String IMPORT_TITLE = "Import from CSV file";
	public static int keyEvent = KeyEvent.VK_I;
	private static final long serialVersionUID = 201404031810L;
	private PortfolioController controller = ViewControllers.getController();
	private Window window = ViewControllers.getWindow();

	private static final ImportFromCsvAction singleton = new ImportFromCsvAction();

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO move code to reusable location
		JFileChooser fc = new JFileChooser(new File(DEFAULT_CSV_FILENAME));
		fc.addChoosableFileFilter(new CsvFileFilter());
		fc.setApproveButtonText(IMPORT_BUTTON);
		fc.setDialogTitle(IMPORT_TITLE);

		while (true) {
			fc.showDialog(window, IMPORT_BUTTON);
			File file = fc.getSelectedFile();
			if (file == null) {
				break;
			}
			if (file.exists()) {
				controller.importTransactions(file);
				break;
			} else {
				JOptionPane.showMessageDialog(null, "The specified file does not exist", "Cannot find file", JOptionPane.WARNING_MESSAGE);
				fc.setSelectedFile(null);
			}
		}

		// JOptionPane.showConfirmDialog(null, "Import results", (transactionOk
		// - transactionWarning) +
		// " transactions were imported with sucess\n"
		// + transactionWarning + " transactions were imported with a warning\n"
		// + transactionError
		// +
		// " transactions could not be imported, check traces.log for details\n",
		// JOptionPane.DEFAULT_OPTION, icon);
	}

	public static ImportFromCsvAction getInstance() {
		return singleton;
	}

}

class CsvFileFilter extends FileFilter {
	private static final String COMMA_SEPARATED_VALUE_EXPORT_FILE = "Comma Separated Value export file";
	private static final String CSV_EXTENSION = ".csv";

	@Override
	public boolean accept(File pathname) {
		return pathname.isDirectory() || pathname.getPath().endsWith(CSV_EXTENSION);
	}

	@Override
	public String getDescription() {
		return COMMA_SEPARATED_VALUE_EXPORT_FILE;
	}

}
