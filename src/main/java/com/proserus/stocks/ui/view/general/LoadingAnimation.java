package com.proserus.stocks.ui.view.general;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.proserus.stocks.ui.view.common.AbstractDialog;

public class LoadingAnimation extends AbstractDialog {

	private static final long serialVersionUID = 201407011000L;

	public LoadingAnimation() {
		super();
		setModal(true);
		setUndecorated(true);
		setSize(200, 200);
		add(loadingPanel());
	}

	private JPanel loadingPanel() {
		JPanel panel = new JPanel();
		BoxLayout layoutMgr = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
		panel.setLayout(layoutMgr);

		ClassLoader cldr = this.getClass().getClassLoader();
		java.net.URL imageURL = cldr.getResource("images/Loading.gif");
		ImageIcon imageIcon = new ImageIcon(imageURL);
		JLabel iconLabel = new JLabel();
		iconLabel.setIcon(imageIcon);
		imageIcon.setImageObserver(iconLabel);

		panel.add(iconLabel);
		return panel;
	}
}
