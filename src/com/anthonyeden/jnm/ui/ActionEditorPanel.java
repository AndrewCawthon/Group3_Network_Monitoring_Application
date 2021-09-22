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

import com.anthonyeden.jnm.Action;
import com.anthonyeden.jnm.ActionEditor;

public class ActionEditorPanel extends JPanel{
	
	public ActionEditorPanel(){
		init();
	}
	
	public ActionEditor getEditor(){
		return editor;
	}
	
	public boolean save(){
		Action action = editor.getAction();
		action.setName(nameField.getText());
		//action.setDelay(delayField.getText().trim());
		
		return editor.save();
	}
	
	public void revert(){
		Action action = editor.getAction();
		nameField.setText(action.getName());
		//delayField.setText(new Integer(monitor.getDelay()).toString());
	}
	
	public void setEditor(ActionEditor editor){
		log.debug("setEditor(" + editor + ")");
		this.editor = editor;
		setEditorComponent(editor.getComponent());
		revert();
	}
	
	public void setEditorComponent(Component editorComponent){
		if(currentEditorComponent != null){
			actionEditorPanel.remove(currentEditorComponent);
		}
		
		currentEditorComponent = editorComponent;
		actionEditorPanel.add(currentEditorComponent, BorderLayout.CENTER);
		actionEditorPanel.revalidate();
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
		
		actionEditorPanel = new JPanel();
		actionEditorPanel.setLayout(new BorderLayout());
		actionEditorPanel.add(currentEditorComponent, BorderLayout.CENTER);
		tabbedPane.add(new JScrollPane(actionEditorPanel), "General");
		
		// add actions tab
		//assignActionsPanel = new AssignActionsPanel();
		//tabbedPane.add(new JScrollPane(assignActionsPanel), "Actions");
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
		
		/*
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
		*/
		
		return panel;
	}
	
	private static final Logger log = LogManager.getLogger(ActionEditorPanel.class.getName());
	
	private JTabbedPane tabbedPane;
	private ActionEditor editor;
	private Component currentEditorComponent;
	private JPanel actionEditorPanel;
	
	private JLabel nameLabel;
	private JTextField nameField;
	//private JLabel delayLabel;
	//private JTextField delayField;

}
