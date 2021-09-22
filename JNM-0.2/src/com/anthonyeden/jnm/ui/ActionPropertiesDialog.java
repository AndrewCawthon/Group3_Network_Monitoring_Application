package com.anthonyeden.jnm.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

import com.anthonyeden.jnm.Action;
import com.anthonyeden.jnm.JNMClient;

public class ActionPropertiesDialog extends JFrame{
	
	public ActionPropertiesDialog(JNMClient client){
		this.client = client;
		init();
	}
	
	public void setAction(Action action){
		try{
			this.action = action;
			actionEditorPanel.setEditor(action.getActionEditor());
		} catch(Exception e){
			JNMWindow.error(e);
		}
	}
	
	public boolean save(){
		boolean saveResult = actionEditorPanel.save();
		if(saveResult){
			client.setModified(true);
		}
		return saveResult;
	}
	
	public void close(){
		dispose();
	}
	
	private void init(){
		actionEditorPanel = new ActionEditorPanel();
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(actionEditorPanel, BorderLayout.CENTER);
		getContentPane().add(createButtonPanel(), BorderLayout.SOUTH);
		
		setTitle("Action Properties");
		
		getRootPane().setDefaultButton(closeButton);
		
		pack();
		setSize(600, 400);
	}
	
	private JPanel createButtonPanel(){
		JPanel panel = new JPanel();
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				save();
			}
		});
		panel.add(saveButton);
		
		saveAndCloseButton = new JButton("Save and Close");
		saveAndCloseButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				if(save()){
					close();
				}
			}
		});
		panel.add(saveAndCloseButton);
		
		closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				close();
			}
		});
		panel.add(closeButton);
		
		return panel;
	}
	
	private JNMClient client;
	private Action action;
	private ActionEditorPanel actionEditorPanel;
	
	private JButton saveButton;
	private JButton saveAndCloseButton;
	private JButton closeButton;
	
}
