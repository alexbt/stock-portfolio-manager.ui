package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.apache.log4j.Priority;

import com.proserus.stocks.bo.transactions.Transaction;
import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventBus;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.SwingEvents;
import com.proserus.stocks.ui.controller.PortfolioController;
import com.proserus.stocks.ui.controller.ViewControllers;

//FIXME remove EventListener
public class RemoveTransactionAction extends AbstractAction implements
		EventListener {
	private static final long serialVersionUID = 201404031810L;
	private PortfolioController controller = ViewControllers.getController();

	private Transaction selectedTransaction;

	public RemoveTransactionAction() {
		EventBus.getInstance().add(this,
				SwingEvents.TRANSACTION_SELECTION_CHANGED);
		setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		int n = JOptionPane.showConfirmDialog(null,
				"Do you want to remove the selected transaction ?",
				"Removing transaction", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (n == JOptionPane.YES_OPTION) {
			controller.remove(selectedTransaction);
			controller.setSelection((Transaction) null);
		}
	}

	@Override
	public void update(Event event, Object model) {
		if (SwingEvents.TRANSACTION_SELECTION_CHANGED.equals(event)) {
			selectedTransaction = SwingEvents.TRANSACTION_SELECTION_CHANGED
					.resolveModel(model);
			setEnabled(selectedTransaction != null);
		}
	}
}
