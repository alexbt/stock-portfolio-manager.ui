package com.proserus.stocks.ui.view.common;


import javax.swing.JPanel;

public class DialogImpl extends AbstractDialog{
	private static final long serialVersionUID = 201404041920L;

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
