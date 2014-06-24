package com.proserus.stocks.ui.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.apache.commons.lang3.Validate;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.GenericGenerator;

import com.proserus.stocks.bo.transactions.Label;
import com.proserus.stocks.bo.transactions.Transaction;
import com.proserus.stocks.bo.utils.LoggerUtils;

@Entity(name = "Label")
@NamedQueries({ @NamedQuery(name = "label.findAll", query = "SELECT s FROM Label s ORDER BY name ASC"),
		@NamedQuery(name = "label.findByName", query = "SELECT s FROM Label s WHERE name = :label"),
		@NamedQuery(name = "label.findSubLabels", query = "SELECT s FROM Label s WHERE name in (:labels)") })
public class LabelImpl implements Comparable<Label>, Label {
	@Id
	@GeneratedValue(generator = "auto_increment")
	@GenericGenerator(name = "auto_increment", strategy = "increment")
	private Integer id;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.transactions.Label#getId()
	 */
	@Override
	public Integer getId() {
		return id;
	}

	public LabelImpl() {
		// for JPA
	}

	@Column(unique = true, nullable = false, name = "name")
	@Check(constraints = "LTRIM(name) <> ''")
	private String name;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "labels", targetEntity = TransactionImpl.class)
	private Collection<Transaction> transactions = new ArrayList<Transaction>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.transactions.Label#setTransactions(java.util.
	 * Collection)
	 */
	@Override
	public void setTransactions(Collection<Transaction> transactions) {
		this.transactions = transactions;
	}

	// TODO Maybe the same transaction is set...
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.proserus.stocks.bo.transactions.Label#addTransaction(com.proserus
	 * .stocks.bo.transactions.TransactionImpl)
	 */
	@Override
	public void addTransaction(Transaction t) {
		Validate.isTrue(!transactions.contains(t));
		transactions.add(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.proserus.stocks.bo.transactions.Label#removeTransaction(com.proserus
	 * .stocks.bo.transactions.Transaction)
	 */
	@Override
	public void removeTransaction(Transaction t) {
		transactions.remove(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.transactions.Label#getTransactions()
	 */
	@Override
	public Collection<Transaction> getTransactions() {
		return transactions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.transactions.Label#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		// Validate.notEmpty(name);
		this.name = name.trim();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.transactions.Label#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		assert LoggerUtils.validateCalledFromLogger() : LoggerUtils.callerException();
		return "LabelImpl [id=" + id + ", name=" + name + ", nbTransactions=" + (transactions != null ? transactions.size() : 0) + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.proserus.stocks.bo.transactions.Label#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Label label) {
		return this.getName().compareToIgnoreCase(label.getName());
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
