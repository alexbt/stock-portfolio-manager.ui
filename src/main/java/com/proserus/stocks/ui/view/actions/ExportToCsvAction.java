package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.proserus.stocks.ui.controller.PortfolioController;
import com.proserus.stocks.ui.controller.ViewControllers;
import com.proserus.stocks.ui.view.general.Window;

public class ExportToCsvAction extends AbstractAction {
    public static int keyEvent = KeyEvent.VK_X;
    private static final String EXPORT_BUTTON = "Export";
    private static final String EXPORT_TITLE = "Export to CSV file";
    private static final String STOCK_PORTFOLIO_EXPORT_CSV = "stock-portfolio_export.csv";
    private static final String CSV_EXTENSION = ".csv";
    private static Logger LOGGER = Logger.getLogger(ExportToCsvAction.class);
    private static final long serialVersionUID = 201404031819L;
    private PortfolioController controller = ViewControllers.getController();
    private Window window = ViewControllers.getWindow();

    private static final ExportToCsvAction singleton = new ExportToCsvAction();

    private ExportToCsvAction() {

    }

    public static ExportToCsvAction getInstance() {
        return singleton;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        JFileChooser fc = new JFileChooser(new File(STOCK_PORTFOLIO_EXPORT_CSV));
        fc.addChoosableFileFilter(new CsvFileFilter());
        fc.setApproveButtonText(EXPORT_BUTTON);
        fc.setDialogTitle(EXPORT_TITLE);
        fc.showDialog(window, EXPORT_BUTTON);
        File file = fc.getSelectedFile();

        if (file != null) {
            if (!file.getName().toLowerCase().endsWith(CSV_EXTENSION)) {
                file = new File(file.getPath() + CSV_EXTENSION);
            }

            String baos = controller.exportTransactions();
            
            try {
                FileUtils.writeStringToFile(file, baos);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Failed to export CSV file " + file.getName(), "Error creating backup archive",
                        JOptionPane.ERROR_MESSAGE, null);
                LOGGER.log(Level.FATAL, "Unexpected error", e);
            }
        }
    }

}
