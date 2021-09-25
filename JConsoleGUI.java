import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;



public class JConsoleGUI implements ActionListener {
	
	JFrame frame;
	
	JButton button;
	JLabel label;
	
	JPanel panel;
	
	public JConsoleGUI() {
		frame = new JFrame();
		
		button = new JButton ("Click me");
		button.addActionListener(this);
		
		label = new JLabel("Clicking on the above link will begin the Monitoring application process");
		
		panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(300,300,100,300));
		panel.setLayout(new GridLayout(0,1));
		panel.add(button);
		panel.add(label);
		
		frame.add(panel,BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Nework Monitoring Application");
		frame.pack();
		frame.setVisible(true);
		
	
		
	}
	

	

	public static void main(String[] args) {
		JConsoleGUI newGUI = new JConsoleGUI();

	}




	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			ProcessBuilder p = new ProcessBuilder("jConsole");
			p.start();
			}
			catch (IOException ex) {
				Logger.getLogger(JConsoleGUI.class.getName()).log(Level.SEVERE, null, ex);
			}
	}

}
