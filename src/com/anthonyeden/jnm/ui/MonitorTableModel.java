package com.anthonyeden.jnm.ui;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.AbstractTableModel;

import com.anthonyeden.lib.log.Logger;
import com.anthonyeden.lib.log.LogManager;
import com.anthonyeden.lib.util.XArrayList;

import com.anthonyeden.jnm.Monitor;
import com.anthonyeden.jnm.MonitorState;
import com.anthonyeden.jnm.ActivityState;

public class MonitorTableModel extends AbstractTableModel{
	
	public MonitorTableModel(XArrayList monitors){
		this.monitors = monitors;
		
		this.monitors.addListDataListener(new ListDataListener(){
			public void intervalAdded(ListDataEvent evt){
				fireTableRowsInserted(evt.getIndex0(), evt.getIndex1());
			}
			public void intervalRemoved(ListDataEvent evt){
				fireTableRowsDeleted(evt.getIndex0(), evt.getIndex1());
			}
			public void contentsChanged(ListDataEvent evt){
				//log.debug("Table rows updated: " + evt.getIndex0() + " to " + evt.getIndex1());
				fireTableRowsUpdated(evt.getIndex0(), evt.getIndex1());
			}
		});
	}
	
	public String getColumnName(int index){
		return columnNames[index];
	}
	
	public Class getColumnClass(int index){
		return columnClasses[index];
	}
	
	public int getColumnCount(){
		return columnNames.length;
	}
	
	public int getRowCount(){
		return monitors.size();
	}
	
	public Object getValueAt(int row, int column){
		Monitor monitor = (Monitor)monitors.get(row);
		
		switch(column){
			case 0:
				return monitor.getName();
			case 1:
				return monitor.getActivity();
			case 2:
				return monitor.getState();
			default:
				throw new IllegalArgumentException("Illegal column index");
		}
	}
	
	private static final String[] columnNames = {"Name", "Activity", "State"};
	private static final Class[] columnClasses = {String.class, ActivityState.class, MonitorState.class};
	
	private static final Logger log = LogManager.getLogger(MonitorTableModel.class.getName());
	
	private XArrayList monitors;
	
}
