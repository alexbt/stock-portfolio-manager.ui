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

import com.proserus.stocks.bo.analysis.SectorAnalysis;

public class SectorGraph extends ChartPanel {
	private static final long serialVersionUID = 201404041920L;

	public SectorGraph() {
		super(null);
	}

	public void updateData(Collection<? extends SectorAnalysis> col) {

		JFreeChart chart = ChartFactory.createPieChart("Market Value by Sectors (%)", // chart
																						// title
				createDataset(col), // data
				false, // include legend
				false, false);
		final PiePlot plot = (PiePlot) chart.getPlot();
		// plot.set
		// PieRenderer renderer = ((BarRenderer) chart.getPlot().getRenderer());
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} = {2}", NumberFormat.getNumberInstance(), NumberFormat
				.getPercentInstance()));
		setChart(chart);
	}

	private static PieDataset createDataset(Collection<? extends SectorAnalysis> col) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		for (SectorAnalysis analysis : col) {
			dataset.setValue(analysis.getSector().getTitle(), analysis.getMarketValue().floatValue());
		}
		return dataset;
	}

}
