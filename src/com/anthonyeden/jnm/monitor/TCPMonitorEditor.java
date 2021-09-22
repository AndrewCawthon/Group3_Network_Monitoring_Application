package com.anthonyeden.jnm.monitor;

import java.awt.Insets;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;

import com.anthonyeden.jnm.Monitor;
import com.anthonyeden.jnm.MonitorEditor;
import com.anthonyeden.jnm.event.EditorListener;
import com.anthonyeden.jnm.event.EditorListenerSupport;

/**	Implementation of the MonitorEditor interface which defines an
	editor for TCPMonitors.
	
	@author Anthony Eden
*/

public class TCPMonitorEditor extends JPanel implements MonitorEditor{
	
	public TCPMonitorEditor(){
		editorListenerSupport = new EditorListenerSupport(this);
		init();
	}
	
	/**	Get the monitor that is being edited.
	
		@return The Monitor
	*/
	
	public Monitor getMonitor(){
		return monitor;
	}
	
	/**	Set the monitor that the editor is editing.
	
		@param monitor The Monitor
	*/
	
	public void setMonitor(Monitor monitor){
		if(!(monitor instanceof TCPMonitor)){
			throw new IllegalArgumentException("Montor must be an TCPMonitor");
		}
		
		this.monitor = (TCPMonitor)monitor;
		
		revert();
	}
	
	/**	Save the current edits.  Returns true if the save
		was successful.
		
		@return True if the edits were saved
	*/
	
	public boolean save(){
		try{
			monitor.setHost(hostField.getText());
			monitor.setPort(portField.getText());
			monitor.setTimeout(timeoutField.getText());
			monitor.setExpression(expressionField.getText());
			monitor.setRequestData(requestArea.getText());
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		editorListenerSupport.fireSaved();
		
		return true;
	}
	
	/**	Revert to the last saved data. */
	
	public void revert(){
		hostField.setText(getString(monitor.getHost()));
		portField.setText(getString(monitor.getPort()));
		timeoutField.setText(getString(monitor.getTimeout()));
		expressionField.setText(getString(monitor.getExpression()));
		requestArea.setText(monitor.getRequestData());
	}
	
	public Component getComponent(){
		return this;
	}
	
	public void addEditorListener(EditorListener l){
		editorListenerSupport.addEditorListener(l);
	}
	
	public void removeEditorListener(EditorListener l){
		editorListenerSupport.removeEditorListener(l);
	}
	
	/**	Initialize the user interface. */
	
	private void init(){
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		setLayout(gbl);
		
		gbc.insets = new Insets(1, 1, 1, 1);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		hostLabel = new JLabel("Host");
		gbc.weightx = 0;
		gbc.gridwidth = 1;
		gbl.setConstraints(hostLabel, gbc);
		add(hostLabel);
		
		hostField = new JTextField();
		gbc.weightx = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(hostField, gbc);
		add(hostField);
		
		portLabel = new JLabel("Port");
		gbc.weightx = 0;
		gbc.gridwidth = 1;
		gbl.setConstraints(portLabel, gbc);
		add(portLabel);
		
		portField = new JTextField();
		gbc.weightx = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(portField, gbc);
		add(portField);
		
		timeoutLabel = new JLabel("Timeout (milliseconds)");
		gbc.weightx = 0;
		gbc.gridwidth = 1;
		gbl.setConstraints(timeoutLabel, gbc);
		add(timeoutLabel);
		
		timeoutField = new JTextField();
		gbc.weightx = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(timeoutField, gbc);
		add(timeoutField);
		
		expressionLabel = new JLabel("Expression");
		gbc.weightx = 0;
		gbc.gridwidth = 1;
		gbl.setConstraints(expressionLabel, gbc);
		add(expressionLabel);
		
		expressionField = new JTextField();
		gbc.weightx = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(expressionField, gbc);
		add(expressionField);
		
		requestArea = new JTextArea();
		JScrollPane requestAreaSP = new JScrollPane(requestArea);
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(requestAreaSP, gbc);
		add(requestAreaSP);
	}
	
	private String getString(Object object){
		if(object == null){
			return "";
		} else {
			return object.toString();
		}
	}
	
	private String getString(int value){
		return new Integer(value).toString();
	}
	
	private TCPMonitor monitor;
	private EditorListenerSupport editorListenerSupport;
	
	private JLabel hostLabel;
	private JTextField hostField;
	private JLabel portLabel;
	private JTextField portField;
	private JLabel timeoutLabel;
	private JTextField timeoutField;
	private JLabel expressionLabel;
	private JTextField expressionField;
	private JTextArea requestArea;
		
}
