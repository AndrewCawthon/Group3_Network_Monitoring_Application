package com.anthonyeden.jnm.event;

import java.util.Iterator;
import java.util.ArrayList;

public class EditorListenerSupport{

	public EditorListenerSupport(Object source){
		this.source = source;
		listeners = new ArrayList();
	}
	
	public void addEditorListener(EditorListener l){
		listeners.add(l);
	}
	
	public void removeEditorListener(EditorListener l){
		listeners.remove(l);
	}
	
	public void fireSaved(){
		ArrayList l = null;
		EditorEvent evt = new EditorEvent(source);
		
		synchronized(this){
			l = (ArrayList)(listeners.clone());
		}
		
		Iterator i = l.iterator();
		while(i.hasNext()){
			((EditorListener)i.next()).saved(evt);
		}
	}
	
	private Object source;
	private ArrayList listeners;
	
}
