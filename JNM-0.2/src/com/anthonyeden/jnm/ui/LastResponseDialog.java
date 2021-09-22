package com.anthonyeden.jnm.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import com.anthonyeden.jnm.Monitor;
import com.anthonyeden.jnm.JNMResponse;

public class LastResponseDialog extends JDialog{
	
	public LastResponseDialog(){
		init();
	}
	
	public void setMonitor(Monitor monitor){
		this.monitor = monitor;
		setTitle("Last Response: " + monitor.getName());
		
		JNMResponse response = monitor.getLastResponse();
		String responseText = "[No Response]";
		if(response != null){
			responseText = response.toString();
		}
		
		lastResponseArea.setText(responseText);
	}
	
	private void init(){
		lastResponseArea = new JTextArea();
		lastResponseArea.setRows(24);
		lastResponseArea.setColumns(48);
		lastResponseArea.setLineWrap(true);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JScrollPane(lastResponseArea), BorderLayout.CENTER);
		getContentPane().add(createButtonPanel(), BorderLayout.SOUTH);
		
		setTitle("Last Response");
		
		pack();
	}
	
	private JPanel createButtonPanel(){
		JPanel panel = new JPanel();
		
		closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				dispose();
			}
		});
		panel.add(closeButton);
		
		return panel;
	}
	
	private Monitor monitor;
	
	private JTextArea lastResponseArea;
	private JButton closeButton;
	
}
