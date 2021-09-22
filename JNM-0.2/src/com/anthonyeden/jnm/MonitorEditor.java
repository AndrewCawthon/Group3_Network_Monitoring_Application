package com.anthonyeden.jnm;

import java.awt.Component;

import com.anthonyeden.jnm.event.EditorListener;

/**	Standard interface which is implemented by all monitor
	editors.
	
	@author Anthony Eden
*/

public interface MonitorEditor{
	
	/**	Get an editor component.
	
		@return The editor Component
	*/
	
	public Component getComponent();
	
	/**	Get the monitor that is being edited.
	
		@return The Monitor
	*/
	
	public Monitor getMonitor();
	
	/**	Set the monitor that the editor is editing.
	
		@param monitor The Monitor
	*/
	
	public void setMonitor(Monitor monitor);
	
	/**	Save the current edits.  Returns true if the save
		was successful.
		
		@return True if the edits were saved
	*/
	
	public boolean save();
	
	/**	Revert to the last saved data. */
	
	public void revert();
	
	public void addEditorListener(EditorListener l);
	public void removeEditorListener(EditorListener l);
	
}
