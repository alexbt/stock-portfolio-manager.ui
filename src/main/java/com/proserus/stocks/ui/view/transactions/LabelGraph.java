package com.proserus.stocks.ui.view.transactions;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.Collection;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import com.proserus.stocks.bo.analysis.LabelAnalysis;

public class LabelGraph extends ChartPanel {
	private static final long serialVersionUID = 201404041920L;

	public LabelGraph() {
		super(null);
	}

	public void updateData(Collection<? extends LabelAnalysis> col) {

		JFreeChart chart = ChartFactory.createBarChart("Market Value by Labels (may overlap)", // chart title
		        "", "", createDataset(col), // data
		        PlotOrientation.HORIZONTAL, false, // include legend
		        false, false);
		BarRenderer renderer = ((BarRenderer) chart.getCategoryPlot().getRenderer());
		renderer.setSeriesPaint(0, Color.green);
		renderer.setMaximumBarWidth(.10d);
		
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}",NumberFormat.getInstance()));
		renderer.setBaseItemLabelsVisible(true);
		ItemLabelPosition positive = new ItemLabelPosition(
	            ItemLabelAnchor.INSIDE3, TextAnchor.CENTER_RIGHT
	        );
		renderer.setBasePositiveItemLabelPosition(positive);
		setChart(chart);
	}

	private static DefaultCategoryDataset createDataset(Collection<? extends LabelAnalysis> col) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.clear();
		for (LabelAnalysis analysis : col) {
			dataset.addValue(analysis.getMarketValue().floatValue(), "Labels", analysis.getLabel().getName());
		}
		return dataset;
	}

}


