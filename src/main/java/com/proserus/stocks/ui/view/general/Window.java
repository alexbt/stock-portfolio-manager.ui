package com.proserus.stocks.ui.view.general;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventBus;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.SwingEvents;
import com.proserus.stocks.ui.controller.ViewControllers;
import com.proserus.stocks.ui.utils.ShortcutUtils;
import com.proserus.stocks.ui.view.actions.AddSymbolAction;
import com.proserus.stocks.ui.view.actions.AddTransactionAction;
import com.proserus.stocks.ui.view.actions.CloseApplicationAction;
import com.proserus.stocks.ui.view.actions.ExportToCsvAction;
import com.proserus.stocks.ui.view.actions.ImportFromCsvAction;
import com.proserus.stocks.ui.view.actions.RemoveSymbolAction;
import com.proserus.stocks.ui.view.actions.ShowAboutAction;
import com.proserus.stocks.ui.view.actions.ShowEditSymbolAction;
import com.proserus.stocks.ui.view.actions.ShowHideFiltersAction;
import com.proserus.stocks.ui.view.actions.ShowSettingsAction;
import com.proserus.stocks.ui.view.actions.UpdateOldPricesAction;
import com.proserus.stocks.ui.view.actions.UpdatePriceAction;

public class Window extends JFrame implements WindowListener, Observer,
		PropertyChangeListener, EventListener {
	
	private static final Logger LOGGER = Logger
			.getLogger(Window.class.getName());

	private static final long serialVersionUID = 201404031810L;

	private static final String LOGO_GIF = "images/Logo.gif";// TODO move

	private static final String PROSERUS_STOCKS_PORTFOLIO_0_1_BETA = "Stock Portfolio Manager "
			+ Version.VERSION + Version.VERSION_SUFFIX;

	JSplitPane split;

	private Toolbar toolbar;;

	// TODO Add icons
	// http://www.iconki.com/pack.asp?ico=136&Customicondesign-office-iconset
	public void start() {
		ColorSettingsDialog.updateUI();
		// UIManager.put(CONTROL_HIGHLIGHT, new Color(255, 148, 0));
		// UIManager.put(COMBO_BOX_SELECTION_BACKGROUND, new Color(255, 148,
		// 0));
		setLayout(new BorderLayout());

		ShortcutUtils.apply(getRootPane());        
        setJMenuBar(Menu.getInstance());
        
		JPanel north = new JPanel();
		north.setLayout(new BorderLayout());

		JPanel blah = new JPanel(new BorderLayout());
		blah.add(FilterPanelImpl.getInstance(), BorderLayout.NORTH);

		toolbar = new Toolbar();
		north.add(toolbar, BorderLayout.NORTH);
		north.add(blah, BorderLayout.CENTER);

		add(north, BorderLayout.NORTH);
		add(new SummaryStatusBar(), BorderLayout.SOUTH);
		FilterPanelImpl.getInstance().setVisible(false);
		add(new Tab(), BorderLayout.CENTER);
		setSize(1024, 768);
		setTitle(PROSERUS_STOCKS_PORTFOLIO_0_1_BETA);

		setVisible(true);
		addWindowListener(this);
		setIconImage(new ImageIcon(getClass().getClassLoader().getResource(
				LOGO_GIF)).getImage());
		setVisible(true);

		EventBus.getInstance().add(this, SwingEvents.DATABASE_SELECTED);

		Menu.getInstance().setEnabled(false);
		toolbar.setEnabled(false);

		// split.setDividerLocation(1);
	}

	public Window() {
		
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		CloseApplicationAction.getInstance().actionPerformed(null);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}

	@Override
	public void update(Observable arg0, Object UNUSED) {
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Integer newValue = (Integer) evt.getNewValue();
		Integer oldValue = (Integer) evt.getOldValue();
		if (newValue > oldValue && oldValue > 1) {
			JSplitPane split = (JSplitPane) evt.getSource();
			split.setDividerLocation(oldValue);
		}
	}

	@Override
	public void update(Event event, Object model) {
		if (SwingEvents.DATABASE_SELECTED.equals(event)) {
			setTitle(PROSERUS_STOCKS_PORTFOLIO_0_1_BETA + " - " + StringUtils.right
					(SwingEvents.DATABASE_SELECTED.resolveModel(model).getSelectedDatabase().getPath(),200));
			LOGGER.info("Selected database: " + SwingEvents.DATABASE_SELECTED.resolveModel(model).getSelectedDatabase());
			ViewControllers.getController().checkDatabaseVersion();
			ViewControllers.getController().refresh();
		}
	}
}
