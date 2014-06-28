package com.proserus.stocks.ui.model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

import org.apache.commons.lang3.Validate;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.proserus.stocks.bo.symbols.CurrencyEnum;
import com.proserus.stocks.bo.symbols.HistoricalPrice;
import com.proserus.stocks.bo.symbols.SectorEnum;
import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bo.utils.BigDecimalUtils;
import com.proserus.stocks.bo.utils.LoggerUtils;

@Entity(name = "Symbol")
@NamedQueries({ @NamedQuery(name = "symbol.findAll", query = "SELECT s FROM Symbol s"),
		@NamedQuery(name = "symbol.findByTicker", query = "SELECT s FROM Symbol s WHERE ticker = :ticker") })
public class SymbolImpl implements Comparable<Symbol>, Symbol {

	@Id
	@GeneratedValue(generator = "auto_increment")
	@GenericGenerator(name = "auto_increment", strategy = "increment")
	private Integer id;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.symbols.SymbolIF#getId()
	 */
	@Override
	public Integer getId() {
		return id;
	}

	private static final String EMPTY_STR = "";

	@OneToMany(targetEntity = HistoricalPriceImpl.class, cascade = CascadeType.ALL)
	@JoinTable(name = "SYMBOL_PRICES", joinColumns = { @JoinColumn(name = "symbolId") }, inverseJoinColumns = { @JoinColumn(name = "priceId") })
	private Set<HistoricalPrice> prices = new LinkedHashSet<HistoricalPrice>();

	@Transient
	private Map<Integer, HistoricalPrice> mapPrices = new HashMap<Integer, HistoricalPrice>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.symbols.SymbolIF#getMapPrices()
	 */
	@Override
	public Map<Integer, HistoricalPrice> getMapPrices() {
		return mapPrices;
	}

	@PostLoad
	private void postLoad() {
		setPrices(prices);
	}

	@Column(nullable = false)
	private boolean isCustomPriceFirst = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.symbols.SymbolIF#isCustomPriceFirst()
	 */
	@Override
	public boolean isCustomPriceFirst() {
		return isCustomPriceFirst;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.symbols.SymbolIF#setCustomPriceFirst(boolean)
	 */
	@Override
	public void setCustomPriceFirst(boolean isCustomPriceFirst) {
		this.isCustomPriceFirst = isCustomPriceFirst;
	}

	@Column(nullable = false)
	private String name = EMPTY_STR;

	@Column(unique = true, nullable = false)
	@Check(constraints = "LTRIM(ticker) <> ''")
	private String ticker;

	@Type(type = "com.proserus.stocks.bo.enu.CurrencyUserType")
	@Column(nullable = false)
	private CurrencyEnum currency = CurrencyEnum.valueOfById(Currency.getInstance(Locale.getDefault()).getCurrencyCode());

	@Type(type = "com.proserus.stocks.bo.enu.SectorUserType")
	@Column(nullable = false)
	private SectorEnum sector;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.symbols.SymbolIF#getSector()
	 */
	@Override
	public SectorEnum getSector() {
		if (null == this.sector) {
			return SectorEnum.UNKNOWN;
		}
		return sector;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.proserus.stocks.bo.symbols.SymbolIF#setSector(com.proserus.stocks
	 * .bo.symbols.SectorEnum)
	 */
	@Override
	public void setSector(SectorEnum sector) {
		this.sector = sector;
	}

	public SymbolImpl() {
		// for JPA
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.symbols.SymbolIF#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		Validate.notNull(name);
		// Empty is allowed

		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.symbols.SymbolIF#setTicker(java.lang.String)
	 */
	@Override
	public void setTicker(String ticker) {
		Validate.notNull(ticker);
		Validate.notEmpty(ticker);

		this.ticker = ticker.toLowerCase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.proserus.stocks.bo.symbols.SymbolIF#setPrice(java.math.BigDecimal)
	 */
	@Override
	public void setPrice(BigDecimal price, int year) {
		Validate.notNull(year);
		Validate.notNull(price);
		Validate.isTrue(BigDecimalUtils.isNotNegative(price));

		HistoricalPriceImpl histPrice = (HistoricalPriceImpl) mapPrices.get(year);
		if (histPrice == null) {
			histPrice = new HistoricalPriceImpl();
			histPrice.setYear(year);
			histPrice.setCustomPrice(price);
			mapPrices.put(year, histPrice);
			prices.add(histPrice);
		}
		histPrice.setPrice(price);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.proserus.stocks.bo.symbols.SymbolIF#setCurrency(com.proserus.stocks
	 * .bo.symbols.CurrencyEnum)
	 */
	@Override
	public void setCurrency(CurrencyEnum currency) {
		Validate.notNull(currency);
		this.currency = currency;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.symbols.SymbolIF#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.symbols.SymbolIF#getTicker()
	 */
	@Override
	public String getTicker() {
		return ticker;
	}

	@Override
	public HistoricalPrice getPrice(int year) {
		Validate.notNull(year);

		HistoricalPriceImpl h = (HistoricalPriceImpl) mapPrices.get(year);
		if (h == null) {
			h = new HistoricalPriceImpl();
			h.setCustomPrice(BigDecimal.ZERO);
			h.setPrice(BigDecimal.ZERO);
			h.setYear(year);
		}
		return h;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.symbols.SymbolIF#getHistoricalPricesValues()
	 */
	@Override
	public Set<HistoricalPrice> getHistoricalPricesValues() {
		return prices;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.symbols.SymbolIF#getPrices()
	 */
	@Override
	public Set<HistoricalPrice> getPrices() {
		return prices;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.proserus.stocks.bo.symbols.SymbolIF#addPrice(com.proserus.stocks.
	 * bo.symbols.HistoricalPrice)
	 */
	@Override
	public void addPrice(HistoricalPrice price) {
		Validate.notNull(price);

		// TODO Do I need a map of prices ?
		if (mapPrices.containsKey(price.getYear())) {
			prices.remove(price);
		}
		prices.add(price);
		mapPrices.put(price.getYear(), price);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.proserus.stocks.bo.symbols.SymbolIF#setPrices(java.util.Collection)
	 */
	@Override
	public void setPrices(Set<HistoricalPrice> prices) {
		Validate.notNull(prices);
		Validate.isTrue(!prices.contains(null));

		this.prices = prices;
		mapPrices.clear();
		for (HistoricalPrice hPrice : prices) {
			if (hPrice.getYear() == null) {
				prices.remove(hPrice);
			} else {
				mapPrices.put(hPrice.getYear(), hPrice);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.symbols.SymbolIF#getCurrency()
	 */
	@Override
	public CurrencyEnum getCurrency() {
		return currency;
	}

	@Override
	public int compareTo(Symbol arg0) {
		return getTicker().compareTo(((Symbol) arg0).getTicker());
	}

	@Override
	public String toString() {
		assert LoggerUtils.validateCalledFromLogger() : LoggerUtils.callerException();
		return "SymbolImpl [id=" + id + ", prices=" + prices + ", mapPrices=" + mapPrices + ", isCustomPriceFirst=" + isCustomPriceFirst
				+ ", name=" + name + ", ticker=" + ticker + ", currency=" + currency + ", sector=" + sector + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Symbol other = (Symbol) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		if (ticker == null) {
			if (other.getTicker() != null)
				return false;
		} else if (!ticker.equals(other.getTicker()))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.proserus.stocks.bo.symbols.SymbolIF#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((ticker == null) ? 0 : ticker.hashCode());
		return result;
	}
}
