package com.anthonyeden.jnm.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

import com.anthonyeden.jnm.Monitor;
import com.anthonyeden.jnm.JNMClient;

public class MonitorPropertiesDialog extends JFrame{
	
	public MonitorPropertiesDialog(JNMClient client){
		this.client = client;
		init();
	}
	
	public void setMonitor(Monitor monitor){
		try{
			this.monitor = monitor;
			monitorEditorPanel.setEditor(monitor.getMonitorEditor());
		} catch(Exception e){
			JNMWindow.error(e);
		}
	}
	
	public boolean save(){
		boolean saveResult = monitorEditorPanel.save();
		if(saveResult){
			client.setModified(true);
		}
		return saveResult;
	}
	
	public void close(){
		dispose();
		
		if(monitor != null){
			monitor.executeAsynchronously();
		}
	}
	
	private void init(){
		monitorEditorPanel = new MonitorEditorPanel(client);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(monitorEditorPanel, BorderLayout.CENTER);
		getContentPane().add(createButtonPanel(), BorderLayout.SOUTH);
		
		setTitle("Monitor Properties");
		
		getRootPane().setDefaultButton(closeButton);
		
		pack();
		setSize(600, 400);
	}
	
	private JPanel createButtonPanel(){
		JPanel panel = new JPanel();
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				save();
			}
		});
		panel.add(saveButton);
		
		saveAndCloseButton = new JButton("Save and Close");
		saveAndCloseButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				if(save()){
					close();
				}
			}
		});
		panel.add(saveAndCloseButton);
		
		closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				close();
			}
		});
		panel.add(closeButton);
		
		return panel;
	}
	
	private JNMClient client;
	private Monitor monitor;
	private MonitorEditorPanel monitorEditorPanel;
	
	private JButton saveButton;
	private JButton saveAndCloseButton;
	private JButton closeButton;
	
}
