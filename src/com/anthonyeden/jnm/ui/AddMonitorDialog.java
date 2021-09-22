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

import com.anthonyeden.jnm.JNM;
import com.anthonyeden.jnm.JNMClient;
import com.anthonyeden.jnm.MonitorEditor;
import com.anthonyeden.jnm.MonitorDefinition;
import com.anthonyeden.jnm.event.EditorEvent;
import com.anthonyeden.jnm.event.EditorListener;

public class AddMonitorDialog extends JFrame{
	
	public AddMonitorDialog(JNMClient client){
		this.client = client;
		init();
	}
	
	public void accept(){
		if(monitorEditorPanel.save()){
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
		
		setTitle("Monitors");
		
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
		
		monitorLabel = new JLabel("Monitor Type");
		gbc.weightx = 0;
		gbc.gridwidth = 1;
		gbl.setConstraints(monitorLabel, gbc);
		panel.add(monitorLabel);
		
		monitorComboBox = new JComboBox(JNM.getRegisteredMonitors());
		monitorComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				showEditor();
			}
		});
		gbc.weightx = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(monitorComboBox, gbc);
		panel.add(monitorComboBox);
		
		monitorEditorPanel = new MonitorEditorPanel(client);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1;
		gbl.setConstraints(monitorEditorPanel, gbc);
		panel.add(monitorEditorPanel);
		
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
		MonitorDefinition monitorDef = (MonitorDefinition)monitorComboBox.getSelectedItem();
		if(monitorDef == null){
			return;
		}
		
		try{
			editor = monitorDef.getEditorInstance();
			editor.addEditorListener(new EditorListener(){
				public void saved(EditorEvent evt){
					client.startMonitor(editor.getMonitor());
				}
			});
		
			editor.setMonitor(monitorDef.getMonitorInstance());
			monitorEditorPanel.setEditor(editor);
		} catch(Exception e){
			e.printStackTrace();
		}
		
		okButton.setEnabled(true);
	}
	
	private JNMClient client;
	
	private MonitorEditor editor;
	
	private JLabel monitorLabel;
	private JComboBox monitorComboBox;
	private MonitorEditorPanel monitorEditorPanel;
	private JButton okButton;
	private JButton cancelButton;
	
}
		
