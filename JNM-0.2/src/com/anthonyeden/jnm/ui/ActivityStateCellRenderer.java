package com.anthonyeden.jnm.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.anthonyeden.lib.log.Logger;
import com.anthonyeden.lib.log.LogManager;

import com.anthonyeden.jnm.ActivityState;

public class ActivityStateCellRenderer extends DefaultTableCellRenderer{
	
	public ActivityStateCellRenderer(){
		
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, 
		boolean isSelected, boolean hasFocus, int row, int column)
	{
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		//log.debug("Rendering ActivityState cell");
		ActivityState state = (ActivityState)value;
		
		if(!isSelected){
			switch(state.getValue()){
				case ActivityState.STATE_RUNNING:
					setBackground(Color.green);
					break;
				case ActivityState.STATE_STOPPING:
					setBackground(Color.orange);
					break;
				case ActivityState.STATE_STOPPED:
					setBackground(Color.red);
					break;
				case ActivityState.STATE_STARTING:
					setBackground(Color.yellow);
					break;
				case ActivityState.STATE_TESTING:
					setBackground(Color.blue);
					break;
				default:
					setBackground(Color.white);
					//System.out.println("Set background color to white.");
					break;
			}
		}
		
		return this;
	}
	
	private static final Logger log = LogManager.getLogger(ActivityStateCellRenderer.class.getName());
	
}
