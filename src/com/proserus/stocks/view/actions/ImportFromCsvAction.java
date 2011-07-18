package com.proserus.stocks.view.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.proserus.stocks.controllers.iface.PortfolioController;
import com.proserus.stocks.view.common.ViewControllers;
import com.proserus.stocks.view.general.Window;

public class ImportFromCsvAction extends AbstractAction  {
	private PortfolioController controller = ViewControllers.getController();
	private Window window = ViewControllers.getWindow();
	
	@Override
    public void actionPerformed(ActionEvent arg0) {
		//TODO move code to reusable location
		JFileChooser fc = new JFileChooser(new File("stock-portfolio_export.csv"));
		fc.addChoosableFileFilter(new CsvFileFilter());
		fc.setApproveButtonText("Import");
		fc.showOpenDialog(window);
		File file = fc.getSelectedFile();
		if(file!=null){
			controller.importTransactions(file);
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
