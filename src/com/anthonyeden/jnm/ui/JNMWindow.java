package com.anthonyeden.jnm.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.JTree;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;

import com.anthonyeden.lib.gui.StatusPanel;
import com.anthonyeden.lib.gui.StandardAction;
import com.anthonyeden.lib.log.Logger;
import com.anthonyeden.lib.log.LogManager;

import com.anthonyeden.jnm.JNMClient;

public class JNMWindow extends JFrame{
	
	public JNMWindow(){
		client = new JNMClient();
		init();
		loadAppProperties();
		
		String lastConfigurationPath = appProperties.getProperty("configuration.last");
		if(lastConfigurationPath != null){
			File lastConfigurationFile = new File(lastConfigurationPath);
			if(lastConfigurationFile.exists()){
				open(lastConfigurationFile);
			}
		}
	}
	
	public static void error(Throwable t){
		error("Error: " + t.getMessage(), t);
	}
	
	public static void error(String message, Throwable t){
		t.printStackTrace();
		
		JOptionPane.showMessageDialog(null, message, "Error", 
			JOptionPane.ERROR_MESSAGE);
	}
	
	// action methods
	
	public void open(){
		JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
		chooser.rescanCurrentDirectory();
		if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			File file = chooser.getSelectedFile();
			if(file != null){
				open(file);
			}	
		}
	}
	
	public void open(File file){
		try{
			client.loadConfiguration(file);
			client.startAllMonitors();
			lastSavedFile = file;
			appProperties.setProperty("configuration.last", file.toString());
		} catch(Exception e){
			error(e);
		}
	}
	
	public void revert(){
		if(lastSavedFile != null){
			open(lastSavedFile);
		}
	}
	
	public void save(){
		if(lastSavedFile != null){
			save(lastSavedFile);
		} else {
			saveAs();
		}
	}
	
	public void save(File file){
		try{
			client.saveConfiguration(file);
			lastSavedFile = file;
			appProperties.setProperty("configuration.last", file.toString());
		} catch(Exception e){
			error(e);
		}
	}
	
	public void saveAs(){
		JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
		chooser.rescanCurrentDirectory();
		if(chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
			File file = chooser.getSelectedFile();
			if(file != null){
				save(file);
			}	
		}
	}
	
	public void quit(){
		if(client.isModified()){
			Object[] options = { "Save", "Don't Save", "Cancel" };
			int result = JOptionPane.showOptionDialog(this, 
				"Do you want to save your current configuration?", "Save?", 
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, options, options[0]);
			switch(result){
				case 0:
					client.stopAllMonitors();
					save();
					break;
				case 1:
					client.stopAllMonitors();
					break;
				default:
					return;
			}
		}
		
		saveAppProperties();
		System.exit(0);
	}
	
	public void cut(){
		
	}
	
	public void copy(){
		
	}
	
	public void paste(){
		
	}
	
	public void startAllMonitors(){
		client.startAllMonitors();
	}
	
	public void stopAllMonitors(){
		client.stopAllMonitors();
	}
	
	public void addMonitor(){
		AddMonitorDialog d = new AddMonitorDialog(client);
		d.setVisible(true);
	}
	
	public void editMonitorProperties(){
		log.debug("Editing monitor properties");
		monitorTable.editMonitorProperties();
	}
	
	public void removeMonitor(){
		monitorTable.removeMonitor();
	}
	
	public void manageActions(){
		ManageActionsDialog d = new ManageActionsDialog(client);
		d.setVisible(true);
	}
	
	// other methods
	
	public void startMonitor(){
		client.startAllMonitors();
	}
	
	public void stopMonitor(){
		client.stopAllMonitors();
	}
	
	public Map getActions(){
		return actions;
	}

	public File getHomeDirectory(){
		File homeDir = new File(System.getProperty("user.home"), ".jnm");
		if(!homeDir.exists()){
			homeDir.mkdirs();
		}
		return homeDir;
	}
	
	public void loadAppProperties(){
		try{
			File propsFile = new File(getHomeDirectory(), "app.properties");
			if(propsFile.exists()){
				appProperties = new Properties();
				appProperties.load(new FileInputStream(propsFile));	
			}
		} catch(Exception e){
			error("Error loading application properties", e);
		}
	}
	
	public void saveAppProperties(){
		try{
			File propsFile = new File(getHomeDirectory(), "app.properties");
			appProperties.store(new FileOutputStream(propsFile), "JNM Application Properties");
		} catch(Exception e){
			error("Error saving application properties", e);
		}
	}
	
	private void init(){
		initActions();
		
		monitorTableModel = new MonitorTableModel(client.getMonitors());
		monitorTable = new MonitorTable(monitorTableModel, client);
		
		JScrollPane monitorTableScrollPane = new JScrollPane(monitorTable);
		monitorTableScrollPane.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent evt){
				monitorTable.setSelectedMonitor(evt.getPoint());
			}
		});
		
		statusPanel = new StatusPanel();
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(monitorTableScrollPane, BorderLayout.CENTER);
		getContentPane().add(statusPanel, BorderLayout.SOUTH);
		
		menuBar = new JNMWindowMenuBar(this);
		setJMenuBar(menuBar);
		
		setTitle("Java Network Monitor");
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent evt){
				quit();
			}
		});
		
		pack();
	}
	
	private void initActions(){
		actions = new HashMap();
		
		actions.put("file.open", new StandardAction(this, "open"));
		actions.put("file.revert", new StandardAction(this, "revert"));
		actions.put("file.save", new StandardAction(this, "save"));
		actions.put("file.saveAs", new StandardAction(this, "saveAs"));
		actions.put("file.quit", new StandardAction(this, "quit"));
		
		actions.put("edit.cut", new StandardAction(this, "cut"));
		actions.put("edit.copy", new StandardAction(this, "copy"));
		actions.put("edit.paste", new StandardAction(this, "paste"));
		
		actions.put("monitor.startAll", new StandardAction(this, "startAllMonitors"));
		actions.put("monitor.stopAll", new StandardAction(this, "stopAllMonitors"));
		actions.put("monitor.addMonitor", new StandardAction(this, "addMonitor"));
		actions.put("monitor.removeMonitor", new StandardAction(this, "removeMonitor"));
		actions.put("monitor.actions", new StandardAction(this, "manageActions"));
	}
	
	private static final Logger log = LogManager.getLogger(JNMWindow.class.getName());
	
	private JNMClient client;
	private JNMWindowMenuBar menuBar;
	
	private MonitorTableModel monitorTableModel;
	private MonitorTable monitorTable;
	private StatusPanel statusPanel;
	
	private Map actions;
	private Properties appProperties = new Properties();
	
	private File lastSavedFile;
	
}
