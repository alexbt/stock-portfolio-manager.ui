package com.proserus.stocks.ui.view.general;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proserus.stocks.bo.common.Database;
import com.proserus.stocks.bo.common.DatabasePaths;
import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventBus;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.ModelChangeEvents;
import com.proserus.stocks.ui.view.common.AbstractDialog;
import com.proserus.stocks.ui.view.common.SortedComboBoxModel;

public class DbChooser implements EventListener, ActionListener, ComponentListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(DbChooser.class.getName());

	private JComboBox comboBox;
	private SortedComboBoxModel comboModel;
	private DatabasePaths databases;

	private AbstractDialog dialog = new AbstractDialog() {
		private static final long serialVersionUID = 201404261332L;
	};

	public DbChooser() {
		init();
		EventBus.getInstance().add(this, ModelChangeEvents.DATABASE_DETECTED);
	}

	private void init() {
		JPanel panel = new JPanel();
		JLabel lblMoreThanOne = new JLabel("More than one database was found. Please choose");

		comboModel = new SortedComboBoxModel();
		comboBox = new JComboBox(comboModel);
		comboBox.setRenderer(new ComboBoxModelRenderer());
		comboBox.setEditor(new ComboBoxModelEditor());

		JLabel lblIfYouWish = new JLabel(
				"<html>If you wish to avoid of this message, <br/>please choose the <i>unwanted</i> database and '<i>File -> Delete current portfolio</i>'</html>'");

		JButton cancelButton = new JButton("Cancel (and close)");
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("ok");
		okButton.addActionListener(this);
		GroupLayout groupLayout = new GroupLayout(panel);
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGap(27)
										.addGroup(
												groupLayout
														.createParallelGroup(Alignment.LEADING)
														.addGroup(
																groupLayout.createSequentialGroup()
																		.addComponent(lblIfYouWish, 0, 0, Short.MAX_VALUE)
																		.addContainerGap())
														.addGroup(
																groupLayout.createSequentialGroup()
																		.addComponent(comboBox, 0, 427, Short.MAX_VALUE).addContainerGap())
														.addGroup(
																groupLayout.createSequentialGroup().addComponent(lblMoreThanOne)
																		.addContainerGap())))
						.addGroup(
								groupLayout.createSequentialGroup().addGap(89).addComponent(cancelButton).addGap(34).addComponent(okButton)
										.addContainerGap(190, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
				groupLayout.createSequentialGroup().addContainerGap().addComponent(lblMoreThanOne)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGap(18).addComponent(lblIfYouWish, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(cancelButton).addComponent(okButton))
						.addContainerGap(93, Short.MAX_VALUE)));
		panel.setLayout(groupLayout);
		dialog.add(panel);
		dialog.setModal(true);
		dialog.setTitle("More than one local database was found");
		dialog.setSize(675, 225);
		dialog.setMaximumSize(new Dimension(800, 225));
		dialog.setMinimumSize(new Dimension(675, 225));
		dialog.addComponentListener(this);
	}

	@Override
	public void update(Event event, Object model) {
		if (ModelChangeEvents.DATABASE_DETECTED.equals(event)) {
			databases = ModelChangeEvents.DATABASE_DETECTED.resolveModel(model);
			if (databases.getDatabases().size() == 1) {
				databases.setSelectedDatabase(databases.getDatabases().iterator().next());
				ModelChangeEvents.DATABASE_SELECTED.fire(databases);
				dialog.dispose();
			} else {
				comboModel.removeAllElements();
				for (Database db : databases.getDatabases()) {
					comboModel.addElement(db);
				}
				comboBox.setSelectedIndex(0);
				dialog.setVisible(true);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			dialog.dispose();
			databases.setSelectedDatabase((Database) comboBox.getSelectedItem());
			LOGGER.info("Selected database is: " + databases.getSelectedDatabase());
			ModelChangeEvents.DATABASE_SELECTED.fire(databases);
		} else if (e.getActionCommand().equals("cancel")) {
			LOGGER.info("User dit not choose a database, closing the application");
			System.exit(1);
		}
	}

	@Override
	public void componentResized(ComponentEvent component) {
		if (component.getSource() == dialog) {
			Dimension currSize = (Dimension) dialog.getSize().clone();
			if (currSize.height != dialog.getMaximumSize().getHeight()) {
				dialog.setSize((int) dialog.getSize().getWidth(), (int) dialog.getMaximumSize().getHeight());
			}
		}
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
	}
}
