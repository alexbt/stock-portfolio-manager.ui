package com.proserus.stocks.ui.view.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.proserus.stocks.ui.controller.ViewControllers;
import com.proserus.stocks.ui.view.general.Version;
import com.proserus.stocks.ui.view.general.Window;

public class ShowAboutAction extends AbstractAction {
	public static int keyEvent = KeyEvent.VK_B;
	private static final long serialVersionUID = 201404031810L;
	private static String MESSAGE = "Stock Portfolio Manager was created by:\n" + "Alex Bélisle-Turcot\n"
			+ "alex.belisleturcot@proserus.com\n\n" + "Version: " + Version.VERSION + Version.VERSION_SUFFIX + "\n" + "Build: "
			+ Version.TIMESTAMP + "\n\n" + "This product includes/uses:\n" + "   - Joda-Time: joda-time.sourceforge.net\n"
			+ "   - Hibernate: hibernate.org\n" + "   - HSQLDB: hsqldb.org\n" + "   - log4j: logging.apache.org\n"
			+ "   - ant: ant.apache.org" + "   - JFreeChart: jfree.org/jfreechart\n"
			+ "   - JFree: jfree.org/jcommon (included in the build but not used for now)\n" + "   - dom4j: dom4j.org\n"
			+ "   - Apachage common logging: commons.apache.org/logging\n" + "   - Yahoo Finance API: finance.yahoo.com/d/quotes.csv\n"
			+ "   - Yahoo iChart API: ichart.finance.yahoo.com/table.csv"
			+ "\n\n(c) Copyright Groupe Proserus Inc. 2009 - 2011. All rights reserved.";

	private Window window = ViewControllers.getWindow();

	private static ShowAboutAction singleton = new ShowAboutAction();

	public static ShowAboutAction getInstance() {
		return singleton;
	}

	private ShowAboutAction() {

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JOptionPane.showMessageDialog(window, MESSAGE, "About Proserus Stocks Portfolio", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(
				getClass().getClassLoader().getResource("images/LogoProserus.gif")));
	}

}
