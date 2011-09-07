package com.proserus.stocks.model.symbols;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.jfree.data.time.Year;

import com.proserus.stocks.utils.BigDecimalUtils;

@Entity(name = "HistoricalPrice")
public class HistoricalPriceImpl implements HistoricalPrice {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;

	@Column(nullable = false)
	//@Check(constraints = "YEAR <= YEAR(ADD_YEARS(SYSDATE,1))")
	//TODO add constraint to forbid years later this current year
	private Year year;

	public Year getYear() {
		return year;
	}

	@Column(nullable = false, columnDefinition="DECIMAL(38,8)")
	//TODO @Check(constraints = "PRICE >= 0")
	private BigDecimal price;

	@Column(nullable = false, columnDefinition="DECIMAL(38,8)")
	//TODO @Check(constraints = "CUSTOMPRICE >= 0")
	private BigDecimal customPrice;

	// @ManyToOne(targetEntity = Symbol.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	// private List<Symbol> symbols;

	// public List<Symbol> getSymbols() {
	// return symbols;
	// }

	public BigDecimal getCustomPrice() {
		return customPrice;
	}

	public void setCustomPrice(BigDecimal customPrice) {
		this.customPrice = BigDecimalUtils.setDecimalWithScale(customPrice);
	}

	public void setYear(Year year) {
		this.year = year;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = BigDecimalUtils.setDecimalWithScale(price);
	}
	
	@Override
	public boolean equals(Object obj) {
	    if(!(obj instanceof HistoricalPriceImpl)){
	    	return false;
	    }
	    if(((HistoricalPriceImpl)obj).getYear().equals(getYear())){
	    	return true;
	    }
	    
	    return false;
	}
}
