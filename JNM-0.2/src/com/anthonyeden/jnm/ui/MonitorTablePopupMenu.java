package com.anthonyeden.jnm.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.anthonyeden.jnm.Monitor;
import com.anthonyeden.jnm.JNMClient;

public class MonitorTablePopupMenu extends JPopupMenu{
	
	public MonitorTablePopupMenu(MonitorTable parent, JNMClient client){
		this.parent = parent;
		this.client = client;
		init();
	}
	
	private void loadResources(){
		removeMenuItem.setText("Remove");
		
		propertiesMenuItem.setText("Properties...");
		
		executeNowMenuItem.setText("Execute Now");
		
		lastRequestMenuItem.setText("Last Request...");
		lastResponseMenuItem.setText("Last Response...");
	}
	
	private void init(){
		Map actions = parent.getActions();
		
		String startStopString = "Start";
		if(client.isRunning(parent.getSelectedMonitor())){
			startStopString = "Stop";
		}
		
		startStopMenuItem = new JMenuItem(startStopString);
		startStopMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				Monitor monitor = parent.getSelectedMonitor();
				if(client.isRunning(monitor)){
					client.stopMonitor(monitor);
					startStopMenuItem.setText("Start");
				} else {
					client.startMonitor(monitor);
					startStopMenuItem.setText("Stop");
				}
			}
		});
		add(startStopMenuItem);
		
		addSeparator();
		removeMenuItem = add((Action)actions.get("monitor.remove"));
		addSeparator();
		propertiesMenuItem = add((Action)actions.get("monitor.properties"));
		addSeparator();
		executeNowMenuItem = add((Action)actions.get("monitor.executeNow"));
		lastRequestMenuItem = add((Action)actions.get("monitor.lastRequest"));
		lastResponseMenuItem = add((Action)actions.get("monitor.lastResponse"));
		
		loadResources();
	}
	
	private JNMClient client;
	private MonitorTable parent;
	
	private JMenuItem startStopMenuItem;
	private JMenuItem removeMenuItem;
	private JMenuItem propertiesMenuItem;
	private JMenuItem executeNowMenuItem;
	private JMenuItem lastRequestMenuItem;
	private JMenuItem lastResponseMenuItem;
	
}
