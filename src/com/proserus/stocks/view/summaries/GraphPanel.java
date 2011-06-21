//package com.proserus.stocks.view.summaries;
//
//import java.awt.Color;
//import java.awt.Font;
//import java.awt.GradientPaint;
//import java.util.Collection;
//import java.util.Observable;
//import java.util.Observer;
//
//import javax.swing.JPanel;
//
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.axis.AxisLocation;
//import org.jfree.chart.axis.CategoryAxis;
//import org.jfree.chart.axis.NumberAxis;
//import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
//import org.jfree.chart.plot.CategoryPlot;
//import org.jfree.chart.plot.PiePlot;
//import org.jfree.chart.plot.PlotOrientation;
//import org.jfree.chart.renderer.category.BarRenderer;
//import org.jfree.data.category.CategoryDataset;
//import org.jfree.data.category.DefaultCategoryDataset;
//import org.jfree.data.general.PieDataset;
//
//import com.proserus.stocks.controllers.PortfolioControllerImpl;
//import com.proserus.stocks.controllers.iface.SummaryController;
//import com.proserus.stocks.model.summaries.SummaryLabel;
//import com.proserus.stocks.view.common.SharedFilter;
//
///**
// * A simple demonstration application showing how to create a chart consisting of multiple pie charts.
// * 
// */
//public class GraphPanel extends JPanel implements Observer {
//	private SummaryController summaryController = PortfolioControllerImpl.getInstance();
//	final ChartPanel chartPanel;
//
//	/**
//	 * Creates a new demo.
//	 * 
//	 * @param title
//	 *            the frame title.
//	 */
//	public GraphPanel(final String title) {
//		chartPanel = new ChartPanel(null, true, true, true, true, true);
//		chartPanel.setPreferredSize(new java.awt.Dimension(800, 550));
//		SharedFilter.getInstance().addObserver(this);
//		add(chartPanel);
//		summaryController.addTransactionsObserver(this);
//	}
//
//	/**
//	 * Creates a sample dataset.
//	 * 
//	 * @return A sample dataset.
//	 */
//	private JFreeChart createChart2(CategoryDataset dataset) {
//		JFreeChart fc = ChartFactory.createBarChart("Market value per tag", // chart title
//		        "Tags", // domain axis label
//		        "Market value", // range axis label
//		        dataset, // data
//		        PlotOrientation.HORIZONTAL, // orientation
//		        true, // include legend
//		        true, // tooltips?
//		        false // URLs?
//		        );
//
//		CategoryPlot plot = (CategoryPlot) fc.getPlot();
//		plot.setBackgroundPaint(Color.lightGray);
//		plot.setRangeGridlinePaint(Color.white);
//		plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
//		BarRenderer renderer = (BarRenderer) plot.getRenderer();
//		renderer.setBaseItemLabelsVisible(true);
//		
//		// prints the value at the end
//		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
//
//		CategoryAxis categoryAxis = plot.getDomainAxis();
//		categoryAxis.setCategoryMargin(0.0);
//		categoryAxis.setUpperMargin(0.02);
//		categoryAxis.setLowerMargin(0.02);
//		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
//		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
//		rangeAxis.setUpperMargin(0.10);
//		GradientPaint gradientpaint = new GradientPaint(0.0F, 0.0F, Color.blue, 0.0F, 0.0F, new Color(0, 0, 64));
//		renderer.setSeriesPaint(0, gradientpaint);
//
//		return fc;
//	}
//
//	/**
//	 * Creates a sample chart with the given dataset.
//	 * 
//	 * @param dataset
//	 *            the dataset.
//	 * 
//	 * @return A sample chart.
//	 */
//	private JFreeChart createChart(PieDataset dataset) {
//
//		JFreeChart chart = ChartFactory.createPieChart("Market value per tag", // chart title
//		        dataset, // data
//		        true, // include legend
//		        true, false);
//
//		PiePlot plot = (PiePlot) chart.getPlot();
//		plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
//		plot.setNoDataMessage("No data available");
//		plot.setCircular(false);
//		return chart;
//	}
//
//	@Override
//	public void update(Observable arg0, Object UNUSED) {
//		// TODO Graph - Check Update
//		if (arg0 instanceof SharedFilter) {
//			//Filter.getInstance().getLabels()
//			Collection<SummaryLabel> summ = summaryController.getLabelSummaries(SharedFilter.getInstance());
//			
//			//summaryController.isLabelOverlapping(null);
//
//			//if (summ.isLabelOverlapping()) {
//				DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//				for (SummaryLabel summaryLabel: summ) {
//					if (summaryLabel == null)
//						continue;
//
//					dataset.setValue(Math.round(summaryLabel.getMarketValue()), summaryLabel.getLabel(), summaryLabel
//					        .getLabel());
//				}
//				//Float market = summaryController.getSummaries().get(summaryId).getRealMarketValue();
//				chartPanel.setChart(createChart2(dataset));
//				/*ValueMarker marker = new ValueMarker(summaryLabel.market, Color.red,    
//		                new BasicStroke(1.0f), Color.red,    
//		                new BasicStroke(1.0f), 1.0f);  
//				((CategoryPlot)chartPanel.getChart().getPlot()).getRangeAxis().setUpperBound(market+market*.1);
//				
//				((CategoryPlot)chartPanel.getChart().getPlot()).addRangeMarker(marker, Layer.BACKGROUND); 
//			 *//*} else {
//				DefaultPieDataset dataset = new DefaultPieDataset();
//				for (Iterator iterator = col.iterator(); iterator.hasNext();) {
//					SummaryLabel summaryLabel = (SummaryLabel) iterator.next();
//					if (summaryLabel == null)
//						continue;
//
//					dataset.setValue(summaryLabel.getLabel(),
//					        Math.round(summaryLabel.getMarketValue()));
//					dataset.sortByKeys(SortOrder.ASCENDING);
//
//				}
//				chartPanel.setChart(createChart(dataset));
//			}*/
//			
//		}
//	}
// }