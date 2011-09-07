package com.proserus.stocks.model.transactions;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.joda.time.DateTime;

import com.proserus.stocks.model.symbols.Symbol;
import com.proserus.stocks.utils.BigDecimalUtils;


@Entity(name="Transaction")
@NamedQueries( {
        @NamedQuery(name = "transaction.findAll", query = "SELECT t FROM Transaction t"),
        @NamedQuery(name = "transaction.findAllBySymbol", query = "SELECT t FROM Transaction t WHERE symbol_id = :symbolId"),
        @NamedQuery(name = "transaction.findAllByCurrency", query = "SELECT t FROM Transaction t, Symbol s WHERE symbol_id = s.id AND s.currency = :currency"),
        @NamedQuery(name = "transaction.findAllByLabel", query = "SELECT t FROM Transaction t WHERE :label in elements(t.labels)"),
        @NamedQuery(name = "transaction.findMinDate", query = "SELECT min(date) FROM Transaction t")
        })
public class TransactionImpl implements Transaction{

	private static final String SEMICOLON_STR = ";";

	private static final String YYYY_M_MDD = "yyyyMMdd";

	@Column(nullable = false, columnDefinition="DECIMAL(38,8)")
	//Add constraint for min 0
	private BigDecimal commission;
	//Add constraint to check that the date is before Today (sysdate)
	private Date date;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;

	@ManyToMany( 
			targetEntity=LabelImpl.class, //TODO or Label.class
			cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinTable(
			name = "TRANSACTION_LABEL", 
			joinColumns =@JoinColumn(name = "transactionId"),
			inverseJoinColumns = @JoinColumn(name = "labelId")
		)
	private Collection<Label> labels = new ArrayList<Label>();

	@Column(nullable = false, columnDefinition="DECIMAL(38,8)")
	//Add constraint for min 0
	private BigDecimal price;

	@Column(nullable = false, columnDefinition="DECIMAL(38,8)")
	//Add constraint for min 0
	private BigDecimal quantity;

	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, optional = false)
	//TODO Symbol ? @Column(nullable = false)
	private Symbol symbol;

	@Column(nullable = false)
	private TransactionType type;

	public TransactionImpl() {
		// for JPA
	}

	//TODO Maybe the same label can be set twice 
	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Transaction#addLabel(com.proserus.stocks.model.transactions.Label)
     */
	@Override
    public void addLabel(Label label) {
		if (label == null) {
			throw new NullPointerException();
		}

		if (label.getName().isEmpty()) {
			throw new IllegalArgumentException();
		}

		this.labels.add(label);
		label.addTransaction(this);
	}

	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Transaction#getCommission()
     */
	@Override
    public BigDecimal getCommission() {
		return commission;
	}

	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Transaction#getDate()
     */
	@Override
    public Date getDate() {
		return date;
	}
	
	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Transaction#getDateTime()
     */
	@Override
    public DateTime getDateTime() {
		return new DateTime(date);
	}
	
	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Transaction#getId()
     */
	@Override
    public Integer getId() {
		return id;
	}

	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Transaction#getLabelsValues()
     */
	@Override
    public Collection<Label> getLabelsValues() {
		return labels;
	}

	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Transaction#getPrice()
     */
	@Override
    public BigDecimal getPrice() {
		return price;
	}

	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Transaction#getQuantity()
     */
	@Override
    public BigDecimal getQuantity() {
		return quantity;
	}

	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Transaction#getSymbol()
     */
	@Override
    public Symbol getSymbol() {
		return symbol;
	}

	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Transaction#getType()
     */
	@Override
    public TransactionType getType() {
		return type;
	}

	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Transaction#removeLabel(com.proserus.stocks.model.transactions.Label)
     */
	@Override
    public void removeLabel(Label label) {
		if(label == null){
			throw new NullPointerException();
		}
		
		if(label.getName().isEmpty()){
			throw new IllegalArgumentException();
		}
		
		labels.remove(label);
		label.removeTransaction(this);
	}

	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Transaction#setCommission(java.math.BigDecimal)
     */
	@Override
    public void setCommission(BigDecimal commission) {
		if (commission == null) {
			throw new NullPointerException();
		}
		
		this.commission = BigDecimalUtils.setDecimalWithScale(commission);
	}

	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Transaction#setDate(java.util.Date)
     */
	@Override
    public void setDate(Date date) {
		this.date = date;
	}

	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Transaction#setDateTime(org.joda.time.DateTime)
     */
	@Override
    public void setDateTime(DateTime date) {
		this.date = date.toDate();
	}

	//TODO Maybe the same label can be set twice
	//When removing labels.. we need to remove the transaction link too...
	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Transaction#setLabels(java.util.Collection)
     */
	@Override
    public void setLabels(Collection<Label> labels) {
		if (labels == null || labels.contains(null)) {
			throw new NullPointerException();
		}

		if (labels.contains("")){
			throw new IllegalArgumentException();
		}

		for (Label label : this.labels) {
			label.removeTransaction(this);
		}
		this.labels.clear();
		for (Label label : labels) {
			addLabel(label);
		}
	}

	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Transaction#setPrice(java.math.BigDecimal)
     */
	@Override
    public void setPrice(BigDecimal price) {
		if (price == null) {
			throw new NullPointerException();
		}
		this.price = BigDecimalUtils.setDecimalWithScale(price);
	}

	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Transaction#setQuantity(java.math.BigDecimal)
     */
	@Override
    public void setQuantity(BigDecimal quantity) {
		if (quantity == null) {
			throw new NullPointerException();
		}
		this.quantity = BigDecimalUtils.setDecimalWithScale(quantity);
	}

	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Transaction#setSymbol(com.proserus.stocks.model.symbols.Symbol)
     */
	@Override
    public void setSymbol(Symbol symbol) {
		if (symbol == null) {
			throw new NullPointerException();
		}

		this.symbol = symbol;
	}

	/* (non-Javadoc)
     * @see com.proserus.stocks.model.transactions.Transaction#setType(com.proserus.stocks.model.transactions.TransactionType)
     */
	@Override
    public void setType(TransactionType type) {
		if (type == null) {
			throw new NullPointerException();
		}
		this.type = type;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_M_MDD);
		return sdf.format(date) + SEMICOLON_STR + getType() + SEMICOLON_STR + getSymbol().getTicker() + SEMICOLON_STR
		        + getPrice() + SEMICOLON_STR + getQuantity() + SEMICOLON_STR + getCommission() + SEMICOLON_STR
		        + labels.toString() + SEMICOLON_STR;
	}
}
