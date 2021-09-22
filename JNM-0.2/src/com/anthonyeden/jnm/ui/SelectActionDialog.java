package com.anthonyeden.jnm.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;

import com.anthonyeden.jnm.Action;
import com.anthonyeden.jnm.JNMClient;

public class SelectActionDialog extends JDialog{
	
	public SelectActionDialog(JNMClient client){
		this.client = client;
		init();
	}
	
	public Action getSelectedAction(){
		return (Action)actionList.getSelectedValue();
	}
	
	public int showDialog(){
		setVisible(true);
		return result;
	}
	
	private void init(){
		actionList = new JList(client.getActions());
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JScrollPane(actionList), BorderLayout.CENTER);
		getContentPane().add(createButtonPanel(), BorderLayout.SOUTH);
		
		getRootPane().setDefaultButton(okButton);
		
		setModal(true);
		setTitle("Select An Action");
		pack();
	}
	
	private JPanel createButtonPanel(){
		JPanel panel = new JPanel();
		
		okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				result = APPROVE_OPTION;
				dispose();
			}
		});
		panel.add(okButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				result = CANCEL_OPTION;
				dispose();
			}
		});
		panel.add(cancelButton);
		
		return panel;
	}
	
	public static final int CANCEL_OPTION = 0;
	public static final int APPROVE_OPTION = 1;
	
	private JNMClient client;
	
	private int result = CANCEL_OPTION;
	
	private JList actionList;
	private JButton okButton;
	private JButton cancelButton;
	
}
