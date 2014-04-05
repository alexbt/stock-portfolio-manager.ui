package com.proserus.stocks.bp.strategies;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.jfree.data.time.Year;
import org.junit.Assert;
import org.junit.Test;

import com.proserus.stocks.bo.analysis.AnalysisImpl;
import com.proserus.stocks.bo.symbols.CurrencyEnum;
import com.proserus.stocks.bo.symbols.SectorEnum;
import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bo.transactions.Label;
import com.proserus.stocks.bo.transactions.Transaction;
import com.proserus.stocks.bo.transactions.TransactionType;
import com.proserus.stocks.bp.model.Filter;
import com.proserus.stocks.bp.services.PerfOverviewStrategyEnum;
import com.proserus.stocks.ui.model.LabelImpl;
import com.proserus.stocks.ui.model.SymbolImpl;
import com.proserus.stocks.ui.model.TransactionImpl;

public class AnalysisTest {

	@Test
	public void test() {
		AnalysisImpl ana = new AnalysisImpl();
		Collection<Transaction> col = new ArrayList<Transaction>();
		Symbol s = createSymbol("PRS","Groupe Proserus Inc.", SectorEnum.TECHNOLOGY,
				CurrencyEnum.CAD, new BigDecimal(45));
		Collection<Label> labels = createLabels(new String[] { "Label1, Label2" });

		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, -1);
		Transaction t = createTransaction(TransactionType.BUY, c.getTime(),
				new BigDecimal(10), new BigDecimal(50), new BigDecimal(1000),
				s, labels);
		col.add(t);

		c.add(Calendar.MONTH, 3);
		t = createTransaction(TransactionType.BUY, c.getTime(), new BigDecimal(
				10), new BigDecimal(25), new BigDecimal(1000), s, labels);
		col.add(t);

		t = createTransaction(TransactionType.DIVIDEND, c.getTime(),
				new BigDecimal(0), new BigDecimal(.04), new BigDecimal(1000),
				s, labels);
		col.add(t);

		c.add(Calendar.MONTH, 3);
		t = createTransaction(TransactionType.SELL, c.getTime(),
				new BigDecimal(10), new BigDecimal(75), new BigDecimal(1000),
				s, labels);
		col.add(t);

		Filter f = new Filter();

		for (PerfOverviewStrategyEnum strategy : PerfOverviewStrategyEnum
				.values()) {
			strategy.getStrategy().process(ana, col, f);
		}

		Assert.assertEquals(ana.getCommission().stripTrailingZeros(),
				new BigDecimal("30").stripTrailingZeros());
		Assert.assertEquals(ana.getQuantity().stripTrailingZeros(),
				new BigDecimal("1000").stripTrailingZeros());
		Assert.assertEquals(ana.getQuantityBuy().stripTrailingZeros(),
				new BigDecimal("2000").stripTrailingZeros());
		Assert.assertEquals(ana.getQuantitySold().stripTrailingZeros(),
				new BigDecimal("1000").stripTrailingZeros());
		// Assert.assertTrue(ana.getCurrency().equals(CurrencyEnum.CAD));
		// Assert.assertTrue(ana.getAnnualizedReturn().toString().equals("1000.00000"));
		Assert.assertEquals(ana.getAveragePrice().stripTrailingZeros(),
				new BigDecimal("37.51").stripTrailingZeros());
		Assert.assertEquals(ana.getTotalSold().stripTrailingZeros(),
				new BigDecimal("75010").stripTrailingZeros());
		Assert.assertEquals(ana.getCapitalGain().stripTrailingZeros(),
				new BigDecimal("37500").stripTrailingZeros());
		// Assert.assertTrue(ana.getCapitalGainPercent().stripTrailingZeros().equals("0.499866702"));
		Assert.assertEquals(ana.getCostBasis().stripTrailingZeros(),
				new BigDecimal("37510").stripTrailingZeros());
		Assert.assertEquals(ana.getDividend().stripTrailingZeros(),
				new BigDecimal("40").stripTrailingZeros());
		Assert.assertEquals(ana.getTotalCost().stripTrailingZeros(),
				new BigDecimal("75020").stripTrailingZeros());
		// Assert.assertEquals(ana.getDividendYield().stripTrailingZeros(),new
		// BigDecimal("40").stripTrailingZeros());

		Assert.assertEquals(
				ana.getMarketGrowth().setScale(6, BigDecimal.ROUND_UP),
				new BigDecimal("19.968009").stripTrailingZeros());// (marketValue-CostBasis)/CostBasis
		Assert.assertEquals(ana.getMarketValue().stripTrailingZeros(),
				new BigDecimal("45000").stripTrailingZeros());// CurrentYear
																// price X
																// getQuantity
		// Assert.assertEquals(ana.getNumberOfYears().stripTrailingZeros(),new
		// BigDecimal("40").stripTrailingZeros());
		// Assert.assertEquals(ana.getOverallReturn().stripTrailingZeros(),new
		// BigDecimal("40").stripTrailingZeros());
		// Assert.assertEquals(ana.getEndOfPeriod().stripTrailingZeros(),new
		// BigDecimal("40").stripTrailingZeros());
		// Assert.assertEquals(ana.getStartOfPeriod().stripTrailingZeros(),new
		// BigDecimal("40").stripTrailingZeros());

	}

	private Collection<Label> createLabels(String[] labelNames) {
		Collection<Label> labels = new ArrayList<Label>();

		for (String lName : labelNames) {
			Label la = new LabelImpl();
			la.setName(lName);
			labels.add(la);
		}
		return labels;

	}

	private Symbol createSymbol(String ticker, String name, SectorEnum sector, CurrencyEnum cad, BigDecimal currentPrice) {
		Symbol s = new SymbolImpl();
		s.setCurrency(cad);
		s.setName(name);
		s.setPrice(currentPrice,
				new Year(Calendar.getInstance().get(Calendar.YEAR)));

		s.setSector(sector);
		s.setTicker(ticker);
		s.setCustomPriceFirst(false);
		return s;
	}

	private Transaction createTransaction(TransactionType type, Date date,
			BigDecimal commission, BigDecimal price, BigDecimal quantity,
			Symbol s, Collection<Label> labels) {
		Transaction t = new TransactionImpl();

		t.setCommission(commission);
		t.setDate(date);
		t.setLabels(labels);
		t.setPrice(price);
		t.setQuantity(quantity);
		t.setSymbol(s);
		t.setType(type);
		return t;
	}

}
