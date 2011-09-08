package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.proserus.stocks.ui.controller.PortfolioController;
import com.proserus.stocks.ui.view.common.ViewControllers;
import com.proserus.stocks.ui.view.general.Window;

public class ImportFromCsvAction extends AbstractAction  {
	private PortfolioController controller = ViewControllers.getController();
	private Window window = ViewControllers.getWindow();
	
	@Override
    public void actionPerformed(ActionEvent arg0) {
		//TODO move code to reusable location
		JFileChooser fc = new JFileChooser(new File("stock-portfolio_export.csv"));
		fc.addChoosableFileFilter(new CsvFileFilter());
		fc.setApproveButtonText("Import");
		fc.showDialog(window, "Import");
		File file = fc.getSelectedFile();
		if(file!=null && file.exists()){
				controller.importTransactions(file);
			
//			JOptionPane.showConfirmDialog(null, "Import results", (transactionOk - transactionWarning) + " transactions were imported with sucess\n"
//			        + transactionWarning + " transactions were imported with a warning\n" + transactionError
//			        + " transactions could not be imported, check traces.log for details\n", JOptionPane.DEFAULT_OPTION, icon);
		}else{
			JOptionPane.showMessageDialog(null, "The specified file does not exist", "Cannot find file", JOptionPane.WARNING_MESSAGE);
		}
    }

}
class CsvFileFilter extends FileFilter{
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
