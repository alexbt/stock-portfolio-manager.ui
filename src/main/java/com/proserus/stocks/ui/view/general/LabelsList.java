package com.proserus.stocks.ui.view.general;

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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

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

import com.proserus.stocks.bo.transactions.Label;
import com.proserus.stocks.bo.transactions.Transaction;
import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventBus;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.ModelChangeEvents;
import com.proserus.stocks.bp.model.Filter;
import com.proserus.stocks.ui.controller.PortfolioController;
import com.proserus.stocks.ui.controller.ViewControllers;

public class LabelsList extends JPanel implements KeyListener, EventListener, ActionListener,
		MouseListener, FocusListener, ItemListener {
	/**
     * 
     */
	private static final long serialVersionUID = 201108112014L;

	private Filter filter = ViewControllers.getFilter();

	private static final String DO_YOU_WANT_TO_REMOVE_THE_TAG = "Do you want to remove the tag ";

	private static final String REMOVING_TAG = "Removing tag";

	private static final String EMPTY_STR = "";

	private static final String COMMA_STR = ",";

	private PortfolioController transactionController = ViewControllers
			.getController();

	private JScrollPane js;
	private JList listOfLabels = new JList();
	private JTextField newLabelField = new JTextField();
	private HashMap<String, Label> labels = new HashMap<String, Label>();
	private Component parent;

	private Transaction transaction = null;
	private boolean isFilteringModeOn;
	private boolean removedEnabled = true;

	public LabelsList() {
		initEmbeded();
	}

	public LabelsList(Transaction transaction, Component parent) {
		initPopup(transaction, parent);
	}

	public boolean isRemovedEnabled() {
		return removedEnabled;
	}

	public void setRemovedEnabled(boolean removedEnabled) {
		this.removedEnabled = removedEnabled;
	}

	private void initEmbeded() {
		newLabelField.addFocusListener(this);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		initList();
		newLabelField.addKeyListener(this);
		newLabelField.setInputVerifier(new DateVerifier());
		add(newLabelField, BorderLayout.NORTH);
		listOfLabels.setVisibleRowCount(6);
		setSize(40, 100);
		setVisible(true);
	}

	private void initPopup(Transaction transaction, Component parent) {
		this.transaction = transaction;
		newLabelField.addFocusListener(this);
		this.parent = parent;

		initList();
		EventBus.getInstance().add(this, ModelChangeEvents.LABELS_UPDATED);
		ViewControllers.getController().refreshOther();
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		newLabelField.addKeyListener(this);
		newLabelField.setInputVerifier(new DateVerifier());
		add(newLabelField, BorderLayout.NORTH);
		listOfLabels.setVisibleRowCount(10);

		setVisible(true);
		setSize(40, 100);
		setSelectedItems(transaction);
	}

	public void setAddEnabled(boolean flag) {
		newLabelField.setVisible(flag);
	}

	public void setModeFilter(boolean flag) {
		isFilteringModeOn = flag;
	}

	public void setListColor(Color bg) {
		listOfLabels.setBackground(bg);
		setBackground(bg);
	}

	public void setHorizontal(boolean flag) {
		if (flag) {
			listOfLabels.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			listOfLabels.setVisibleRowCount(-1);
			listOfLabels.setFixedCellHeight(25);
		} else {
			listOfLabels.setLayoutOrientation(JList.VERTICAL);
		}

	}

	private void initList() {
		js = new JScrollPane(listOfLabels);

		listOfLabels.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent me) {
				Point p = new Point(me.getX(), me.getY());
				listOfLabels.setSelectedIndex(listOfLabels.locationToIndex(p));
				listOfLabels.requestFocus();
			}
		});

		setLayout(new BorderLayout());
		add(js, BorderLayout.CENTER);
		listOfLabels.setCellRenderer(new CheckListRenderer(isFilteringModeOn, this, this));
		listOfLabels
				.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listOfLabels.addMouseListener(this);
		listOfLabels.addKeyListener(this);
	}

	private void setupClosedOnClick() {
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			@Override
			public void eventDispatched(AWTEvent awe) {
				MouseEvent me = (MouseEvent) awe;

				if (isVisible()
						&& me.getClickCount() >= 1
						&& me.getSource() != newLabelField
						&& !(me.getSource() instanceof javax.swing.JDialog && ((javax.swing.JDialog) me.getSource())
								.getTitle().contains("tag"))) {

					if (explore((Component) me.getSource(), js)) {
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
		for (int i = 0; i < listOfLabels.getModel().getSize(); i++) {
			if (((CheckListItem) listOfLabels.getModel().getElementAt(i))
					.isSelected()) {
				Label str = ((CheckListItem) listOfLabels.getModel()
						.getElementAt(i)).get();
				a.put(str.getName(), str);
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
			if (!date.getText().contains(ONE_SPACE)
					&& !date.getText().contains(COMMA_STR)) {
				date.setBackground(Color.white);
				date.setToolTipText(EMPTY_STR);
				return true;
			}
			date.setBackground(Color.red);
			date.setToolTipText(NO_SPACE_AND_COMMA_ALLOWED);
			return false;
		}
	}

	public void setSelectedItems(Transaction transaction) {
		for (Label label : transaction.getLabelsValues()) {
			labels.put(label.getName(), label);
		}
		for (int i = 0; i < listOfLabels.getModel().getSize(); i++) {
			CheckListItem check = (CheckListItem) listOfLabels.getModel()
					.getElementAt(i);
			check.setSelected(labels.containsKey(check.get().getName()));
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getSource().equals(newLabelField)
				&& arg0.getKeyCode() == KeyEvent.VK_ENTER
				&& newLabelField.getInputVerifier().verify(newLabelField)
				&& newLabelField.getText().compareTo(EMPTY_STR) != 0) {
			addNewLabel();
		} else if (arg0.getSource().equals(listOfLabels)
				&& listOfLabels.getVisibleRowCount() > 0
				&& (arg0.getKeyChar() == KeyEvent.VK_SPACE || arg0.getKeyChar() == KeyEvent.VK_ENTER)) {
			updateLabelSelection();
			updateFilter(((CheckListItem) listOfLabels.getSelectedValue()));
			updateLabelsInTransaction(((CheckListItem) listOfLabels.getSelectedValue()));
		} else if (arg0.getSource().equals(listOfLabels)
				&& (arg0.getKeyChar() == KeyEvent.VK_DELETE)) {
			deleteLabel(((CheckListItem) listOfLabels.getSelectedValue()));
		}
	}

	private void deleteLabel(CheckListItem item) {
		int n = JOptionPane.showConfirmDialog(this,
				DO_YOU_WANT_TO_REMOVE_THE_TAG + item.get().getName() + " ?",
				REMOVING_TAG, JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (n == JOptionPane.YES_OPTION) {
			transactionController.remove(item.get());
			labels.remove(item.get().getName());
			item.setSelected(false);
			updateFilter(item);
		}
	}

	private void updateLabelSelection() {
		CheckListItem item = ((CheckListItem) listOfLabels.getSelectedValue());
		if (item != null) {
			item.setSelected(!item.isSelected());
			listOfLabels.repaint(listOfLabels.getCellBounds(
					listOfLabels.getSelectedIndex(),
					listOfLabels.getSelectedIndex()));
			if (item.isSelected()) {
				labels.put(item.get().getName(), item.get());
			} else {
				labels.remove(item.get().getName());
			}
		}
	}

	private void addNewLabel() {
		labels = getSelectedValues();
		Label l = ViewControllers.getBoBuilder().getLabel();
		l.setName(newLabelField.getText());
		labels.put(l.getName(), l);
		l = transactionController.addLabel(l);

		if (transaction != null) {
			if (!transaction.getLabelsValues().contains(l)) {
				transaction.addLabel(l);
				transactionController.updateTransaction(transaction);
			}
		}
		newLabelField.setText(EMPTY_STR);
	}

	@Override
	public void setVisible(boolean flag) {
		super.setVisible(flag);
		if (transaction != null) {
			AWTEventListener[] listeners = Toolkit.getDefaultToolkit()
					.getAWTEventListeners();
			for (AWTEventListener listener : listeners) {
				Toolkit.getDefaultToolkit().removeAWTEventListener(listener);
			}

			setupClosedOnClick();
			if (flag) {
				newLabelField.requestFocus();

			}
			if (parent != null) {
				parent.setVisible(flag);
			}
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	@Override
	public void update(Event event, Object model) {
		if (ModelChangeEvents.LABELS_UPDATED.equals(event)) {
			Collection<Label> col = ModelChangeEvents.LABELS_UPDATED
					.resolveModel(model);
			CheckListItem[] item = new CheckListItem[col.size()];
			int i = 0;

			for (Label label : col) {
				item[i] = new CheckListItem(label);
				if (labels.containsKey(label.getName())) {
					item[i].setSelected(true);
				}
				i++;
			}
			Arrays.sort(item);
			listOfLabels.setListData(item);
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
		CheckListItem item = (CheckListItem) list.getModel()
				.getElementAt(index);

		if (item == null || event == null || item.getIcon() == null
				|| item.getIcon().getLocation() == null) {
			return;
		}
		if ((item.getIcon().getLocation().getX() != 0)
				&& item.getIcon().getLocation().getX() < event.getPoint()
						.getX()) {
			deleteLabel(item);
		} else {

			item.setSelected(!item.isSelected());

			if (list.getCellBounds(index, index) != null) {
				list.repaint(list.getCellBounds(index, index));
			}
		}
		updateLabelsInTransaction(item);
	}

	private void updateLabelsInTransaction(CheckListItem item) {
		updateFilter(item);

		if (transaction != null) {
			transaction.setLabels(labels.values());
			transactionController.updateTransaction(transaction);
		}
	}

	private void updateFilter(CheckListItem item) {
		Label label = item.get();
		if (item.isSelected()) {
			labels.put(item.get().getName(), label);
			if (isFilteringModeOn) {
				filter.addLabel(label);
			}
		} else {
			labels.remove(label.getName());
			filter.removeLabel(label);
		}
		ViewControllers.getController().refreshFilteredData();
	}

	@Override
	public void mouseReleased(MouseEvent event) {
	}

	@Override
	public void focusGained(FocusEvent arg0) {
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		//FIXME is this used?
		if (arg0.paramString().startsWith("TRAVERSAL_FORWARD")) {
			listOfLabels.requestFocus();
			listOfLabels.setSelectedIndex(0);
			listOfLabels.repaint(listOfLabels.getCellBounds(0, 0));
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		((CheckListRenderer)e.getSource()).getCheckListItem();
		if(ItemEvent.ITEM_STATE_CHANGED == e.getStateChange()){
			e.getSource();
		}
				
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		arg0.getSource();
	}
}

class CheckListItem implements Comparable<CheckListItem> {
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
        return "CheckListItem [label=" + label + ", isSelected=" + isSelected + ", icon=" + icon + "]";
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
	public int compareTo(CheckListItem item) {
		return label.getName().toLowerCase()
				.compareTo(item.get().getName().toLowerCase());
	}
}

// Handles rendering cells in the list using a check box

class CheckListRenderer extends JCheckBox implements ListCellRenderer {
	/**
     * 
     */
	private static final long serialVersionUID = 201108112020L;

	private static final String IMAGES_CANCEL_GIF = "images/RemoveSmall.png";
	private boolean isFilteringModeOn;

	private ItemListener changeListener;
	private ActionListener actionListener;
	private CheckListItem checkListItem;

	public CheckListRenderer(boolean isFilteringModeOn, ItemListener l, ActionListener a) {
		this.isFilteringModeOn = isFilteringModeOn;
		this.changeListener = l;
		this.actionListener = a;
	}
	
	
	public CheckListItem getCheckListItem() {
		return checkListItem;
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean hasFocus) {
		JPanel panel = new JPanel(new BorderLayout());
		setEnabled(list.isEnabled());
		this.checkListItem = ((CheckListItem) value);
		setSelected(checkListItem.isSelected());
		setFont(list.getFont());

		panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		if (list.getSelectedIndex() == index) {
			BufferedImage img = null;
			try {
				img = ImageIO.read(getClass().getClassLoader().getResource(
						IMAGES_CANCEL_GIF));
			} catch (IOException e) {
			}
			ImageBackgroundPanel imagePanel = new ImageBackgroundPanel(img);
			imagePanel
					.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			imagePanel.setPreferredSize(new Dimension(20, 20));
			if (isFilteringModeOn) {
				setBackground(ColorSettingsDialog.getColor(true));
				imagePanel.setBackground(ColorSettingsDialog.getColor(true));
			} else {
				setBackground(ColorSettingsDialog.getSelectionColor());
				imagePanel.setBackground(ColorSettingsDialog
						.getSelectionColor());
			}

			checkListItem.setIcon(imagePanel);

			if (list.getLayoutOrientation() != JList.HORIZONTAL_WRAP) {
				panel.add(imagePanel, BorderLayout.EAST);
			}
		} else {
			setBackground(list.getBackground());
		}

		setText(checkListItem.get().getName());
		panel.add(this, BorderLayout.CENTER);
		
		addItemListener(changeListener);
		addActionListener(actionListener);
		return panel;
	}

}

class ImageBackgroundPanel extends JPanel {
	/**
     * 
     */
	private static final long serialVersionUID = 201108112020L;

	private BufferedImage image;

	ImageBackgroundPanel(BufferedImage image) {
		this.image = image;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 3, this);
	}
}

