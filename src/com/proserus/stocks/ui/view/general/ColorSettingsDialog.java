package com.proserus.stocks.ui.view.general;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.proserus.stocks.ui.view.common.AbstractDialog;
import com.proserus.stocks.ui.view.common.ViewControllers;

public class ColorSettingsDialog extends AbstractDialog implements ActionListener {
	private static final String TABBED_PANE_SELECTED = "TabbedPane.selected";

	private static final String COMBO_BOX_SELECTION_BACKGROUND = "ComboBox.selectionBackground";

	private static final String CONTROL_HIGHLIGHT = "controlHighlight";

	private static final String TEXT_FIELD_SELECTION_BACKGROUND = "TextField.selectionBackground";

	private static final String EMPTY_STR = "";

	private static final String CHOOSE_BACKGROUND_COLOR = "Choose Background Color";

	private static final String COMMA_STR = ",";

	private static final String UI_PROPERTIES = "./ui.properties";

	private static final String GUI_SETTINGS = "Color Settings";

	private static final String TEXTSELECTION = "textselection";

	private static final String TABLESELECTION = "tableselection";

	private static final String SELECTION = "selection";

	private static final String FILTERED = "filtered";

	private static final String DEFAULT = "default";

	private static final String TEXT_SELECTION_BACKGROUND_COLOR = "Highlighted Text: ";

	private static final String TABLE_SELECTION_BACKGROUND_COLOR = "Selected Rows: ";

	private static final String SELECTION_BACKGROUND_COLOR = "Selected Items: ";

	private static final String FILTERED_TABLE_COLOR = "Filtered Rows: ";

	private static Color defaultColor = new Color(204, 204, 204);
	private static Color filteredColor = new Color(150, 190, 255);
	private static Color selectionColor = new Color(204, 255, 204);
	private static Color tableSelectionColor = new Color(176, 196, 222);
	private static Color textSelectionColor = new Color(176, 196, 222);
	private static AbstractDialog dialog = new ColorSettingsDialog();
	private Properties ptrans = new Properties();
	private JLabel[] labels = new JLabel[] { new JLabel("Unfiltered Rows:      "), new JLabel(FILTERED_TABLE_COLOR),
	        new JLabel(TABLE_SELECTION_BACKGROUND_COLOR),
	        new JLabel(TEXT_SELECTION_BACKGROUND_COLOR),new JLabel(SELECTION_BACKGROUND_COLOR) };

	private JButton[] buttons = new JButton[] { new JButton(EMPTY_STR), new JButton(EMPTY_STR), new JButton(EMPTY_STR),
	        new JButton(EMPTY_STR), new JButton(EMPTY_STR) };

	private ColorSettingsDialog() {
		initProperties();
		JPanel panel = new JPanel();
		buttons[0].setName(DEFAULT);
		buttons[0].setBackground(defaultColor);
		buttons[1].setName(FILTERED);
		buttons[1].setBackground(filteredColor);
		buttons[2].setName(TABLESELECTION);
		buttons[2].setBackground(tableSelectionColor);
		buttons[3].setName(TEXTSELECTION);
		buttons[3].setBackground(textSelectionColor);
		buttons[4].setName(SELECTION);
		buttons[4].setBackground(selectionColor);
		GroupLayout group = new GroupLayout(panel);
		group.setAutoCreateGaps(true);
		group.setAutoCreateContainerGaps(true);
		panel.setLayout(group);
		GroupLayout.SequentialGroup hGroup = group.createSequentialGroup();
		ParallelGroup par = group.createParallelGroup();
		for (JLabel l : labels) {
			par.addComponent(l);
		}
		hGroup.addGroup(par);

		par = group.createParallelGroup();
		for (JButton b : buttons) {
			b.addActionListener(this);
			b.addKeyListener(this);
			par.addComponent(b);
		}
		hGroup.addGroup(par);
		group.setHorizontalGroup(hGroup);

		GroupLayout.SequentialGroup vGroup = group.createSequentialGroup();
		for (int i = 0; i < labels.length; i++) {
			par = group.createParallelGroup(Alignment.CENTER);
			par.addComponent(labels[i],GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE).addComponent(buttons[i],GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE);
			vGroup.addGroup(par);
		}

		group.setVerticalGroup(vGroup);

		setModal(true);
		add(panel);
		setTitle(GUI_SETTINGS);

		setResizable(false);
		pack();
	}

	/*
	 * public void centerOnScreen() { Dimension dim = Toolkit.getDefaultToolkit().getScreenSize(); // Determine the new location of the
	 * window int w = getSize().width; int h = getSize().height; int x = (dim.width - w) / 2; int y = (dim.height - h) / 2;
	 * 
	 * // Move the window setLocation(x, y); }
	 */

	public void initProperties() {
		try {
			ptrans.load(new FileInputStream(UI_PROPERTIES));
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		String[] colors;
		try {
			colors = ptrans.getProperty(DEFAULT).split(COMMA_STR);
			if (colors.length == 3) {

				defaultColor = new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer
				        .parseInt(colors[2]));

			}
		} catch (NumberFormatException e) {

		} catch (NullPointerException e) {

		}

		try {
			colors = ptrans.getProperty(FILTERED).split(COMMA_STR);
			if (colors.length == 3) {
				filteredColor = new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer
				        .parseInt(colors[2]));
			}
		} catch (NumberFormatException e) {

		} catch (NullPointerException e) {

		}

		try {
			colors = ptrans.getProperty(SELECTION).split(COMMA_STR);
			if (colors.length == 3) {
				selectionColor = new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer
				        .parseInt(colors[2]));
			}
		} catch (NumberFormatException e) {

		} catch (NullPointerException e) {
		}
		try {
			colors = ptrans.getProperty(TABLESELECTION).split(COMMA_STR);
			if (colors.length == 3) {
				tableSelectionColor = new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer
				        .parseInt(colors[2]));
			}
		} catch (NumberFormatException e) {

		} catch (NullPointerException e) {
		}
		try {
			colors = ptrans.getProperty(TEXTSELECTION).split(COMMA_STR);
			if (colors.length == 3) {
				textSelectionColor = new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer
				        .parseInt(colors[2]));
			}
		} catch (NumberFormatException e) {

		} catch (NullPointerException e) {
		}
	}

	static public JDialog getInstance() {
		return dialog;
	}

	static public Color getAlternateRowColor() {
		return new Color(245, 245, 245);
	}

	static public Color getTableSelectionColor() {
		return tableSelectionColor;
	}

	static public Color getSelectionColor() {
		return selectionColor;
	}

	static public Color getColor(boolean isFiltered) {
		if (isFiltered) {
			return filteredColor;
		}
		return defaultColor;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JComponent button = (JComponent) arg0.getSource();
		Color newColor = JColorChooser.showDialog(this, CHOOSE_BACKGROUND_COLOR, button.getBackground());
		if (newColor != null) {
			button.setBackground(newColor);
			if (button.getName().compareTo(SELECTION) == 0) {
				selectionColor = newColor;
			} else if (button.getName().compareTo(DEFAULT) == 0) {
				defaultColor = newColor;
			} else if (button.getName().compareTo(FILTERED) == 0) {
				filteredColor = newColor;
			} else if (button.getName().compareTo(TABLESELECTION) == 0) {
				tableSelectionColor = newColor;
			} else if (button.getName().compareTo(TEXTSELECTION) == 0) {
				textSelectionColor = newColor;
			}
			ptrans.setProperty(button.getName(), newColor.getRed() + COMMA_STR + newColor.getGreen() + COMMA_STR
			        + newColor.getBlue());
			try {
				ptrans.store(new FileOutputStream(UI_PROPERTIES), EMPTY_STR);
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}

			updateUI();
		}

	}

	static public void updateUI() {
	    UIManager.put(TEXT_FIELD_SELECTION_BACKGROUND, textSelectionColor);
	    UIManager.put(CONTROL_HIGHLIGHT, selectionColor);
	    UIManager.put(COMBO_BOX_SELECTION_BACKGROUND, selectionColor);
	    UIManager.put(TABBED_PANE_SELECTED, selectionColor);
	    SwingUtilities.updateComponentTreeUI(ViewControllers.getWindow());
    }
}
