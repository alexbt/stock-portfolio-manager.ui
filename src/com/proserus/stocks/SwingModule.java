package com.proserus.stocks;

import com.google.inject.Scopes;
import com.proserus.stocks.view.general.Window;

public class SwingModule extends BpModule {
	  @Override 
	  protected void configure() {
		  super.configure();
	    bind( Window.class ).in( Scopes.SINGLETON );
	  }
	}