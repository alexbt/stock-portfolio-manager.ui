package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.proserus.stocks.ui.controller.ViewControllers;
import com.proserus.stocks.ui.view.common.DialogImpl;
import com.proserus.stocks.ui.view.transactions.AddTransactionPanelImpl;

public class AddTransactionAction extends AbstractAction {
    public static int keyEvent = KeyEvent.VK_T;

    private static final long serialVersionUID = 201404031750L;
    private final static Logger LOGGER = Logger.getLogger(AddTransactionAction.class.getName());

    private static final AddTransactionAction singleton = new AddTransactionAction();

    private DialogImpl addTransactionWindow = new DialogImpl(new AddTransactionPanelImpl(), "Add a transaction");

    private AddTransactionAction() {

    }

    public static AddTransactionAction getInstance() {
        return singleton;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        try {
           // SwingUtilities.invokeLater(new Runnable() {
           //     public void run() {
                    addTransactionWindow.setVisibile(true);
           //     }
           // });

            ViewControllers.getController().refreshOther();
        } catch (Throwable e) {
            LOGGER.log(Level.FATAL, "Error displaying AddTransaction panel", e);
        }
    }

}
