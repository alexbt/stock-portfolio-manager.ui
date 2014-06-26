package com.proserus.stocks.ui.controller;

import java.io.IOException;
import java.util.Locale;

import org.junit.Before;

public class AbstractUnit {
	static {
		Locale.setDefault(Locale.CANADA);
	}

	@Before
	public void clean() throws IOException {
		Locale.setDefault(Locale.CANADA);
	}
}
