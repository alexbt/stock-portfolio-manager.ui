package com.proserus.stocks.ui.view.common;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

import com.proserus.stocks.ui.controller.ViewControllers;

abstract public class AbstractDialog extends JDialog implements KeyListener {
	private static final long serialVersionUID = 201404031810L;
	private static final String LOGO_GIF = "images/Logo.gif";// TODO move

	public AbstractDialog() {
		super(ViewControllers.getWindow());
		setIconImage(new ImageIcon(getClass().getClassLoader().getResource(LOGO_GIF)).getImage());
		addKeyListener(this);
		setFocusable(true);
		centerOnScreen();
		// ShortcutUtils.apply(getRootPane());
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
		 * Dimension dim = Toolkit.getDefaultToolkit().getScreenSize(); int w =
		 * getSize().width; int h = getSize().height; int x = (dim.width - w) /
		 * 2; int y = (dim.height - h) / 2;
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
