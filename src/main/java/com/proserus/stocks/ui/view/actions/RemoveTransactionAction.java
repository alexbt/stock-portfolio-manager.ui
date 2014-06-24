package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.proserus.stocks.bo.transactions.Transaction;
import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventBus;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.ModelChangeEvents;
import com.proserus.stocks.ui.controller.PortfolioController;
import com.proserus.stocks.ui.controller.ViewControllers;

public class RemoveTransactionAction extends AbstractAction implements EventListener {
	public static int keyEvent = KeyEvent.VK_R;
	private static final long serialVersionUID = 201404031810L;
	private PortfolioController controller = ViewControllers.getController();

	private Transaction selectedTransaction;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private static RemoveTransactionAction singleton = new RemoveTransactionAction();

	public static RemoveTransactionAction getInstance() {
		return singleton;
	}

	private RemoveTransactionAction() {
		EventBus.getInstance().add(this, ModelChangeEvents.TRANSACTION_SELECTION_CHANGED);
		setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		int n = JOptionPane.showConfirmDialog(null, "<html>Do you want to remove the selected transaction ('"
				+ selectedTransaction.getSymbol().getTicker() + "' - " + sdf.format(selectedTransaction.getCalendar().getTime()) + " - "
				+ selectedTransaction.getType().getTitle() + ") ?</html>", "Removing transaction", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (n == JOptionPane.YES_OPTION) {
			controller.remove(selectedTransaction);
			controller.setSelection((Transaction) null);
		}
	}

	@Override
	public void update(Event event, Object model) {
		if (ModelChangeEvents.TRANSACTION_SELECTION_CHANGED.equals(event)) {
			selectedTransaction = ModelChangeEvents.TRANSACTION_SELECTION_CHANGED.resolveModel(model);
			setEnabled(selectedTransaction != null);
		}
	}
}
