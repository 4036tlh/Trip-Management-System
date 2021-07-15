package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane; 
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import model.user; 

public class ListSchedule { 

	public ListSchedule(Vector<String> timetable, Vector<String> description, double price){
		
		String[][] result = new String[timetable.size()][2];

		//combine the 2 vector into 1 string array for further process
		for(int i = 0; i < timetable.size(); i++) {
		   result[i][0] = timetable.get(i);
		   result[i][1] = description.get(i);
		}

		JFrame f = new JFrame(); 
		
		JPanel panels=new JPanel();
		    
		JButton b2=new JButton("Back to Main Page");
		panels.add(b2);
		
		user user = new user();
        String s = user.getName();
		JLabel label = new JLabel("Hi "+ s + ", the total price is RM "+price+", and this is your schedule:");

		String[] col = { "Time", "Description" }; 
	    JTable table = new JTable(result, col); 
		
	    //adding background color to the every two row
	    table.setDefaultRenderer(Object.class, new TableCellRenderer(){
	    	
	    	private Color a = new Color(222,222,222);
            private DefaultTableCellRenderer DEFAULT_RENDERER =  new DefaultTableCellRenderer();
            
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (row%2 == 0){
                    c.setBackground(Color.WHITE);
                }
                else {
                    c.setBackground(a);
                }                        
                return c;
            }

        });
	    
	    JScrollPane scroll = new JScrollPane(table); 
	   
	    f.setTitle("Theme Park Trip Scheduler");
		f.add(label, BorderLayout.NORTH);
		f.add(scroll, BorderLayout.CENTER); 
		f.add(panels, BorderLayout.SOUTH); 
		
		f.setSize(400, 500); 
		f.setVisible(true); 
		
		b2.addActionListener(new ActionListener() {
			        
			public void actionPerformed(ActionEvent arg0) {
						
				f.dispose();
				new mainPage(null);  
			}
		});
		
	} 

} 
