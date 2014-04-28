package com.proserus.stocks.ui.view.general;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.proserus.stocks.bo.common.DatabasePaths;
import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventBus;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.SwingEvents;
import com.proserus.stocks.ui.view.common.AbstractDialog;

public class DbChooser  implements EventListener,
		ActionListener {
	private static final Logger LOGGER = Logger
			.getLogger(DbChooser.class.getName());
	
	private JComboBox comboBox;
	private DatabasePaths databases;
	
	private AbstractDialog dialog = new AbstractDialog() {
		private static final long serialVersionUID = 201404261332L;
	};

	public DbChooser() {
		init();

		EventBus.getInstance().add(this, SwingEvents.DATABASE_DETECTED);
	}

	private void init() {
		JPanel panel = new JPanel();
		JLabel lblMoreThanOne = new JLabel(
				"More than one database was found. Please choose");

		comboBox = new JComboBox();

		JLabel lblIfYouWish = new JLabel(
				"<html>If you wish to get rid of this message, <br/>please choose the unwanted database and choose '<i>File -> Delete current portfolio</i>'</html>'");

		JButton cancelButton = new JButton("Cancel (and close)");
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);
		
		JButton okButton = new JButton("OK");
		okButton.setActionCommand("ok");
		okButton.addActionListener(this);
		GroupLayout groupLayout = new GroupLayout(panel);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(27)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblIfYouWish, 0, 0, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(comboBox, 0, 427, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblMoreThanOne)
							.addContainerGap())))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(89)
					.addComponent(cancelButton)
					.addGap(34)
					.addComponent(okButton)
					.addContainerGap(190, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblMoreThanOne)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblIfYouWish, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(cancelButton)
						.addComponent(okButton))
					.addContainerGap(93, Short.MAX_VALUE))
		);
		panel.setLayout(groupLayout);
		dialog.add(panel);
		dialog.setModal(true);
		dialog.setTitle("More than one local database was found");
		dialog.setSize(675, 303);
	}

	@Override
	public void update(Event event, Object model) {
		if (SwingEvents.DATABASE_DETECTED.equals(event)) {
			databases = SwingEvents.DATABASE_DETECTED
					.resolveModel(model);
			if (databases.getDatabases().size() == 1) {
				databases.setSelectedDatabase(databases.getDatabases().iterator().next());
				SwingEvents.DATABASE_SELECTED.fire(databases);
				dialog.dispose();
			} else {
				comboBox.removeAllItems();
				for (String db : databases.getDatabases()) {
					if (db.length() > 100) {
						//db = StringUtils.right("..." + db, 100);
					}
					comboBox.addItem(db);
				}
				dialog.setVisible(true);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			dialog.dispose();
			databases.setSelectedDatabase(comboBox.getSelectedItem()
					.toString());
			LOGGER.log(Level.INFO, "Selected database is: " + databases.getSelectedDatabase());
			SwingEvents.DATABASE_SELECTED.fire(databases);
		}else if (e.getActionCommand().equals("cancel")) {
			LOGGER.log(Level.INFO, "User dit not choose a database, closing the application");
			System.exit(1);
		}
	}
}
