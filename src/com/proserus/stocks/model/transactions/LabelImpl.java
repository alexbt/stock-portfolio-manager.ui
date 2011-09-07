package com.proserus.stocks.model.transactions;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity(name="Label")
@NamedQueries( { @NamedQuery(name = "label.findAll", query = "SELECT s FROM Label s"),
        @NamedQuery(name = "label.findByName", query = "SELECT s FROM Label s WHERE label = :label"),
        @NamedQuery(name = "label.findSubLabels", query = "SELECT s FROM Label s WHERE label in (:labels)") })
public class LabelImpl implements Comparable, Label {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;

	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Label#getId()
     */
	@Override
    public Integer getId() {
		return id;
	}

	public LabelImpl() {
		// for JPA
	}

	//TODO next time we update the table, change "label" column name for "name"
	@Column(unique = true, nullable = false, name="label")
	//TODO add constraint to forbid empty string (or just blank space): LTRIM(LABEL) != ''
	//@Check(constraints = "LTRIM(LABEL) != ''")
	private String name;

	@ManyToMany(
	        cascade = {CascadeType.PERSIST,CascadeType.MERGE},
	        mappedBy = "labels",
	        targetEntity = TransactionImpl.class
	    )
	private Collection<Transaction> transactions = new ArrayList<Transaction>();

	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Label#setTransactions(java.util.Collection)
     */
	@Override
    public void setTransactions(Collection<Transaction> transactions) {
		this.transactions = transactions;
	}

	//TODO Maybe the same transaction is set...
	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Label#addTransaction(com.proserus.stocks.model.transactions.TransactionImpl)
     */
	@Override
    public void addTransaction(Transaction t) {
		if (!transactions.contains(t)) {
			transactions.add(t);
		} else {
			throw new IllegalArgumentException();
		}
	}

	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Label#removeTransaction(com.proserus.stocks.model.transactions.Transaction)
     */
	@Override
    public void removeTransaction(Transaction t) {
		transactions.remove(t);
	}

	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Label#getTransactions()
     */
	@Override
    public Collection<Transaction> getTransactions() {
		return transactions;
	}

	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Label#setName(java.lang.String)
     */
	@Override
    public void setName(String name) {
		if (name == null) {
			throw new NullPointerException();
		}
		
		if (name.isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.name = name.trim();
	}

	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Label#getName()
     */
	@Override
    public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}

	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Label#compareTo(java.lang.Object)
     */
    @Override
	public int compareTo(Object arg0) {
		if (arg0 instanceof LabelImpl) {
			return ((Label) arg0).toString().compareTo(toString());
		}
		return -1;
	}

	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Label#equals(java.lang.Object)
     */
	@Override
    public boolean equals(Object obj) {
		return toString().equals(((Label) obj).toString());
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
