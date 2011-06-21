package com.proserus.stocks.view.general;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.data.time.Year;

import com.proserus.stocks.bp.DateUtil;
import com.proserus.stocks.bp.FilterBp;
import com.proserus.stocks.bp.SharedFilter;
import com.proserus.stocks.bp.SymbolsBp;
import com.proserus.stocks.bp.TransactionsBp;
import com.proserus.stocks.controllers.PortfolioControllerImpl;
import com.proserus.stocks.controllers.iface.PortfolioController;
import com.proserus.stocks.model.symbols.Symbol;
import com.proserus.stocks.view.common.EmptyYear;
import com.proserus.stocks.view.common.SortedComboBoxModel;
import com.proserus.stocks.view.symbols.EmptySymbol;

public class FilterPanel extends JPanel implements ActionListener, Observer, KeyListener {
	private static final String EMPTY_STR = "";


	private static final String TAGS = "  Tags:  ";

	private static final String YEAR = "Year:";

	private static final String IMAGES_TAGS_GIF = "images/tags.gif";

	private PortfolioController controller = PortfolioControllerImpl.getInstance();

	// private JTextField txt = new JTextField();
	private SortedComboBoxModel modelYears = new SortedComboBoxModel(new FilterYearComparator());
	private SortedComboBoxModel modelSymbols = new SortedComboBoxModel();
	private JComboBox years = new JComboBox(modelYears);
	private JButton button;
	private LabelsList labelsList = null;
	private JTextField filterLabels = new JTextField(EMPTY_STR);
	private Year firstYear = PortfolioControllerImpl.getInstance().getFirstYear();

	public FilterPanel() {
		URL url = getClass().getClassLoader().getResource(IMAGES_TAGS_GIF);
		if (url != null) {
			button = new JButton(new ImageIcon(url));
		}else{
			button = new JButton("Tags");
		}
		controller.addTransactionsObserver(this);
		controller.addFilterObserver(this);
		controller.addTransactionObserver(this);
		// symbols = new LabelsList(false, false, jWindow, true);
		filterLabels.setBorder(null);
		setLayout(new GridBagLayout());
		filterLabels.setEditable(false);
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.weightx = 3;
		gridBagConstraints.gridwidth = 5;
		gridBagConstraints.gridy=1;
		gridBagConstraints.fill=GridBagConstraints.HORIZONTAL;
		labelsList = new LabelsList(button, true, false, this, true);
		labelsList.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
//		button.setPreferredSize(new Dimension(20, 20));
		
		add(labelsList, gridBagConstraints);
		
		gridBagConstraints.gridy=2;
		gridBagConstraints.gridx=1;
		gridBagConstraints.weighty=2;
		setSize(10, 10);
		gridBagConstraints.weightx = 0.05;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.anchor = GridBagConstraints.SOUTHWEST;
		gridBagConstraints.fill = GridBagConstraints.NONE;

//		add(new JLabel("<html>F<br/>I<br/>L<br/>T<br/>E<br/>R<br/>S<br/><br/></html>"), gridBagConstraints);
		
		gridBagConstraints.weightx = 2;
		gridBagConstraints.gridwidth = 2;
		add(new JLabel(""), gridBagConstraints);
		
		gridBagConstraints.weightx = 0.05;
		gridBagConstraints.gridwidth = 1;
		add(new JLabel(YEAR), gridBagConstraints);
		years.addActionListener(this);
		years.setActionCommand("changeYear");
		add(years, gridBagConstraints);

		add(new JLabel("Symbol:"), gridBagConstraints);

		populateSymbols();
		gridBagConstraints.weightx = 0.05;

		button.addActionListener(this);
		//symbols = new LabelsList(button, true, false, null, true);
//		symbols.setVisible(true);
		//symbols.setSize(150, 117);
		
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		
		JComboBox dropDown = new JComboBox();
		dropDown.setModel(modelSymbols);
		controller.addSymbolsObserver(this);
		dropDown.addActionListener(this);
		add(dropDown, gridBagConstraints);
		
		
//		jWindow.add(symbols);
//		jWindow.setSize(150, 117);

		
		//button.addActionListener(this);

		
		// gridBagConstraints.gridy = 2;
		//customFilter.addKeyListener(this);

		// gridBagConstraints.anchor = GridBagConstraints.EAST;

//		gridBagConstraints.weightx = 0;
//		add(new JLabel(""), gridBagConstraints);
//		add(new JLabel(""), gridBagConstraints);
//		// gridBagConstraints.anchor = GridBagConstraints.SOUTHEAST;
//		gridBagConstraints.weightx = 1;
//		add(filterLabels, gridBagConstraints);
//		gridBagConstraints.weightx = 0;
		populateYears(firstYear);
		
		setVisible(true);
	}

	private void populateYears(Year min) {
		years.removeActionListener(this);
		Object o = modelYears.getSelectedItem();
		modelYears.removeAllElements();
		for (Year i = DateUtil.getCurrentYear(); i.getYear() >= min.getYear(); i = (Year)i.previous()) {
			modelYears.addElement(i);
		}
		modelYears.addElement((new EmptyYear()));
		modelYears.setSelectedItem(o);
		years.addActionListener(this);
	}

	private void populateSymbols() {
		Iterator<Symbol> iter = controller.getSymbols().iterator();
		modelSymbols.removeAllElements();
		while (iter.hasNext()) {
			modelSymbols.addElement(iter.next());
		}
		Symbol s = new EmptySymbol();
		modelSymbols.addElement(s);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource().equals(years) && arg0.getActionCommand().compareTo("changeYear") == 0) {
			Year selectedYear = (Year) ((JComboBox) arg0.getSource()).getSelectedItem();
			if (!selectedYear.toString().isEmpty()) {
				SharedFilter.getInstance().setYear(selectedYear);
			}else{
				SharedFilter.getInstance().setYear(null);
			}
		} else if (arg0.getSource().equals(button)) {
			throw new AssertionError();
		}if (arg0.getSource() instanceof JComboBox) {
			Object o = ((JComboBox) arg0.getSource()).getSelectedItem();
			if (o instanceof Symbol) {
				FilterBp filter = SharedFilter.getInstance();
				if (((Symbol) o).getId() != null) {
					filter.setSymbol((Symbol) o);
				}

				else {
					filter.setSymbol(null);
				}
			}
		}
	}

	@Override
	public void update(Observable arg0, Object UNUSED) {
		if (arg0 instanceof SharedFilter) {
			filterLabels.setText(SharedFilter.getInstance().toString());
		} else if (arg0 instanceof TransactionsBp) {
			if (!firstYear.equals(controller.getFirstYear())) {
				populateYears(controller.getFirstYear());
			}
		}else if(arg0 instanceof SymbolsBp){
			Object o = modelSymbols.getSelectedItem();
			modelSymbols.removeAllElements();
			for (Symbol symbol : controller.getSymbols()) {
				modelSymbols.addElement(symbol);
			}
			Symbol s = new EmptySymbol();
			modelSymbols.addElement(s);
			modelSymbols.setSelectedItem(o);
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent textField) {
		// TODO BEFORE: int length = ((JTextField)
		// textField.getSource()).getText().length();
		// NOW BETTER ?
		if (!((JTextField) textField.getSource()).getText().isEmpty()) {
			controller.setCustomFilter((((JTextField) textField.getSource()).getText()));
		} else {
			controller.setCustomFilter(EMPTY_STR);
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
