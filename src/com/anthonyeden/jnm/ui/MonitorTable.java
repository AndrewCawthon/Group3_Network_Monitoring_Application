package com.anthonyeden.jnm.ui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.Map;
import java.util.HashMap;

import javax.swing.JTable;

import com.anthonyeden.lib.gui.StandardAction;
import com.anthonyeden.lib.log.Logger;
import com.anthonyeden.lib.log.LogManager;

import com.anthonyeden.jnm.Monitor;
import com.anthonyeden.jnm.JNMClient;
import com.anthonyeden.jnm.MonitorState;

public class MonitorTable extends JTable{
	
	public MonitorTable(MonitorTableModel model, JNMClient client){
		super(model);
		this.client = client;
		
		setDefaultRenderer(MonitorState.class, new MonitorStateCellRenderer());
		//setDefaultRenderer(ActivityState.class, new ActivityStateCellRenderer());
		
		init();
	}
	
	public void removeMonitor(){
		Monitor monitor = getSelectedMonitor();
		if(monitor != null){
			client.removeMonitor(monitor);
		}
	}
	
	public void editMonitorProperties(){
		log.debug("Editing monitor properties");
		try{
			Monitor monitor = getSelectedMonitor();
			if(monitor != null){
				try{
					MonitorPropertiesDialog d = new MonitorPropertiesDialog(client);
					d.setMonitor(monitor);
					d.show();
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		} catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	public void executeNow(){
		final Monitor monitor = getSelectedMonitor();
		if(monitor != null){
			Thread t = new Thread(new Runnable(){
				public void run(){
					monitor.execute();
				}
			});
			t.start();
		}
	}
	
	public void showLastRequest(){
		Monitor monitor = getSelectedMonitor();
		if(monitor != null){
			log.debug("Show last request: " + monitor.getName());
			try{
				LastRequestDialog d = new LastRequestDialog();
				d.setMonitor(monitor);
				d.setVisible(true);
			} catch(Exception e){
				e.printStackTrace();
			}
		}	
	}
	
	public void showLastResponse(){
		Monitor monitor = getSelectedMonitor();
		if(monitor != null){
			log.debug("Show last response: " + monitor.getName());
			try{
				LastResponseDialog d = new LastResponseDialog();
				d.setMonitor(monitor);
				d.setVisible(true);
			} catch(Exception e){
				e.printStackTrace();
			}
		}	
	}
	
	public Monitor getSelectedMonitor(){
		int selectedRow = getSelectedRow();
		if(selectedRow >= 0){
			return (Monitor)client.getMonitors().get(selectedRow);
		} else {
			return null;
		}
	}
	
	public void setSelectedMonitor(Monitor monitor){
		setSelectedMonitor(client.getMonitors().indexOf(monitor));
	}
	
	public void setSelectedMonitor(Point p){
		setSelectedMonitor(rowAtPoint(p));
	}
	
	public void setSelectedMonitor(int row){
		if(row >= 0){
			setRowSelectionInterval(row, row);
		} else {
			clearSelection();
		}
	}
	
	public synchronized void showPopup(Point p){
		//log.debug("showPopup(" + p + ")");
		if(getSelectedMonitor() != null){
			//log.debug("Showing popup");
			MonitorTablePopupMenu menu = new MonitorTablePopupMenu(this, client);
			menu.show(this, p.x, p.y);
		}
	}
	
	public Map getActions(){
		return actions;
	}
	
	private void init(){
		initActions();
		
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent evt){
				//log.debug("Mouse pressed.");
				setSelectedMonitor(evt.getPoint());
				if(evt.isPopupTrigger()){
					//log.debug("Popup trigger on mousePressed");
					showPopup(evt.getPoint());
				}
			}
			public void mouseReleased(MouseEvent evt){
				//log.debug("Mouse released.");
				if(evt.isPopupTrigger()){
					//log.debug("Popup trigger on mouseReleased");
					showPopup(evt.getPoint());
				}
			}
		});
	}
	
	private void initActions(){
		actions = new HashMap();
		actions.put("monitor.remove", new StandardAction(this, "removeMonitor"));
		actions.put("monitor.properties", new StandardAction(this, "editMonitorProperties"));
		actions.put("monitor.executeNow", new StandardAction(this, "executeNow"));
		actions.put("monitor.lastRequest", new StandardAction(this, "showLastRequest"));
		actions.put("monitor.lastResponse", new StandardAction(this, "showLastResponse"));
	}
	
	private static final Logger log = LogManager.getLogger(MonitorTable.class.getName());
	
	private JNMClient client;
	private Map actions;
	
}
