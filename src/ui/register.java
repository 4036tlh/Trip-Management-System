package ui;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import Controller.controller;

public class register extends JFrame {

	register(JFrame father, String customerID){  
		
		JFrame f=new JFrame("Theme Park Trip Scheduler"); 
					
		JButton b=new JButton("Register");    
		JButton b2=new JButton("Back to Main Page");    

		JPanel panelc=new JPanel();
		panelc.setLayout(new GridLayout(5, 2));
		
		JPanel panels=new JPanel();
		panels.add(b);
		panels.add(b2);
		
		JLabel label1 = new JLabel("Name: ", JLabel.RIGHT);	
		JLabel label2 = new JLabel("Date of birth(dd/mm/yy): ", JLabel.RIGHT);	
		JLabel label3 = new JLabel("Gender: ", JLabel.RIGHT);	
		JLabel label4 = new JLabel("Height(cm): ", JLabel.RIGHT);	
		JLabel label5 = new JLabel("Weight(kg): ", JLabel.RIGHT);	
		
		JTextField input1= new JTextField();
		JTextField input2= new JTextField();
		
		String[] gender = {"Male", "Female"};
		JComboBox input3= new JComboBox(gender);

		JTextField input4= new JTextField();
		JTextField input5= new JTextField();
		
		panelc.add(label1);
		panelc.add(input1);
		
		panelc.add(label2);
		panelc.add(input2);
		
		panelc.add(label3);
		panelc.add(input3);

		panelc.add(label4);
		panelc.add(input4);
		
		panelc.add(label5);
		panelc.add(input5);
		
		f.add(panelc, BorderLayout.CENTER);
		f.add(panels, BorderLayout.SOUTH);
		
		f.setSize(600,250);    
		f.setVisible(true);    
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   


		b.addActionListener(new ActionListener() {
	        
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					
					if(input1.getText().isEmpty() || input2.getText().isEmpty() ||input4.getText().isEmpty() ||input5.getText().isEmpty()) {
			    		JOptionPane.showMessageDialog(f,
			    			    "Please modify these following values:\n-Name is required.\n-Date is required.\n-Height is required.\n-Weight is required.",
			    			    "Theme Park Trip Scheduler",
			    			    JOptionPane.WARNING_MESSAGE);   		
			    	}
					else {
						
						SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy");    

						String name = input1.getText();
						String date = input2.getText();
						
						try {
							formatter1.setLenient(false);
							formatter1.parse(date);
					    } 
						catch (DateTimeParseException | NullPointerException e) {
					    	throw new IllegalArgumentException();
					    }

						String gender =String.valueOf(input3.getItemAt(input3.getSelectedIndex()));
						int height = Integer.parseInt(input4.getText());
						int weight = Integer.parseInt(input5.getText());
						
						SimpleDateFormat inSDF = new SimpleDateFormat("dd/mm/yyyy");
						SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-mm-dd");

						Date indate = inSDF.parse(date);
			            String outDate = outSDF.format(indate);
			            
						if(height < 1 || weight < 1) {
							throw new IllegalArgumentException();
						}
						
						controller app = new controller();
						app.insert(customerID, name, outDate, gender, height, weight);
						
						Vector<String> options =  app.getOption();
						
						f.dispose();
						new selectList(father, options, null);
					}
				}catch(IllegalArgumentException e) {
					JOptionPane.showMessageDialog(f, "Input not valid", "Theme Park Trip Scheduler", JOptionPane.WARNING_MESSAGE);
			    	
				}catch (ParseException e) {
					JOptionPane.showMessageDialog(f, "Input not valid", "Theme Park Trip Scheduler", JOptionPane.WARNING_MESSAGE);
			    }
			}
	    });
		
		b2.addActionListener(new ActionListener() {
	        
			public void actionPerformed(ActionEvent arg0) {
				f.dispose();
				father.setVisible(true);
			}
		});
	}         
}
