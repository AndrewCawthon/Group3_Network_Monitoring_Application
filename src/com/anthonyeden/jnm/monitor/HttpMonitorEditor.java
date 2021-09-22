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
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

import com.anthonyeden.jnm.Monitor;
import com.anthonyeden.jnm.MonitorEditor;
import com.anthonyeden.jnm.event.EditorListener;
import com.anthonyeden.jnm.event.EditorListenerSupport;

/**	Implementation of the MonitorEditor interface which defines an
	editor for HttpMonitors.
	
	@author Anthony Eden
*/

public class HttpMonitorEditor extends JPanel implements MonitorEditor{
	
	public HttpMonitorEditor(){
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
		if(!(monitor instanceof HttpMonitor)){
			throw new IllegalArgumentException("Montor must be an HttpMonitor");
		}
		
		this.monitor = (HttpMonitor)monitor;
		
		revert();
	}
	
	/**	Save the current edits.  Returns true if the save
		was successful.
		
		@return True if the edits were saved
	*/
	
	public boolean save(){
		try{
			monitor.setURL(urlField.getText());
			monitor.setMethod(methodComboBox.getSelectedItem().toString());
			monitor.setTimeout(timeoutField.getText());
			monitor.setExpression(expressionField.getText());
			monitor.setUsername(usernameField.getText());
			monitor.setPassword(passwordField.getPassword());
			monitor.setUseAuthentication(useAuthenticationCheck.isSelected());
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		editorListenerSupport.fireSaved();
		
		return true;
	}
	
	/**	Revert to the last saved data. */
	
	public void revert(){
		urlField.setText(getString(monitor.getURL()));
		methodComboBox.setSelectedItem(monitor.getMethod());
		timeoutField.setText(getString(monitor.getTimeout()));
		expressionField.setText(getString(monitor.getExpression()));
		usernameField.setText(getString(monitor.getUsername()));
		passwordField.setText(getString(monitor.getPassword()));
		useAuthenticationCheck.setSelected(monitor.isUseAuthentication());
		
		setAuthenticationEnabled(monitor.isUseAuthentication());
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
	
	protected void setAuthenticationEnabled(boolean authEnabled){
		usernameField.setEnabled(authEnabled);
		passwordField.setEnabled(authEnabled);
	}
	
	/**	Initialize the user interface. */
	
	private void init(){
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		setLayout(gbl);
		
		gbc.insets = new Insets(1, 1, 1, 1);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		urlLabel = new JLabel("URL");
		gbc.weightx = 0;
		gbc.gridwidth = 1;
		gbl.setConstraints(urlLabel, gbc);
		add(urlLabel);
		
		urlField = new JTextField();
		gbc.weightx = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(urlField, gbc);
		add(urlField);
		
		methodLabel = new JLabel("Method");
		gbc.weightx = 0;
		gbc.gridwidth = 1;
		gbl.setConstraints(methodLabel, gbc);
		add(methodLabel);
		
		methodComboBox = new JComboBox(METHODS);
		methodComboBox.setSelectedItem(HttpMonitor.DEFAULT_METHOD);
		gbc.weightx = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(methodComboBox, gbc);
		add(methodComboBox);
		
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
		
		useAuthenticationCheck = new JCheckBox("Use Basic Authentication");
		useAuthenticationCheck.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				setAuthenticationEnabled(useAuthenticationCheck.isSelected());
			}
		});
		gbc.weightx = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(useAuthenticationCheck, gbc);
		add(useAuthenticationCheck);
		
		usernameLabel = new JLabel("Username");
		gbc.weightx = 0;
		gbc.gridwidth = 1;
		gbl.setConstraints(usernameLabel, gbc);
		add(usernameLabel);
		
		usernameField = new JTextField();
		gbc.weightx = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(usernameField, gbc);
		add(usernameField);
		
		passwordLabel = new JLabel("Password");
		gbc.weightx = 0;
		gbc.gridwidth = 1;
		gbl.setConstraints(passwordLabel, gbc);
		add(passwordLabel);
		
		passwordField = new JPasswordField();
		gbc.weightx = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(passwordField, gbc);
		add(passwordField);
		
		JPanel p = new JPanel();
		gbc.weighty = 1;
		gbl.setConstraints(p, gbc);
		add(p);
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
	
	private static final String[] METHODS = {"GET", "POST", "HEAD"};
	
	private HttpMonitor monitor;
	private EditorListenerSupport editorListenerSupport;
	
	private JLabel urlLabel;
	private JTextField urlField;
	private JLabel methodLabel;
	private JComboBox methodComboBox;
	private JLabel timeoutLabel;
	private JTextField timeoutField;
	private JLabel expressionLabel;
	private JTextField expressionField;
	private JCheckBox useAuthenticationCheck;
	private JLabel usernameLabel;
	private JTextField usernameField;
	private JLabel passwordLabel;
	private JPasswordField passwordField;
		
}
