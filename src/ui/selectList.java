package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

import Controller.controller;
import model.user;

public class selectList {

	selectList(JFrame father, Vector<String> options, Vector<String> process){
		
		controller app = new controller();
		
		Vector<String> v = new Vector<String>();
		v.clear();
		
		JFrame f = new JFrame();

		JPanel panelc=new JPanel();
		JPanel panels=new JPanel();
		
		user user = new user();
        String username = user.getName();
        
		JLabel label = new JLabel("Hi "+ username + ", please select your preferred attributes:");
		
		JButton Next_B=new JButton("Register"); 
		
		JButton Back_B=new JButton();
		if(process==null || process.isEmpty())Back_B.setText("Back to Main Page");
		else Back_B.setText("Back to Previous Page");
		
		panels.add(Next_B);
		panels.add(Back_B);
		
	    JList optionList = new JList(options) ;
	    
	    optionList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
	    JScrollPane scroll = new JScrollPane(optionList);
	    
	    Border border = BorderFactory.createLineBorder(Color.BLACK);
	    Border margin = BorderFactory.createEmptyBorder(10, 10, 10, 10);
	    scroll.setBorder(BorderFactory.createCompoundBorder(margin,border));
		
	    panelc.add(scroll);
	    panelc.setSize(500,250);
	    f.setSize(520,290);
	    	f.add(label, BorderLayout.NORTH);
	        f.add(scroll, BorderLayout.CENTER);
	        f.add(panels, BorderLayout.SOUTH);
	        
	        father.setVisible(false);
	        f.setVisible(true);
	        
	        Next_B.addActionListener(new ActionListener() {
		        
				public void actionPerformed(ActionEvent arg0) {
					
					v.clear();
					
					Object obj[ ] = optionList.getSelectedValues();
					  for(int i = 0; i < obj.length; i++)
					  {
						 v.add((String) obj[i]);
					  }
					  
					  if(v.isEmpty()) {
						  JOptionPane.showMessageDialog(f, "Please select minimum 1 attribute.", "Theme Park Trip Scheduler", JOptionPane.WARNING_MESSAGE);
					  }
					
					  else {
						  if(process==null || process.isEmpty()) {
								
								 new selectList(f,app.submit(v), v); // means it is in confirmation step
								 f.dispose();
							}
							else{
								int reply = JOptionPane.showConfirmDialog(f,
				        			    "Are you sure you want to visit these "+ v.size() +" attarctions?",
										"Theme Park Trip Scheduler",
				        			    JOptionPane.YES_NO_OPTION,
				        			    JOptionPane.QUESTION_MESSAGE);   
									
									if(reply == JOptionPane.YES_OPTION) {
										app.schedule(v);
										f.dispose();
									}
							}
					  }
				}
		      });
			
			Back_B.addActionListener(new ActionListener() {
		        
				public void actionPerformed(ActionEvent arg0) {
					
					if(process==null || process.isEmpty()) {
						new mainPage(null);    
						f.dispose();
					}
					else {
						
						Vector<String> options =  app.getOption();
						
						new selectList(father, options, null);
						f.dispose();
					}
				}
			});
	}
 
}
