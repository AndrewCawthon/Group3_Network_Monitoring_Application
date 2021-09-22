package com.anthonyeden.jnm.ui;

import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;

import com.anthonyeden.lib.log.Logger;
import com.anthonyeden.lib.log.LogManager;

import com.anthonyeden.jnm.Action;
import com.anthonyeden.jnm.Monitor;
import com.anthonyeden.jnm.JNMClient;
import com.anthonyeden.jnm.MonitorState;

public class AssignActionsPanel extends JPanel{
	
	public AssignActionsPanel(JNMClient client){
		this.client = client;
		init();
	}
	
	public AssignActionsPanel(JNMClient client, Monitor monitor){
		this.client = client;
		this.monitor = monitor;
		init();
		updateActionList();
	}
	
	public Monitor getMonitor(){
		return monitor;
	}
	
	public void setMonitor(Monitor monitor){
		this.monitor = monitor;
	}
	
	public Action getSelectedAction(){
		return (Action)actionList.getSelectedValue();
	}
	
	public void addAction(){
		SelectActionDialog d = new SelectActionDialog(client);
		if(d.showDialog() == SelectActionDialog.APPROVE_OPTION){
			Action action = d.getSelectedAction();
			monitor.getActions(getMonitorState()).add(action);
			log.debug("Adding action: " + action + " to monitor");
		}
	}
	
	public void removeAction(){
		Action action = getSelectedAction();
		if(action != null){
			monitor.getActions(getMonitorState()).remove(action);
		}
	}
	
	public MonitorState getMonitorState(){
		return (MonitorState)monitorStateComboBox.getSelectedItem();
	}
	
	private void updateActionList(){
		if(monitor != null){
			actionList.setModel(monitor.getActions(getMonitorState()));
		}
	}
	
	private void init(){
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		setLayout(gbl);
		
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(1, 1, 1, 1);
		
		monitorStateComboBox = new JComboBox(MonitorState.listAll());
		monitorStateComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				updateActionList();
			}
		});
		monitorStateComboBox.setSelectedItem(MonitorState.ERROR);
		gbc.weightx = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(monitorStateComboBox, gbc);
		add(monitorStateComboBox);
		
		actionList = new JList();
		JScrollPane scrollPane = new JScrollPane(actionList);
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(scrollPane, gbc);
		add(scrollPane);
		
		// add all buttons
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				addAction();
			}
		});
		gbl.setConstraints(addButton, gbc);
		add(addButton);
		
		removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				removeAction();
			}
		});
		gbl.setConstraints(removeButton, gbc);
		add(removeButton);
	}
	
	private static final Logger log = LogManager.getLogger(AssignActionsPanel.class.getName());
	
	private JNMClient client;
	private Monitor monitor;
	
	private JList actionList;
	private JComboBox monitorStateComboBox;
	private JButton addButton;
	private JButton removeButton;
	
}
