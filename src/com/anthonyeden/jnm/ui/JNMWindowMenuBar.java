package com.anthonyeden.jnm.ui;

import java.awt.Event;
import java.awt.event.KeyEvent;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.Action;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class JNMWindowMenuBar extends JMenuBar{
	
	public JNMWindowMenuBar(JNMWindow parent){
		this.parent = parent;
		init();
	}
	
	public void loadResources(){
		fileMenu.setText("File");
		fileMenu.setMnemonic('f');
		
		openMenuItem.setText("Open...");
		openMenuItem.setMnemonic('o');
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
	
		revertMenuItem.setText("Revert");
		revertMenuItem.setToolTipText("Revert to last saved configuration");
		
		saveMenuItem.setText("Save");
		saveMenuItem.setMnemonic('s');
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
		
		saveAsMenuItem.setText("Save As...");
		saveAsMenuItem.setMnemonic('a');
		saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK + Event.SHIFT_MASK));
	
		quitMenuItem.setText("Quit");
		quitMenuItem.setMnemonic('q');
		quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK));
	
		editMenu.setText("Edit");
		editMenu.setMnemonic('e');
		
		cutMenuItem.setText("Cut");
		cutMenuItem.setMnemonic('u');
		cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK));
		
		copyMenuItem.setText("Copy");
		copyMenuItem.setMnemonic('c');
		copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK));
		
		pasteMenuItem.setText("Paste");
		pasteMenuItem.setMnemonic('p');
		pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK));
		
		monitorMenu.setText("Monitor");
		monitorMenu.setMnemonic('m');
		
		startAllMonitorsMenuItem.setText("Start All Monitors");
		
		stopAllMonitorsMenuItem.setText("Stop All Monitors");
		
		addMonitorMenuItem.setText("Add Monitor...");
		
		removeMonitorMenuItem.setText("Remove Monitor");
		
		actionsMenuItem.setText("Actions...");
	}
	
	private void init(){
		Map actions = parent.getActions();
		
		fileMenu = new JMenu();
		openMenuItem = fileMenu.add((Action)actions.get("file.open"));
		fileMenu.addSeparator();
		revertMenuItem = fileMenu.add((Action)actions.get("file.revert"));
		fileMenu.addSeparator();
		saveMenuItem = fileMenu.add((Action)actions.get("file.save"));
		saveAsMenuItem = fileMenu.add((Action)actions.get("file.saveAs"));
		fileMenu.addSeparator();
		quitMenuItem = fileMenu.add((Action)actions.get("file.quit"));
		add(fileMenu);
		
		editMenu = new JMenu();
		cutMenuItem = editMenu.add((Action)actions.get("edit.cut"));
		copyMenuItem = editMenu.add((Action)actions.get("edit.copy"));
		pasteMenuItem = editMenu.add((Action)actions.get("edit.paste"));
		add(editMenu);
		
		monitorMenu = new JMenu();
		startAllMonitorsMenuItem = monitorMenu.add((Action)actions.get("monitor.startAll"));
		stopAllMonitorsMenuItem = monitorMenu.add((Action)actions.get("monitor.stopAll"));
		monitorMenu.addSeparator();
		addMonitorMenuItem = monitorMenu.add((Action)actions.get("monitor.addMonitor"));
		removeMonitorMenuItem = monitorMenu.add((Action)actions.get("monitor.removeMonitor"));
		monitorMenu.addSeparator();
		actionsMenuItem = monitorMenu.add((Action)actions.get("monitor.actions"));
		add(monitorMenu);
		
		loadResources();	
	}
	
	private JNMWindow parent;
	
	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenu monitorMenu;
	
	private JMenuItem openMenuItem;
	private JMenuItem revertMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem saveAsMenuItem;
	private JMenuItem quitMenuItem;
	private JMenuItem cutMenuItem;
	private JMenuItem copyMenuItem;
	private JMenuItem pasteMenuItem;
	private JMenuItem startAllMonitorsMenuItem;
	private JMenuItem stopAllMonitorsMenuItem;
	private JMenuItem addMonitorMenuItem;
	private JMenuItem removeMonitorMenuItem;
	private JMenuItem actionsMenuItem;
	
}
