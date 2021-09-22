package com.anthonyeden.jnm.event;

import java.util.EventListener;

public interface EditorListener extends EventListener{
	
	public void saved(EditorEvent evt);
	
}
