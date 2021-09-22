package com.anthonyeden.jnm.action;

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

import com.anthonyeden.jnm.Action;
import com.anthonyeden.jnm.ActionEditor;
import com.anthonyeden.jnm.event.EditorListener;
import com.anthonyeden.jnm.event.EditorListenerSupport;

/**	Implementation of the ActionEditor interface which defines an
	editor for HttpRequestActions.
	
	@author Anthony Eden
*/

public class HttpRequestActionEditor extends JPanel implements ActionEditor{
	
	public HttpRequestActionEditor(){
		editorListenerSupport = new EditorListenerSupport(this);
		init();
	}
	
	/**	Get the action that is being edited.
	
		@return The Action
	*/
	
	public Action getAction(){
		return action;
	}
	
	/**	Set the action that the editor is editing.
	
		@param action The Action
	*/
	
	public void setAction(Action action){
		if(!(action instanceof HttpRequestAction)){
			throw new IllegalArgumentException("Action must be an HttpRequestAction");
		}
		
		this.action = (HttpRequestAction)action;
		
		revert();
	}
	
	/**	Save the current edits.  Returns true if the save
		was successful.
		
		@return True if the edits were saved
	*/
	
	public boolean save(){
		try{
			action.setURL(urlField.getText());
			action.setMethod(methodComboBox.getSelectedItem().toString());
			action.setUsername(usernameField.getText());
			action.setPassword(passwordField.getPassword());
			action.setUseAuthentication(useAuthenticationCheck.isSelected());
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		editorListenerSupport.fireSaved();
		
		return true;
	}
	
	/**	Revert to the last saved data. */
	
	public void revert(){
		urlField.setText(getString(action.getURL()));
		methodComboBox.setSelectedItem(action.getMethod());
		usernameField.setText(getString(action.getUsername()));
		passwordField.setText(getString(action.getPassword()));
		useAuthenticationCheck.setSelected(action.isUseAuthentication());
		
		setAuthenticationEnabled(action.isUseAuthentication());
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
		methodComboBox.setSelectedItem(HttpRequestAction.DEFAULT_METHOD);
		gbc.weightx = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(methodComboBox, gbc);
		add(methodComboBox);
		
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
	
	private HttpRequestAction action;
	private EditorListenerSupport editorListenerSupport;
	
	private JLabel urlLabel;
	private JTextField urlField;
	private JLabel methodLabel;
	private JComboBox methodComboBox;
	private JCheckBox useAuthenticationCheck;
	private JLabel usernameLabel;
	private JTextField usernameField;
	private JLabel passwordLabel;
	private JPasswordField passwordField;
		
}
