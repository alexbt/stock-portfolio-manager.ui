package com.proserus.stocks.ui.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceUnit;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.GenericGenerator;

import com.proserus.stocks.bo.symbols.HistoricalPrice;
import com.proserus.stocks.bo.utils.BigDecimalUtils;
import com.proserus.stocks.bo.utils.LoggerUtils;

@Entity(name = "HistoricalPrice")
@PersistenceUnit(unitName = "stockPortfolioJpa")
@NamedQueries({ @NamedQuery(name = "historicalPrice.findAll", query = "SELECT h FROM HistoricalPrice h") })
public class HistoricalPriceImpl implements HistoricalPrice {

	@Id
	@GeneratedValue(generator = "auto_increment")
	@GenericGenerator(name = "auto_increment", strategy = "increment")
	private Integer id;

	// TODO add constraint to forbid years later this current year
	@Column(nullable = false)
	private Integer year;

	public Integer getYear() {
		return year;
	}

	@Column(nullable = false, columnDefinition = "DECIMAL(38,8)")
	@Check(constraints = "PRICE >= 0")
	private BigDecimal price;

	@Column(nullable = false, columnDefinition = "DECIMAL(38,8)")
	@Check(constraints = "CUSTOMPRICE >= 0")
	private BigDecimal customPrice;

	public BigDecimal getCustomPrice() {
		return customPrice;
	}

	public void setCustomPrice(BigDecimal customPrice) {
		this.customPrice = BigDecimalUtils.setDecimalWithScale(customPrice);
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = BigDecimalUtils.setDecimalWithScale(price);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((year == null) ? 0 : year.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistoricalPriceImpl other = (HistoricalPriceImpl) obj;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}

	@Override
	public String toString() {
		assert LoggerUtils.validateCalledFromLogger() : LoggerUtils.callerException();
		return "HistoricalPriceImpl [id=" + id + ", year=" + year + ", price=" + price + ", customPrice=" + customPrice + "]";
	}

}
