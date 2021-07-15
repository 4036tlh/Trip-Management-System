package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Controller.controller;

public class mainPage {
	
	mainPage(JFrame father){  
		
		JFrame f=new JFrame("Theme Park Trip Scheduler"); 
					
		JButton b=new JButton("Submit");    
		b.setBounds(200,40,90,30);  
		
		
		JLabel label = new JLabel();
		label.setText("CustomerID :");
		label.setBounds(150, 0, 100, 30);
		
		JTextField InputID= new JTextField();
		InputID.setBounds(250, 0, 250, 30);
					
		f.add(label);
		f.add(InputID);
		
		f.add(b);    
		
		f.setSize(500,150);    
		f.setLayout(null);    
		f.setVisible(true);    
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
		
		b.addActionListener(new ActionListener() {
	        
			public void actionPerformed(ActionEvent arg0) {
				
				String loginID = InputID.getText();
				
				controller app = new controller();
				
				if(loginID.isEmpty()) {
		    		JOptionPane.showMessageDialog(f, "CustormerID is required", "Theme Park Trip Scheduler", JOptionPane.WARNING_MESSAGE);
		    	}
		    	else
		    	{	int reply;
		    		
					if( app.checkId(f, loginID) == 1) {
						Vector<String> options =  app.getOption();
						new selectList(f, options, null);
						
					}
					else {
						reply = JOptionPane.showConfirmDialog(f,
									"Your record is currently not available in our system.Would you like to register as our new customer?",
									"Theme Park Trip Scheduler",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE);   
						
						if(reply == JOptionPane.YES_OPTION) {
							new register(f,loginID);
						}
						else f.setVisible(true);
					}
		    	} 
			} 
			
	      });
		
		
	}
		
	public static void main(String[] args) {    

		new mainPage(null);    
	}       
}
