package com.anthonyeden.jnm;

import java.awt.Component;

import com.anthonyeden.jnm.event.EditorListener;

/**	Standard interface which is implemented by all action
	editors.
	
	@author Anthony Eden
*/

public interface ActionEditor{
	
	/**	Get an editor component.
	
		@return The editor Component
	*/
	
	public Component getComponent();
	
	/**	Get the action that is being edited.
	
		@return The Action
	*/
	
	public Action getAction();
	
	/**	Set the action that the editor is editing.
	
		@param action The Action
	*/
	
	public void setAction(Action action);
	
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
