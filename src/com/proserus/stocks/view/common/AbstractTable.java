package com.proserus.stocks.view.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.swing.DefaultRowSorter;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableColumn;

abstract public class AbstractTable extends JTable {

	/**
	 * Last date this class was modified
	 */
	private static final long serialVersionUID = 20101011L;

	HashMap<String, TableColumn> removedColumns = new HashMap<String, TableColumn>();

	private static Logger LOGGER = Logger.getLogger(AbstractTable.class.toString());

	public AbstractTable() {
	}

	public void showHideColumn(JMenuItem item) {
		if (!item.isSelected()) {
			for (int i = 0; i < getColumnModel().getColumnCount(); i++) {
				String name = getModel().getColumnName(getColumnModel().getColumn(i).getModelIndex());
				if (name.compareTo(item.getText()) == 0) {
					removedColumns.put(item.getText(), getColumnModel().getColumn(i));
					getColumnModel().removeColumn(getColumnModel().getColumn(i));
				}
			}
		} else {
			getColumnModel().addColumn(removedColumns.get(item.getText()));
			for (int i = 0; i < getModel().getColumnCount(); i++) {
				String name = getModel().getColumnName(i);
				if (name.compareTo(item.getText()) == 0) {
					try {
						getColumnModel().moveColumn(getColumnModel().getColumnCount() - 1, i);
					} catch (IllegalArgumentException e2) {

					}
					removedColumns.remove(item.getText());
					break;
				}
			}
		}
	}

	protected void setFirstRowSorted() {
		setFirstRowSorted(false);
	}

	protected void setFirstRowSorted(boolean asc) {
		if (getRowSorter() == null || getRowCount() < 1) {
			return;
		}

		RowSorter.SortKey sortKey;

		if (asc) {
			sortKey = new RowSorter.SortKey(0, SortOrder.ASCENDING);
		} else {
			sortKey = new RowSorter.SortKey(0, SortOrder.DESCENDING);
		}
		ArrayList alist = new ArrayList(1);
		alist.add(sortKey);
		((DefaultRowSorter) getRowSorter()).setSortable(0, true);
		((DefaultRowSorter) getRowSorter()).setSortKeys(alist);
		((DefaultRowSorter) getRowSorter()).sort();
	}
}
