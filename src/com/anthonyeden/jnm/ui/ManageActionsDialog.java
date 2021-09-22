package com.anthonyeden.jnm.ui;

import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;

import com.anthonyeden.jnm.Action;
import com.anthonyeden.jnm.JNMClient;

public class ManageActionsDialog extends JFrame{
	
	public ManageActionsDialog(JNMClient client){
		this.client = client;
		init();
	}
	
	public void addAction(){
		AddActionDialog d = new AddActionDialog(client);
		d.setVisible(true);
	}
	
	public void editAction(){
		Action action = getSelectedAction();
		if(action != null){
			ActionPropertiesDialog d = new ActionPropertiesDialog(client);
			d.setAction(action);
			d.setVisible(true);
		}
	}
	
	public void removeAction(){
		Action action = getSelectedAction();
		if(action != null){
			int result = JOptionPane.showConfirmDialog(null, 
				"Are you sure you want to delete the selected Action?",
				"Are You Sure?", JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.YES_OPTION){
				client.getActions().remove(action);
			}
		}
	}
	
	public Action getSelectedAction(){
		return (Action)actionList.getSelectedValue();
	}
	
	private void init(){
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(createMainPanel(), "Center");
		
		setTitle("Actions");
		pack();
		
		setSize(400, 300);
	}
	
	private JPanel createMainPanel(){
		JPanel panel = new JPanel();
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		panel.setLayout(gbl);
		
		gbc.insets = new Insets(1, 1, 1, 1);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		
		actionList = new JList(client.getActions());
		actionList.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent evt){
				if(evt.getClickCount() >= 2){
					editAction();
				}
			}
		});
		JScrollPane actionListSP = new JScrollPane(actionList);
		gbc.gridwidth = 1;
		gbc.gridheight = 4;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbl.setConstraints(actionListSP, gbc);
		panel.add(actionListSP);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0;
		gbc.weighty = 0;
		
		addButton = new JButton("Add...");
		addButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				addAction();
			}
		});
		gbl.setConstraints(addButton, gbc);
		panel.add(addButton);
		
		editButton = new JButton("Edit...");
		editButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				editAction();
			}
		});
		gbl.setConstraints(editButton, gbc);
		panel.add(editButton);
		
		removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				removeAction();
			}
		});
		gbl.setConstraints(removeButton, gbc);
		panel.add(removeButton);
		
		closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				dispose();
			}
		});
		gbc.insets = new Insets(5, 1, 1, 1);
		gbc.anchor = GridBagConstraints.SOUTH;
		gbl.setConstraints(closeButton, gbc);
		panel.add(closeButton);
		
		return panel;
	}
	
	private JNMClient client;
	
	private JList actionList;
	private JButton addButton;
	private JButton editButton;
	private JButton removeButton;
	private JButton closeButton;
	
}
