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

import com.proserus.stocks.bo.analysis.YearAnalysis;
import com.proserus.stocks.bo.utils.BigDecimalUtils;

public class YearGraph extends ChartPanel {
	private static final long serialVersionUID = 201404042021L;

	public YearGraph() {
		super(null);
	}

	public void updateData(Collection<? extends YearAnalysis> col) {

		JFreeChart chart = ChartFactory.createBarChart("Overall Return per Year (%)", // chart
																						// title
				"", "", createDataset(col), // data
				PlotOrientation.HORIZONTAL, false, // include legend
				false, false);

		if (!col.isEmpty()) {
			BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
			renderer.setMaximumBarWidth(.10d);
			renderer.setMaximumBarWidth(.10d);
			renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", NumberFormat.getInstance()));
			renderer.setBaseItemLabelsVisible(true);

			ItemLabelPosition negative = new ItemLabelPosition(ItemLabelAnchor.INSIDE9, TextAnchor.CENTER_LEFT);
			ItemLabelPosition positive = new ItemLabelPosition(ItemLabelAnchor.INSIDE3, TextAnchor.CENTER_RIGHT);
			renderer.setBasePositiveItemLabelPosition(positive);
			renderer.setBaseNegativeItemLabelPosition(negative);

			YearAnalysis yearAnalysis = col.iterator().next();
			if (yearAnalysis.getOverallReturn().intValue() >= 0) {
				((BarRenderer) chart.getCategoryPlot().getRenderer()).setSeriesPaint(0, Color.green);
				((BarRenderer) chart.getCategoryPlot().getRenderer()).setSeriesPaint(1, Color.red);
			} else {
				((BarRenderer) chart.getCategoryPlot().getRenderer()).setSeriesPaint(0, Color.red);
				((BarRenderer) chart.getCategoryPlot().getRenderer()).setSeriesPaint(1, Color.green);
			}
		}
		setChart(chart);
	}

	private static DefaultCategoryDataset createDataset(Collection<? extends YearAnalysis> col) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.clear();
		for (YearAnalysis analysis : col) {
			float val = analysis.getOverallReturn().floatValue();
			if (val < 0) {
				dataset.addValue(BigDecimalUtils.setDecimalWithScale2(analysis.getOverallReturn()), "Negative", "" + analysis.getYear());
			} else {
				dataset.addValue(BigDecimalUtils.setDecimalWithScale2(analysis.getOverallReturn()), "Positive", "" + analysis.getYear());
			}

		}
		return dataset;
	}

}
