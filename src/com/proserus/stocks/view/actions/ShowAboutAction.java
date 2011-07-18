package com.proserus.stocks.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.proserus.stocks.view.common.ViewControllers;
import com.proserus.stocks.view.general.Version;
import com.proserus.stocks.view.general.Window;

public class ShowAboutAction extends AbstractAction  {
	private static String MESSAGE = "Stock Portfolio Manager was created by:\n" + "Alex Bélisle-Turcot\n" + "alex.belisleturcot@proserus.com\n\n"
    + "Version: " + Version.VERSION + "\n" + "Build: " + Version.TIMESTAMP + "\n\n" + "This product includes/uses:\n"
    + "   - Joda-Time: joda-time.sourceforge.net\n" + "   - Hibernate: hibernate.org\n" + "   - HSQLDB: hsqldb.org\n"
    + "   - log4j: logging.apache.org\n" + "   - ant: ant.apache.org"
    + "   - JFreeChart: jfree.org/jfreechart (included in the build but not used for now)\n"
    + "   - JFree: jfree.org/jcommon (included in the build but not used for now)\n" + "   - dom4j: dom4j.org\n"
    + "   - Apachage common logging: commons.apache.org/logging\n" + "   - Yahoo Finance API: finance.yahoo.com/d/quotes.csv\n"
    + "   - Yahoo iChart API: ichart.finance.yahoo.com/table.csv"
    + "\n\n(c) Copyright Groupe Proserus Inc. 2009 - 2011. All rights reserved.";
	
	private Window window = ViewControllers.getWindow();
	
	@Override
    public void actionPerformed(ActionEvent arg0) {
		JOptionPane.showMessageDialog(window, MESSAGE, "About Proserus Stocks Portfolio", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(
		        getClass().getClassLoader().getResource("images/LogoProserus.gif")));
    }

}
