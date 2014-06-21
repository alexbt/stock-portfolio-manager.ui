package com.proserus.stocks.ui.dbutils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.jfree.data.time.Year;

@Entity(name = "b.HistoricalPrice")
@NamedQueries( { @NamedQuery(name = "historicalPriceUpdate.findAll", query = "SELECT h FROM b.HistoricalPrice h")})
public class HistoricalPriceForUpdate{

    @Id
	private Integer id;
    
	public Integer getId() {
        return id;
    }

    @Column(nullable = false)
	private Year year;
	
	public Year getYear() {
		return year;
	}
}
