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
import com.anthonyeden.jnm.JNMRequest;

public class LastRequestDialog extends JDialog{
	
	public LastRequestDialog(){
		init();
	}
	
	public void setMonitor(Monitor monitor){
		this.monitor = monitor;
		setTitle("Last Request: " + monitor.getName());
		
		JNMRequest request = monitor.getLastRequest();
		String requestText = "[No Request]";
		if(request != null){
			requestText = request.toString();
		}
		
		lastRequestArea.setText(requestText);
	}
	
	private void init(){
		lastRequestArea = new JTextArea();
		lastRequestArea.setRows(24);
		lastRequestArea.setColumns(48);
		lastRequestArea.setLineWrap(true);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JScrollPane(lastRequestArea), BorderLayout.CENTER);
		getContentPane().add(createButtonPanel(), BorderLayout.SOUTH);
		
		setTitle("Last Request");
		
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
	
	private JTextArea lastRequestArea;
	private JButton closeButton;
	
}
