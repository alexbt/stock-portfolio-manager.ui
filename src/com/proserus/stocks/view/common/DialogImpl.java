package com.proserus.stocks.view.common;

import java.util.logging.Logger;

import javax.swing.JPanel;

public class DialogImpl extends AbstractDialog{
	private static Logger LOGGER = Logger.getLogger(DialogImpl.class.toString());

	public DialogImpl(JPanel panel, String title) {
		super();
		setModal(true);
		setTitle(title);
		add(panel);
		setResizable(false);
		pack();
		panel.setVisible(true);
	}
}
