package com.proserus.stocks.view.general;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.proserus.stocks.view.common.ViewControllers;

public class CenterPanel extends JPanel implements ActionListener{
	
	private JButton updateInternet = new JButton("Get Current Prices");
	private JButton updateHistorical = new JButton("Get Old Prices");
	
	public CenterPanel(){
		setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setAlignmentX(JPanel.RIGHT_ALIGNMENT);
		panel.add(updateInternet);
		panel.add(updateHistorical);
		updateInternet.addActionListener(this);
		updateInternet.setMnemonic(KeyEvent.VK_P);
		updateHistorical.addActionListener(this);
		updateHistorical.setMnemonic(KeyEvent.VK_O);

		add(panel,BorderLayout.NORTH);
		add(new Tab(),BorderLayout.CENTER);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		 if (arg0.getSource().equals(updateInternet)) {
			 ViewControllers.getController().updatePrices();
		} else if (arg0.getSource().equals(updateHistorical)) {
			ViewControllers.getController().updateHistoricalPrices();
		}
	}

}
