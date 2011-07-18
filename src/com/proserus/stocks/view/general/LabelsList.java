package com.proserus.stocks.view.general;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.InputVerifier;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import com.proserus.stocks.bp.FilterBp;
import com.proserus.stocks.bp.LabelsBp;
import com.proserus.stocks.controllers.iface.PortfolioController;
import com.proserus.stocks.model.transactions.Label;
import com.proserus.stocks.model.transactions.Transaction;
import com.proserus.stocks.view.common.ViewControllers;

public class LabelsList extends JPanel implements KeyListener, Observer, MouseListener {
	private static final String DO_YOU_WANT_TO_REMOVE_THE_TAG = "Do you want to remove the tag ";

	private static final String REMOVING_TAG = "Removing tag";

	private static final String EMPTY_STR = "";

	private static final String COMMA_STR = ",";

	private static final String TICKER_PATTERN = "[a-zA-Z0-9]";

	private PortfolioController transactionController = ViewControllers.getController();

	private JScrollPane js;
	private JList list = new JList();
	private JComponent startExternalComp = null;
	private JTextField addnew = new JTextField();
	private boolean IS_POPUP = false;
	private boolean IS_LINKED_TO_TRANSACTION = false;
	private boolean NEW_TRANSACTION = false;
	private boolean SET_CHANGED = false;
	private boolean filtering = false;
	private HashMap<String, Label> labels = new HashMap<String, Label>();
	private Transaction transaction = null;
	private Component parent;
	private boolean FILTER_MODE;
	private boolean UPDATE_TRANSACTION;

	private boolean removedEnabled = true;

	public boolean isRemovedEnabled() {
		return removedEnabled;
	}

	public void setRemovedEnabled(boolean removedEnabled) {
		this.removedEnabled = removedEnabled;
	}

	public LabelsList() {
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		IS_LINKED_TO_TRANSACTION = true;
		NEW_TRANSACTION = true;
		setList();
		addnew.addKeyListener(this);
		addnew.setInputVerifier(new DateVerifier());
		add(addnew, BorderLayout.NORTH);
		list.setVisibleRowCount(6);
		setSize(10, 15);
		setVisible(true);
	}

	public LabelsList(Transaction transaction, boolean popup, boolean linkedToTransaction, Component parent, boolean filtering) {
		this((JComponent) null, popup, linkedToTransaction, parent, filtering);
		// FILTER_MODE = true;
		UPDATE_TRANSACTION = true;
		this.transaction = transaction;
		IS_POPUP = true;
		IS_LINKED_TO_TRANSACTION = true;
		setSize(10, 15);
		setSelectedItems(transaction);
		// setVisible(true);
	}

	public LabelsList(JComponent startExternal, boolean popup, boolean linkedToTransaction, Component parent, boolean filtering) {
		if (filtering)
			FILTER_MODE = true;
		this.parent = parent;
		this.filtering = filtering;
		IS_POPUP = popup;
		IS_LINKED_TO_TRANSACTION = linkedToTransaction;

		startExternalComp = startExternal;

		transactionController.addLabelsObserver(this);
		if (!IS_POPUP) {
			setHorizontal();
		}
		setList();
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		// if (IS_POPUP && !filtering) {
		if (IS_LINKED_TO_TRANSACTION) {

			addnew.addKeyListener(this);
			addnew.setInputVerifier(new DateVerifier());
			add(addnew, BorderLayout.NORTH);
		}
		list.setVisibleRowCount(6);
		// }

		if (IS_POPUP && IS_LINKED_TO_TRANSACTION) {
			setVisible(true);
		}
	}

	public void setAddEnabled(boolean flag) {
		addnew.setVisible(flag);
	}

	public void setModeFilter(boolean flag) {
		FILTER_MODE = flag;
	}

	public void setListColor(Color bg) {
		list.setBackground(bg);
		setBackground(bg);
	}

	public void setHorizontal(boolean flag) {
		if (flag) {
			list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			list.setVisibleRowCount(-1);
			list.setFixedCellHeight(25);
		} else {
			list.setLayoutOrientation(JList.VERTICAL);
		}

	}

	public void addLetter(char car) {
		if (car == KeyEvent.VK_ENTER) {
			labels = getSelectedValues();
			Label newLabel = new Label();
			newLabel.setName(addnew.getText());
			newLabel = transactionController.addLabel(newLabel);
			labels.put(newLabel.getName(), newLabel);
			transactionController.addLabel(newLabel);
			addnew.setText(EMPTY_STR);
			updateTransaction();
			setVisible(false);

		} else {
			if ((EMPTY_STR + car).matches(TICKER_PATTERN)) {
				addnew.setText(addnew.getText() + car);
			}
		}
	}

	private void setHorizontal() {
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		list.setFixedCellHeight(25);
		setSize(1000, 20);
	}

	private void setList() {
		js = new JScrollPane(list);

		list.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent me) {
				Point p = new Point(me.getX(), me.getY());
				list.setSelectedIndex(list.locationToIndex(p));
				list.requestFocus();
			}
		});

		setLayout(new BorderLayout());
		add(js, BorderLayout.CENTER);
		list.setCellRenderer(new CheckListRenderer(filtering));
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.addMouseListener(this);
		list.addKeyListener(this);
		// list.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		// js.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	private void setClosedOnClick() {
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			@Override
			public void eventDispatched(AWTEvent awe) {
				MouseEvent me = (MouseEvent) awe;

				if (isVisible() && me.getClickCount() >= 1 && me.getSource() != startExternalComp && me.getSource() != addnew
				        && !(me.getSource() instanceof javax.swing.JDialog && ((javax.swing.JDialog) me.getSource()).getTitle().contains("tag"))) {

					if (explore((Component) me.getSource(), js)) {
						if (IS_LINKED_TO_TRANSACTION && SET_CHANGED) {
							// updateTransaction();
							SET_CHANGED = false;
						}
						setVisible(false);
					}
				}
			}

			private boolean explore(Component source, Container c) {
				if (source.equals(c)) {
					return false;
				}
				for (int i = 0; i < c.getComponentCount(); i++) {
					if (explore(source, (Container) c.getComponent(i)) == false) {
						return false;
					}
				}
				return true;
			}

		}, AWTEvent.MOUSE_EVENT_MASK);
	}

	public HashMap<String, Label> getSelectedValues() {
		HashMap<String, Label> a = new HashMap<String, Label>();
		for (int i = 0; i < list.getModel().getSize(); i++) {
			if (((CheckListItem) list.getModel().getElementAt(i)).isSelected()) {
				Label str = ((CheckListItem) list.getModel().getElementAt(i)).get();
				a.put(str.toString(), str);
			}
		}
		return a;
	}

	class DateVerifier extends InputVerifier {
		private static final String ONE_SPACE = " ";
		private static final String NO_SPACE_AND_COMMA_ALLOWED = "No space and comma allowed";

		@Override
		public boolean verify(JComponent input) {
			JTextField date = ((JTextField) input);
			if (!date.getText().contains(ONE_SPACE) && !date.getText().contains(COMMA_STR)) {
				date.setBackground(Color.white);
				date.setToolTipText(EMPTY_STR);
				return true;
			}
			date.setBackground(Color.red);
			date.setToolTipText(NO_SPACE_AND_COMMA_ALLOWED);
			return false;
		}
	}

	public void setSelectedItems(HashMap<String, String> items) {
		for (int i = 0; i < list.getModel().getSize(); i++) {
			CheckListItem check = (CheckListItem) list.getModel().getElementAt(i);
			check.setSelected(items.containsKey(check.toString()));
		}
	}

	public void updateTransaction() {
		try {
			Collection<Label> colLabel = new ArrayList<Label>();
			for (int i = 0; i < list.getModel().getSize(); i++) {
				CheckListItem check = (CheckListItem) list.getModel().getElementAt(i);
				if (check.isSelected()) {
					colLabel.add(check.get());
				}
			}
			transaction.setLabels(colLabel);
			transactionController.updateTransaction(transaction);
		} catch (NullPointerException e) {
		}
	}

	public void setSelectedItems(Transaction transaction) {
		for (Label label : transaction.getLabelsValues()) {
			labels.put(label.getName(), label);
		}
		for (int i = 0; i < list.getModel().getSize(); i++) {
			CheckListItem check = (CheckListItem) list.getModel().getElementAt(i);
			check.setSelected(labels.containsKey(check.toString()));
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getSource().equals(addnew) && arg0.getKeyCode() == KeyEvent.VK_ENTER && addnew.getInputVerifier().verify(addnew)
		        && addnew.getText().compareTo(EMPTY_STR) != 0) {
			labels = getSelectedValues();

			Label l = new Label();
			l.setName(addnew.getText());
			labels.put(l.toString(), l);
			l = transactionController.addLabel(l);

			if (UPDATE_TRANSACTION) {
				transaction.addLabel(l);
				transactionController.updateTransaction(transaction);
			}
			addnew.setText(EMPTY_STR);
		}
	}

	@Override
	public void setVisible(boolean flag) {
		super.setVisible(flag);
		if (!NEW_TRANSACTION) {
			AWTEventListener[] listeners = Toolkit.getDefaultToolkit().getAWTEventListeners();
			for (AWTEventListener listener : listeners) {
				Toolkit.getDefaultToolkit().removeAWTEventListener(listener);
			}

			setClosedOnClick();
			if (flag) {
				addnew.requestFocus();

			}
			if (parent != null) {
				parent.setVisible(flag);
			}
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		if (arg0.getSource().equals(list) && addnew != null) {
			addnew.requestFocus();
			String str = Character.toString(arg0.getKeyChar());

			if (str.matches(TICKER_PATTERN)) {
				addnew.setText(addnew.getText() + arg0.getKeyChar());
			}
		}
	}

	@Override
	public void update(Observable arg0, Object UNUSED) {
		if (arg0 instanceof LabelsBp) {
			Collection<Label> col = transactionController.getLabels();
			CheckListItem[] item = new CheckListItem[col.size()];
			int i = 0;

			for (Label label : col) {
				item[i] = new CheckListItem(label);
				if (!FILTER_MODE) {
					if (labels.containsKey(label.getName())) {
						item[i].setSelected(true);
					}
				}
				i++;
			}
			Arrays.sort(item);
			list.setListData(item);
		}
	}

	@Override
	public void mouseClicked(MouseEvent event) {

	}

	@Override
	public void mouseEntered(MouseEvent event) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent event) {
		JList list = (JList) event.getSource();

		int index = list.locationToIndex(event.getPoint());
		if (index == -1) {
			return;
		}
		CheckListItem item = (CheckListItem) list.getModel().getElementAt(index);

		if (item == null || event == null || item.getIcon() == null || item.getIcon().getLocation() == null) {
			return;
		}
		if ((item.getIcon().getLocation().getX() != 0) && item.getIcon().getLocation().getX() < event.getPoint().getX()) {

			// FIXME set the position where the cursor is currently.
			int n = JOptionPane.showConfirmDialog(this, DO_YOU_WANT_TO_REMOVE_THE_TAG + item.toString() + " ?", REMOVING_TAG,
			        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (n == JOptionPane.YES_OPTION) {
				/*
				 * if (summaryController.getFilterLabels().getLabels().contains(item.toString())) {
				 * summaryController.setFilterLabels(item.toString()); }
				 */
				transactionController.remove(item.get());
			}
		} else {

			item.setSelected(!item.isSelected());

			SET_CHANGED = true;

			/*
			 * if (filtering) { summaryController.setFilterLabels(item.toString()); }
			 */
			if (list.getCellBounds(index, index) != null) {
				list.repaint(list.getCellBounds(index, index));
			}
		}
		Label label = item.get();
		FilterBp filter = ViewControllers.getSharedFilter();
		if (item.isSelected()) {
			labels.put(item.get().toString(), label);
			if (FILTER_MODE) {
				filter.addLabel(label);
			}
		} else {
			labels.remove(label.getName());
			if (FILTER_MODE) {
				filter.removeLabel(label);
			}
		}

		if (UPDATE_TRANSACTION) {
			transaction.setLabels(labels.values());
			transactionController.updateTransaction(transaction);
		}
		// if(index!=0)
		//
	}

	@Override
	public void mouseReleased(MouseEvent event) {

	}
}

// Represents items in the list that can be selected

class CheckListItem implements Comparable {
	private Label label = null;
	private boolean isSelected = false;
	private ImageBackgroundPanel icon;

	public CheckListItem(Label label) {
		this.label = label;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
		if (isSelected) {
		}
	}

	@Override
	public String toString() {
		return label.toString();
	}

	public Label get() {
		return label;
	}

	public void setIcon(ImageBackgroundPanel icon) {
		this.icon = icon;
	}

	public ImageBackgroundPanel getIcon() {
		return icon;
	}

	@Override
	public int compareTo(Object arg0) {
		return toString().toLowerCase().compareTo(arg0.toString().toLowerCase());
	}
}

// Handles rendering cells in the list using a check box

class CheckListRenderer extends JCheckBox implements ListCellRenderer {
	private static final String IMAGES_CANCEL_GIF = "images/RemoveSmall.png";
	private boolean IS_POPUP;

	public CheckListRenderer(boolean IS_POPUP) {
		this.IS_POPUP = IS_POPUP;
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
		JPanel panel = new JPanel(new BorderLayout());
		setEnabled(list.isEnabled());
		setSelected(((CheckListItem) value).isSelected());
		setFont(list.getFont());

		panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		if (list.getSelectedIndex() == index) {
			BufferedImage img = null;
			try {
				img = ImageIO.read(getClass().getClassLoader().getResource(IMAGES_CANCEL_GIF));
			} catch (IOException e) {
			}
			ImageBackgroundPanel dd = new ImageBackgroundPanel(img);
			dd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			dd.setPreferredSize(new Dimension(20, 20));
			if (IS_POPUP) {
				setBackground(ColorSettingsDialog.getColor(true));
				dd.setBackground(ColorSettingsDialog.getColor(true));
			} else {
				setBackground(ColorSettingsDialog.getSelectionColor());
				dd.setBackground(ColorSettingsDialog.getSelectionColor());
			}

			((CheckListItem) value).setIcon(dd);

			if (list.getLayoutOrientation() != JList.HORIZONTAL_WRAP) {
				panel.add(dd, BorderLayout.EAST);
			}
		} else {
			setBackground(list.getBackground());
		}

		setText(value.toString());
		panel.add(this, BorderLayout.CENTER);

		return panel;
	}

}

class ImageBackgroundPanel extends JPanel {
	BufferedImage image;

	ImageBackgroundPanel(BufferedImage image) {
		this.image = image;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 3, this);
	}
}