package com.anthonyeden.jnm.ui;

import java.awt.Insets;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;

import com.anthonyeden.lib.log.Logger;
import com.anthonyeden.lib.log.LogManager;

import com.anthonyeden.jnm.JNM;
import com.anthonyeden.jnm.JNMClient;
import com.anthonyeden.jnm.Action;
import com.anthonyeden.jnm.ActionEditor;
import com.anthonyeden.jnm.ActionDefinition;
import com.anthonyeden.jnm.event.EditorEvent;
import com.anthonyeden.jnm.event.EditorListener;

public class AddActionDialog extends JFrame{
	
	public AddActionDialog(JNMClient client){
		this.client = client;
		init();
	}
	
	public void accept(){
		if(actionEditorPanel.save()){
			dispose();
		}
	}
	
	public void cancel(){
		dispose();
	}
	
	/**	Initialize the dialog user interface. */
	
	private void init(){
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(createMainPanel(), BorderLayout.CENTER);
		getContentPane().add(createButtonPanel(), BorderLayout.SOUTH);
		
		setTitle("Add Action");
		
		getRootPane().setDefaultButton(okButton);
		
		showEditor();
		
		pack();
		setSize(500, 400);
	}
	
	private JPanel createMainPanel(){
		JPanel panel = new JPanel();
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		panel.setLayout(gbl);
		
		gbc.insets = new Insets(1, 1, 1, 1);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		actionLabel = new JLabel("Action Type");
		gbc.weightx = 0;
		gbc.gridwidth = 1;
		gbl.setConstraints(actionLabel, gbc);
		panel.add(actionLabel);
		
		actionComboBox = new JComboBox(JNM.getRegisteredActions());
		actionComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				showEditor();
			}
		});
		gbc.weightx = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(actionComboBox, gbc);
		panel.add(actionComboBox);
		
		actionEditorPanel = new ActionEditorPanel();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1;
		gbl.setConstraints(actionEditorPanel, gbc);
		panel.add(actionEditorPanel);
		
		return panel;
	}
	
	private JPanel createButtonPanel(){
		JPanel panel = new JPanel();
		
		okButton = new JButton("OK");
		okButton.setEnabled(false);
		okButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				accept();
			}
		});
		panel.add(okButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				cancel();
			}
		});
		panel.add(cancelButton);
		
		return panel;
	}
	
	private void showEditor(){
		ActionDefinition actionDef = (ActionDefinition)actionComboBox.getSelectedItem();
		if(actionDef == null){
			return;
		}
		
		try{
			editor = actionDef.getEditorInstance();
			editor.addEditorListener(new EditorListener(){
				public void saved(EditorEvent evt){
					Action action = editor.getAction();
					action.setId(Long.toString(System.currentTimeMillis()));
					client.getActions().add(action);
				}
			});
		
			editor.setAction(actionDef.getActionInstance());
			actionEditorPanel.setEditor(editor);
		} catch(Exception e){
			e.printStackTrace();
		}
		
		okButton.setEnabled(true);
	}
	
	private static final Logger log = LogManager.getLogger(AddActionDialog.class.getName());
	
	private JNMClient client;
	private ActionEditor editor;
	
	private JLabel actionLabel;
	private JComboBox actionComboBox;
	private ActionEditorPanel actionEditorPanel;
	private JButton okButton;
	private JButton cancelButton;
	
}
		
