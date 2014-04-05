package com.proserus.stocks.ui.view.transactions;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventBus;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.SwingEvents;
import com.proserus.stocks.ui.controller.PortfolioController;
import com.proserus.stocks.ui.controller.ViewControllers;
import com.proserus.stocks.ui.view.general.Tab;

public class GraphsPanel extends JPanel implements EventListener,ChangeListener{
	private static final long serialVersionUID = 201404041920L;
	private boolean needToRecalculate = false;
	private boolean graphTabVisible = false;
	
	private PortfolioController controller = ViewControllers.getController();
	SectorGraph sector = new SectorGraph();
	CurrencyGraph currencies = new CurrencyGraph();
	LabelGraph label = new LabelGraph();
	YearGraph years = new YearGraph();
	
	public GraphsPanel(){
		GridLayout gl = new GridLayout(2,2);
//		gl.setHgap(5);
//		gl.setVgap(5);
		setLayout(gl);
		add(sector);
		add(label);
		add(currencies);
		add(years);
		EventBus.getInstance().add(this, SwingEvents.YEAR_ANALYSIS_UPDATED,SwingEvents.CURRENCY_ANALYSIS_UPDATED,SwingEvents.SECTOR_ANALYSIS_UPDATED,SwingEvents.TRANSACTION_UPDATED,SwingEvents.LABEL_ANALYSIS_UPDATED);
	}


	@Override
    public void update(Event event, Object model) {
		if(event.equals(SwingEvents.SECTOR_ANALYSIS_UPDATED)){
			sector.updateData(SwingEvents.SECTOR_ANALYSIS_UPDATED.resolveModel(model));
		}else if(event.equals(SwingEvents.LABEL_ANALYSIS_UPDATED)){
			label.updateData(SwingEvents.LABEL_ANALYSIS_UPDATED.resolveModel(model));
		}else if(event.equals(SwingEvents.CURRENCY_ANALYSIS_UPDATED)){
			currencies.updateData(SwingEvents.CURRENCY_ANALYSIS_UPDATED.resolveModel(model));
		}else if(event.equals(SwingEvents.YEAR_ANALYSIS_UPDATED)){
			years.updateData(SwingEvents.YEAR_ANALYSIS_UPDATED.resolveModel(model));
		}else if (SwingEvents.TRANSACTION_UPDATED.equals(event)){
			if(graphTabVisible){
				controller.calculateSectorAnalysis();
				controller.calculateLabelAnalysis();
				controller.calculateYearAnalysis();
				needToRecalculate = false;
			}else{
				needToRecalculate = true;
			}
		}
    }

	@Override
    public void stateChanged(ChangeEvent arg0) {
		if(((Tab)arg0.getSource()).getSelectedComponent().equals(this)){
			graphTabVisible = true;
			if(needToRecalculate){
				controller.calculateSectorAnalysis();
				controller.calculateLabelAnalysis();
				controller.calculateYearAnalysis();
				needToRecalculate = false;
			}
		}else{
			graphTabVisible = false;
		}
    }

}


