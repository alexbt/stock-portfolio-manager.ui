package com.proserus.stocks.ui;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.proserus.stocks.bo.common.BoBuilder;
import com.proserus.stocks.bp.Filter;
import com.proserus.stocks.bp.businesslogic.AnalysisBp;
import com.proserus.stocks.bp.businesslogic.LabelsBp;
import com.proserus.stocks.bp.businesslogic.OnlineUpdateBp;
import com.proserus.stocks.bp.businesslogic.SymbolsBp;
import com.proserus.stocks.bp.businesslogic.TransactionsBp;
import com.proserus.stocks.bp.businesslogic.YahooUpdateBp;
import com.proserus.stocks.ui.model.BoBuilderImpl;
import com.proserus.stocks.ui.view.general.Window;

public class SwingModule extends AbstractModule {
	  @Override 
	  protected void configure() {
	    bind( Window.class ).in( Scopes.SINGLETON );
	    bind( BoBuilder.class ).to(BoBuilderImpl.class).in( Scopes.SINGLETON );
	    bind(OnlineUpdateBp.class).to(YahooUpdateBp.class);
	    bind( AnalysisBp.class ).in( Scopes.SINGLETON );
	    bind( LabelsBp.class ).in( Scopes.SINGLETON );
	    bind( SymbolsBp.class ).in( Scopes.SINGLETON );
	    bind( TransactionsBp.class ).in( Scopes.SINGLETON );
	    bind( Filter.class ).in( Scopes.SINGLETON );
	  }
	}