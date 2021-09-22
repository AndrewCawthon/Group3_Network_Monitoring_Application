package com.anthonyeden.jnm;

import com.anthonyeden.lib.config.Configuration;
import com.anthonyeden.lib.config.ConfigurationException;

public class ActionDefinition{
	
	public ActionDefinition(){
		
	}
	
	public Action getActionInstance() throws Exception{
		Action action = (Action)Class.forName(getClassName()).newInstance();
		action.setActionDefinition(this);
		return action;
	}
	
	public synchronized ActionEditor getEditorInstance() throws Exception{
		return (ActionEditor)Class.forName(getEditorClassName()).newInstance();
	}
	
	/**	Get the display name for the monitor implementation.
	
		@return The display name
	*/
	
	public String getName(){
		return name;
	}
	
	/**	Set the display name for the monitor implementation.
	
		@param name The new display name
	*/
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getClassName(){
		return className;
	}
	
	public void setClassName(String className){
		this.className = className;
	}
	
	public String getEditorClassName(){
		return editorClassName;
	}
	
	public void setEditorClassName(String editorClassName){
		this.editorClassName = editorClassName;
	}
	
	public boolean isDefault(){
		return useAsDefault;
	}
	
	public void setDefault(boolean useAsDefault){
		this.useAsDefault = useAsDefault;
	}
	
	public void setDefault(String useAsDefault){
		if(useAsDefault != null){
			setDefault(new Boolean(useAsDefault).booleanValue());
		}
	}
	
	public String toString(){
		return getName();
	}
	
	public void loadConfiguration(Configuration configuration){
		setName(configuration.getChildValue("name"));
		setClassName(configuration.getChildValue("classname"));
		setEditorClassName(configuration.getChildValue("editorclass"));
		setDefault(configuration.getAttribute("default"));
	}
	
	private String name;
	private String className;
	private String editorClassName;
	private boolean useAsDefault = false;
	
}
