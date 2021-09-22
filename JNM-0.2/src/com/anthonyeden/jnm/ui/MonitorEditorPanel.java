package com.anthonyeden.jnm.ui;

import java.awt.Insets;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;

import com.anthonyeden.lib.log.Logger;
import com.anthonyeden.lib.log.LogManager;

import com.anthonyeden.jnm.Monitor;
import com.anthonyeden.jnm.JNMClient;
import com.anthonyeden.jnm.MonitorEditor;

public class MonitorEditorPanel extends JPanel{
	
	public MonitorEditorPanel(JNMClient client){
		this.client = client;
		init();
	}
	
	public boolean save(){
		Monitor monitor = editor.getMonitor();
		monitor.setName(nameField.getText());
		monitor.setDelay(delayField.getText().trim());
		
		return editor.save();
	}
	
	public void revert(){
		Monitor monitor = editor.getMonitor();
		nameField.setText(monitor.getName());
		delayField.setText(new Integer(monitor.getDelay()).toString());
	}
	
	public void setEditor(MonitorEditor editor){
		log.debug("setEditor(" + editor + ")");
		this.editor = editor;
		setEditorComponent(editor.getComponent());
		assignActionsPanel.setMonitor(editor.getMonitor());
		revert();
	}
	
	public void setEditorComponent(Component editorComponent){
		if(currentEditorComponent != null){
			monitorEditorPanel.remove(currentEditorComponent);
		}
		
		currentEditorComponent = editorComponent;
		monitorEditorPanel.add(currentEditorComponent, BorderLayout.CENTER);
		monitorEditorPanel.revalidate();
	}
	
	private void init(){
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		setLayout(gbl);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		
		JPanel standardEditorPanel = createStandardEditor();
		gbc.weightx = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(standardEditorPanel, gbc);
		add(standardEditorPanel);
		
		// create the tab pane
		tabbedPane = new JTabbedPane();
		gbc.weighty = 1;
		gbl.setConstraints(tabbedPane, gbc);
		add(tabbedPane);
		
		// add general tab
		currentEditorComponent = new JPanel();
		
		monitorEditorPanel = new JPanel();
		monitorEditorPanel.setLayout(new BorderLayout());
		monitorEditorPanel.add(currentEditorComponent, BorderLayout.CENTER);
		tabbedPane.add(new JScrollPane(monitorEditorPanel), "General");
		
		// add actions tab
		assignActionsPanel = new AssignActionsPanel(client);
		tabbedPane.add(new JScrollPane(assignActionsPanel), "Actions");
	}
	
	private JPanel createStandardEditor(){
		JPanel panel = new JPanel();
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		panel.setLayout(gbl);
		
		gbc.insets = new Insets(1, 1, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		nameLabel = new JLabel("Name");
		gbc.weightx = 0;
		gbc.gridwidth = 1;
		gbl.setConstraints(nameLabel, gbc);
		panel.add(nameLabel);
		
		nameField = new JTextField();
		gbc.weightx = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(nameField, gbc);
		panel.add(nameField);
		
		delayLabel = new JLabel("Delay (milliseconds)");
		gbc.weightx = 0;
		gbc.gridwidth = 1;
		gbl.setConstraints(delayLabel, gbc);
		panel.add(delayLabel);
		
		delayField = new JTextField();
		gbc.weightx = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(delayField, gbc);
		panel.add(delayField);
		
		return panel;
	}
	
	private static final Logger log = LogManager.getLogger(MonitorEditorPanel.class.getName());
	
	private JNMClient client;
	
	private JTabbedPane tabbedPane;
	private MonitorEditor editor;
	private Component currentEditorComponent;
	private JPanel monitorEditorPanel;
	private AssignActionsPanel assignActionsPanel;
	
	private JLabel nameLabel;
	private JTextField nameField;
	private JLabel delayLabel;
	private JTextField delayField;

}
