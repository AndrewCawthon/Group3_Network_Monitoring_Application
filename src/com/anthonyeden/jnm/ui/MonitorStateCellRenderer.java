package com.anthonyeden.jnm.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.anthonyeden.lib.log.Logger;
import com.anthonyeden.lib.log.LogManager;

import com.anthonyeden.jnm.MonitorState;

public class MonitorStateCellRenderer extends DefaultTableCellRenderer{
	
	public MonitorStateCellRenderer(){
		
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, 
		boolean isSelected, boolean hasFocus, int row, int column)
	{
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		//log.debug("Rendering MonitorState cell");
		MonitorState state = (MonitorState)value;
		
		if(!isSelected){
			switch(state.getValue()){
				case MonitorState.STATE_OK:
					setBackground(Color.green);
					break;
				case MonitorState.STATE_DOWN:
				case MonitorState.STATE_ERROR:
				case MonitorState.STATE_FAILED:
					setBackground(Color.red);
					break;
				case MonitorState.STATE_TIMEOUT:
					setBackground(Color.yellow);
					break;
				case MonitorState.STATE_NOT_TESTED:
					setBackground(Color.lightGray);
					break;
				default:
					setBackground(Color.white);
					//System.out.println("Set background color to white.");
					break;
			}
		}
		
		return this;
	}
	
	private static final Logger log = LogManager.getLogger(MonitorStateCellRenderer.class.getName());
	
}
