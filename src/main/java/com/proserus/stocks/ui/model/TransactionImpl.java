package com.proserus.stocks.ui.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

import org.apache.commons.lang3.Validate;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bo.transactions.Label;
import com.proserus.stocks.bo.transactions.Transaction;
import com.proserus.stocks.bo.transactions.TransactionType;
import com.proserus.stocks.bo.utils.BigDecimalUtils;
import com.proserus.stocks.bo.utils.LoggerUtils;
import com.proserus.stocks.bp.utils.DateUtils;

@Entity(name = "Transaction")
@NamedQueries({
		@NamedQuery(name = "transaction.findAll", query = "SELECT t FROM Transaction t"),
		@NamedQuery(name = "transaction.findAllBySymbol", query = "SELECT t FROM Transaction t WHERE symbol_id = :symbolId"),
		@NamedQuery(name = "transaction.findAllByCurrency", query = "SELECT t FROM Transaction t, Symbol s WHERE symbol_id = s.id AND s.currency = :currency"),
		@NamedQuery(name = "transaction.findAllByLabel", query = "SELECT t FROM Transaction t WHERE :label in elements(t.labels)"),
		@NamedQuery(name = "transaction.findMinDate", query = "SELECT min(date) FROM Transaction t") })
public class TransactionImpl implements Transaction {

	@Column(nullable = false, columnDefinition = "DECIMAL(38,8)")
	@Check(constraints = "commission >= 0")
	private BigDecimal commission;

	@Column(nullable = false)
	@Check(constraints = "date < sysdate")
	private Date date;

	@Id
	@GeneratedValue(generator = "auto_increment")
	@GenericGenerator(name = "auto_increment", strategy = "increment")
	private Integer id;

	@ManyToMany(targetEntity = LabelImpl.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "TRANSACTION_LABEL", joinColumns = @JoinColumn(name = "transactionId"), inverseJoinColumns = @JoinColumn(name = "labelId"))
	private Collection<Label> labels = new ArrayList<Label>();

	@Column(nullable = false, columnDefinition = "DECIMAL(38,8)")
	@Check(constraints = "price > 0")
	private BigDecimal price;

	@Column(nullable = false, columnDefinition = "DECIMAL(38,8)")
	@Check(constraints = "quantity > 0")
	private BigDecimal quantity;

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, targetEntity = SymbolImpl.class, optional = false)
	private Symbol symbol;

	@Type(type = "com.proserus.stocks.bo.enu.TransactionTypeUserType")
	@Column(nullable = false)
	private TransactionType type;

	@Transient
	private Calendar calendar;

	public TransactionImpl() {
		// for JPA
	}

	@PostLoad
	private void postLoad() {
		setCalendar(DateUtils.getCalendar(date));
	}

	// TODO Maybe the same label can be set twice
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.proserus.stocks.bo.transactions.Transaction#addLabel(com.proserus
	 * .stocks.bo.transactions.Label)
	 */
	@Override
	public void addLabel(Label label) {
		Validate.notNull(label);
		Validate.notEmpty(label.getName());

		this.labels.add(label);
		label.addTransaction(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.transactions.Transaction#getCommission()
	 */
	@Override
	public BigDecimal getCommission() {
		return commission;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.transactions.Transaction#getDate()
	 */
	@Override
	public Calendar getCalendar() {
		return calendar;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.transactions.Transaction#getId()
	 */
	@Override
	public Integer getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.transactions.Transaction#getLabelsValues()
	 */
	@Override
	public Collection<Label> getLabelsValues() {
		return labels;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.transactions.Transaction#getPrice()
	 */
	@Override
	public BigDecimal getPrice() {
		return price;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.transactions.Transaction#getQuantity()
	 */
	@Override
	public BigDecimal getQuantity() {
		return quantity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.transactions.Transaction#getSymbol()
	 */
	@Override
	public Symbol getSymbol() {
		return symbol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.transactions.Transaction#getType()
	 */
	@Override
	public TransactionType getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.proserus.stocks.bo.transactions.Transaction#removeLabel(com.proserus
	 * .stocks.bo.transactions.Label)
	 */
	@Override
	public void removeLabel(Label label) {
		Validate.notNull(label);
		Validate.notEmpty(label.getName());

		labels.remove(label);
		label.removeTransaction(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.proserus.stocks.bo.transactions.Transaction#setCommission(java.math
	 * .BigDecimal)
	 */
	@Override
	public void setCommission(BigDecimal commission) {
		Validate.notNull(commission);
		Validate.isTrue(BigDecimalUtils.isNotNegative(commission));

		this.commission = BigDecimalUtils.setDecimalWithScale(commission);
	}

	// TODO Maybe the same label can be set twice
	// When removing labels.. we need to remove the transaction link too...
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.transactions.Transaction#setLabels(java.util.
	 * Collection)
	 */
	@Override
	public void setLabels(Collection<Label> labels) {
		Validate.notNull(labels);
		Validate.isTrue(!labels.contains(null));
		Validate.isTrue(!labels.contains(""));

		for (Label label : labels) {
			label.removeTransaction(this);
		}
		this.labels.clear();
		for (Label label : labels) {
			addLabel(label);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.proserus.stocks.bo.transactions.Transaction#setPrice(java.math.BigDecimal
	 * )
	 */
	@Override
	public void setPrice(BigDecimal price) {
		Validate.notNull(price);
		Validate.isTrue(BigDecimalUtils.isNotNegative(price));
		this.price = BigDecimalUtils.setDecimalWithScale(price);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.proserus.stocks.bo.transactions.Transaction#setQuantity(java.math
	 * .BigDecimal)
	 */
	@Override
	public void setQuantity(BigDecimal quantity) {
		Validate.notNull(quantity);
		Validate.isTrue(BigDecimalUtils.isNotNegative(quantity));
		this.quantity = BigDecimalUtils.setDecimalWithScale(quantity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.proserus.stocks.bo.transactions.Transaction#setSymbol(com.proserus
	 * .stocks.bo.symbols.Symbol)
	 */
	@Override
	public void setSymbol(Symbol symbol) {
		Validate.notNull(symbol);
		this.symbol = symbol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.proserus.stocks.bo.transactions.Transaction#setType(com.proserus.
	 * stocks.bo.transactions.TransactionType)
	 */
	@Override
	public void setType(TransactionType type) {
		Validate.notNull(type);
		this.type = type;
	}

	@Override
	public String toString() {
		assert LoggerUtils.validateCalledFromLogger() : LoggerUtils.callerException();
		return "TransactionImpl [commission=" + commission + ", date=" + date + ", id=" + id + ", labels=" + labels + ", price=" + price
				+ ", quantity=" + quantity + ", symbol=" + symbol + ", type=" + type + "]";
	}

	@Override
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
		date = calendar.getTime();
	}
}
