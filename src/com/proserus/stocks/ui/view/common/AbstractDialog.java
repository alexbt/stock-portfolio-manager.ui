package com.proserus.stocks.ui.view.common;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Logger;

import javax.swing.JDialog;

import com.proserus.stocks.ui.controller.ViewControllers;

abstract public class AbstractDialog extends JDialog implements KeyListener {
	private static Logger LOGGER = Logger.getLogger(AbstractDialog.class.toString());

	public AbstractDialog() {
		super(ViewControllers.getWindow());
		setIconImage(ViewControllers.getWindow().getIconImage());
		addKeyListener(this);
		setFocusable(true);
		centerOnScreen();
	}

	public void keyReleased(KeyEvent arg0) {
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			dispose();
		}
	}

	protected void centerOnScreen() {
		setLocationRelativeTo(ViewControllers.getWindow());
		/*
		 * Dimension dim = Toolkit.getDefaultToolkit().getScreenSize(); int w = getSize().width; int h = getSize().height; int x =
		 * (dim.width - w) / 2; int y = (dim.height - h) / 2;
		 * 
		 * setLocation(x, y);
		 */
	}

	public void setVisibile(boolean flag) {
		setLocationRelativeTo(ViewControllers.getWindow());
		super.setVisible(flag);
	}

	public void keyTyped(KeyEvent e) {
	}
}
