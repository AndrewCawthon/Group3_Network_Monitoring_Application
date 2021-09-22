package com.anthonyeden.jnm;

public class MonitorType{
	
	public MonitorType(String name, String className){
		setName(name);
		setClassName(className);
	}
	
	public Monitor getMonitorInstance() throws Exception{
		return (Monitor)Class.forName(getClassName()).newInstance();
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getClassName(){
		return className;
	}
	
	public void setClassName(String className){
		this.className = className;
	}
	
	public String toString(){
		return getName();
	}
	
	private String name;
	private String className;
	
}
