package com.proserus.stocks.view.common;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

abstract public class AbstractEditableTable extends AbstractTable implements MouseListener {
	private static final String REMOVE = "Remove";

	@Override
	protected abstract void delete();

	public AbstractEditableTable() {
		addMouseListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource() instanceof JMenuItem) {
			JMenuItem item = ((JMenuItem) e.getSource());

			if (item.getText().compareTo(REMOVE) == 0) {
				delete();
				return;
			}
		}
	}

	public void mouseClicked(MouseEvent evt) {
		if (evt.getButton() == MouseEvent.BUTTON3) {
			setRowSelectionInterval(rowAtPoint(evt.getPoint()), rowAtPoint(evt.getPoint()));
			JPopupMenu popupMenu = new JPopupMenu();
			JMenuItem item = new JMenuItem(REMOVE);
			popupMenu.add(item);
			item.addActionListener(this);
			popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
