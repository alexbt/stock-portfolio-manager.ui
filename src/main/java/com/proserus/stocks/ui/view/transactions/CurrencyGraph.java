package com.proserus.stocks.ui.view.transactions;

import java.text.NumberFormat;
import java.util.Collection;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import com.proserus.stocks.bo.analysis.CurrencyAnalysis;

public class CurrencyGraph extends ChartPanel  {

	public CurrencyGraph() {
	    super(null);
    }
	
	public void updateData(Collection<? extends CurrencyAnalysis> col){
		
		JFreeChart chart =ChartFactory.createPieChart(
	            "Market Value by Currencies (%)",  // chart title
	            createDataset(col),             // data
	            false,               // include legend
	            false,
	            false
	        );
		final PiePlot plot = (PiePlot) chart.getPlot();
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
	            "{0} = {2}", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()
	        ));
		setChart(chart);
	}
	
	private static PieDataset createDataset(Collection<? extends CurrencyAnalysis> col) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for(CurrencyAnalysis analysis: col){
        	dataset.setValue(analysis.getCurrency().name(), analysis.getMarketValue().floatValue());
        }
        return dataset;        
    }

}
