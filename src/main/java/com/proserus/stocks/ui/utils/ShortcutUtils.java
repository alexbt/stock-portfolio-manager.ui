package com.proserus.stocks.ui.utils;

import java.awt.event.KeyEvent;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import com.proserus.stocks.ui.view.actions.AddSymbolAction;
import com.proserus.stocks.ui.view.actions.AddTransactionAction;
import com.proserus.stocks.ui.view.actions.CloseApplicationAction;
import com.proserus.stocks.ui.view.actions.ExportToCsvAction;
import com.proserus.stocks.ui.view.actions.ImportFromCsvAction;
import com.proserus.stocks.ui.view.actions.RemoveSymbolAction;
import com.proserus.stocks.ui.view.actions.RemoveTransactionAction;
import com.proserus.stocks.ui.view.actions.ShowAboutAction;
import com.proserus.stocks.ui.view.actions.ShowEditSymbolAction;
import com.proserus.stocks.ui.view.actions.ShowHideFiltersAction;
import com.proserus.stocks.ui.view.actions.ShowSettingsAction;
import com.proserus.stocks.ui.view.actions.UpdateOldPricesAction;
import com.proserus.stocks.ui.view.actions.UpdatePriceAction;

public class ShortcutUtils {

	public static void apply(JRootPane rootPane){
		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
      ActionMap actionMap = rootPane.getActionMap(); 

      inputMap.put(KeyStroke.getKeyStroke(AddTransactionAction.keyEvent, KeyEvent.CTRL_DOWN_MASK), AddTransactionAction.getInstance());
      actionMap.put(AddTransactionAction.getInstance(), AddTransactionAction.getInstance());
      
      inputMap.put(KeyStroke.getKeyStroke(AddSymbolAction.keyEvent, KeyEvent.CTRL_DOWN_MASK), AddSymbolAction.getInstance());
      actionMap.put(AddSymbolAction.getInstance(), AddSymbolAction.getInstance());
      
      inputMap.put(KeyStroke.getKeyStroke(ShowEditSymbolAction.keyEvent, KeyEvent.CTRL_DOWN_MASK), ShowEditSymbolAction.getInstance());
      actionMap.put(ShowEditSymbolAction.getInstance(), ShowEditSymbolAction.getInstance());
      
      inputMap.put(KeyStroke.getKeyStroke(CloseApplicationAction.keyEvent, KeyEvent.CTRL_DOWN_MASK), CloseApplicationAction.getInstance());
      actionMap.put(CloseApplicationAction.getInstance(), CloseApplicationAction.getInstance());
      
      inputMap.put(KeyStroke.getKeyStroke(ImportFromCsvAction.keyEvent, KeyEvent.CTRL_DOWN_MASK), ImportFromCsvAction.getInstance());
      actionMap.put(ImportFromCsvAction.getInstance(), ImportFromCsvAction.getInstance());
      
      inputMap.put(KeyStroke.getKeyStroke(ExportToCsvAction.keyEvent, KeyEvent.CTRL_DOWN_MASK), ExportToCsvAction.getInstance());
      actionMap.put(ExportToCsvAction.getInstance(), ExportToCsvAction.getInstance());
      
      inputMap.put(KeyStroke.getKeyStroke(ShowSettingsAction.keyEvent, KeyEvent.CTRL_DOWN_MASK), ShowSettingsAction.getInstance());
      actionMap.put(ShowSettingsAction.getInstance(), ShowSettingsAction.getInstance());
      
      inputMap.put(KeyStroke.getKeyStroke(UpdatePriceAction.keyEvent, KeyEvent.CTRL_DOWN_MASK), UpdatePriceAction.getInstance());
      actionMap.put(UpdatePriceAction.getInstance(), UpdatePriceAction.getInstance());
      
      inputMap.put(KeyStroke.getKeyStroke(UpdateOldPricesAction.keyEvent, KeyEvent.CTRL_DOWN_MASK), UpdateOldPricesAction.getInstance());
      actionMap.put(UpdateOldPricesAction.getInstance(), UpdateOldPricesAction.getInstance());
      
      inputMap.put(KeyStroke.getKeyStroke(ShowAboutAction.keyEvent, KeyEvent.CTRL_DOWN_MASK), ShowAboutAction.getInstance());
      actionMap.put(ShowAboutAction.getInstance(), ShowAboutAction.getInstance());
      
      inputMap.put(KeyStroke.getKeyStroke(ShowHideFiltersAction.keyEvent, KeyEvent.CTRL_DOWN_MASK), ShowHideFiltersAction.getInstance());
      actionMap.put(ShowHideFiltersAction.getInstance(), ShowHideFiltersAction.getInstance());
      
      inputMap.put(KeyStroke.getKeyStroke(RemoveSymbolAction.keyEvent, KeyEvent.CTRL_DOWN_MASK), RemoveSymbolAction.getInstance());
      actionMap.put(RemoveSymbolAction.getInstance(), RemoveSymbolAction.getInstance());
      
      inputMap.put(KeyStroke.getKeyStroke(RemoveTransactionAction.keyEvent, KeyEvent.CTRL_DOWN_MASK), RemoveTransactionAction.getInstance());
      actionMap.put(RemoveTransactionAction.getInstance(), RemoveTransactionAction.getInstance());
	}
}
