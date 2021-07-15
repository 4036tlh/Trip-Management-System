package Controller;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JFrame;
import model.user;
import ui.ListSchedule;
import java.util.*; 

public class controller {
	
	//connect the database
    private Connection connect() {

    	String url = "jdbc:mysql://localhost:3306/trip";
        String userName = "root";
        String password = "";
        
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url,userName,password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
 
   //used login
    public int checkId(JFrame f, String CustomerID){
    	  
        String sql = "SELECT * FROM customer where CustomerID='" + CustomerID + "';";
       
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            if (rs.next()) {
            	
            	//store the things into user model as session
            	user user = new user();
                user.setId(CustomerID);
                user.setName(rs.getString("CustomerName"));
                user.setAge(rs.getString("DateOfBirth"));
                user.setHeight(rs.getInt("Height"));
                user.setWeight(rs.getInt("Weight"));
                
	            
	            return 1;    
            }
            else {
            	return 0;
            	}
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        	}
      
    	return 0;
    }
   
    //Register of a new user
    public void insert(String customerID, String name, String date, String gender, int height, int weight) {
    	
    	String sql = "INSERT INTO Customer( CustomerID, CustomerName, DateOfBirth, Gender, Weight, Height) "
    				+ "VALUES ('"+ customerID + "', '"+name + "', '"+date + "','"+ gender + "','"+ height + "','"+ weight + "')";
	       
        Connection connection = null;
        Statement stmt = null;
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/trip", "root", null);
             
            stmt = connection.createStatement();
            stmt.execute(sql);
            
            //store the things into user model as session
        	user user = new user();
            user.setId(customerID);
            user.setName(name);
            user.setAge(date);
            user.setHeight(height);
            user.setWeight(weight);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //it will return the available option of attraction of the current user
    public  Vector<String> getOption(){
    	
    	user user = new user();
    	String sql = "select * from Attraction"
    				+ " where  MinimumAge<=" + user.getAge() + " and MinimumHeight<="+user.getHeight()+" and MinimumWeight<="+user.getWeight()+";";

    	Vector<String> option= new  Vector<String>();
    	 
    	try (Connection conn = this.connect();
                Statement stmt  = conn.createStatement();
                ResultSet rs    = stmt.executeQuery(sql)){
               
	               	while(rs.next()) {
	            	   option.add(rs.getString("AttractionName"));
	               	}
          		} catch (SQLException e) {
					e.printStackTrace();
				}
    	return option;
    }
    
    //after first submission, the data come in and output with price
    public Vector<String> submit(Vector<String> choices) {

    	Vector<String> confirm = new Vector<String>();
    	System.out.println(choices);
    	
    	//use the INSTR commend to retrieve the attraction from database
    	String sql="select * from Attraction "
    			+ "where INSTR(\""+choices+"\", CONCAT('[', AttractionName))<>0 "
    			+ " or INSTR(\""+choices+"\",CONCAT(', ', AttractionName))<>0 ;" ;
    			
		try (
				Connection conn = this.connect();
		        Statement stmt  = conn.createStatement();
		        ResultSet rs    = stmt.executeQuery(sql)){
		       
		       while(rs.next()) {
		    	   
		    	   confirm.add(rs.getString("AttractionName") + " (RM " + rs.getString("Price") + ")");
		    	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return confirm;
	}
    
    //after second submission and confirmation, retrieve the data and make the place arrange in shortest distance and show it
    public void schedule(Vector<String> choices) {
    	
    	//set the default position 
    	int x=0;
    	int y=0;
    	int z=0;
    	
    	//set as getting hour and minute only
    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");  
    	//get current time
    	LocalDateTime time = LocalDateTime.now(); 
    	
    	String time_s;		//time_s will be the starting time of each process
    	String time_e;		//time_e is the ending time of the process
    	
    	double price = 0;
    	
    	Vector<String> timetable = new Vector<String>();		//it record the time
    	Vector<String> description = new Vector<String>();		//it record the action
    															
    	time_s = dtf.format(time);
    	
    	//start of trip
 	    timetable.add(time_s);
 	    description.add("Start trip");
    	
 	    while(!choices.isEmpty()) {	
 	    	
    		//getting from the database and rearrange it based on distance with the newest position in ascending order
	    	String sql = "select *, power(power(LocationX-"+x+",2)+power(LocationY-"+y+",2)+power(LocationZ-"+z+",2),1/2) distance" + 
	    					" from Attraction " +
	    					" where INSTR(\""+choices+"\",CONCAT('[', AttractionName))<>0" +
	    					" or INSTR(\""+choices+"\",CONCAT(', ', AttractionName))<>0" +
	    					" group by AttractionName order by distance";

	    	try (
	    			Connection conn = this.connect();
	                Statement stmt  = conn.createStatement();
	                ResultSet rs    = stmt.executeQuery(sql)){
	               
	    			//get only the first line where it has the shortest distance with current position
		    		if (rs.next()) {
	            	   
		    			//adding into database (the table EntryRecord)
		    		   entryRecord(rs.getString("AttractionID"));
		    			
	            	   price += rs.getDouble("Price");
	            	   
	            	   //remove the attraction from the input data(choice) after it has been recorded into the timetable
	            	   String rmv = rs.getString("AttractionName") + " (RM " + rs.getDouble("Price") + "0)";
	            	   choices.remove(rmv);

	            	   //update current position
	            	   x = rs.getInt("LocationX");
	            	   y = rs.getInt("LocationY");
	            	   z = rs.getInt("LocationZ");
	            	   
	            	   //calculate the time used for going to the closest attraction
	            	   double path = (rs.getInt("distance")/60);
	            	   
	            	   //update the time after the process
	            	   time_s = dtf.format(time);
	            	   time = time.plusMinutes((long) path);
	            	   time_e = dtf.format(time);
	            	   
	            	   //record the time used, duration and on working process
	            	   timetable.add(time_s+" - "+time_e);
	            	   description.add("Go to " + rs.getString("AttractionName"));
	            	   
	            	   time_s = dtf.format(time);
	            	   time = time.plusMinutes(rs.getInt("Duration"));
	            	   time_e = dtf.format(time);
	            	   
	            	   timetable.add(time_s+" - "+time_e);
	            	   description.add("At " + rs.getString("AttractionName"));
		    		}
	               
		    	} 
	    		catch (SQLException e) {
		    		e.printStackTrace();
				}
    	}
    	
    	//end of trip
        timetable.add(time_s);
        description.add("End Trip");
        
        //show the schedule
    	new ListSchedule(timetable, description, price);
    }
    
    //insert data into EntryRecord
    public void entryRecord(String record) {
    	
    	user user = new user();
    	String customerID = user.getId();
    	
    	//get current date
    	String date = LocalDate.now().toString();
    	
    	String sql = "INSERT INTO entryrecord(AttractionID, CustomerID, EntryDate) "
				+ "VALUES ('"+ record + "', '"+ customerID + "', '"+ date + "')";
       
	    Connection connection = null;
	    Statement stmt = null;
	    try
	    {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/trip", "root", null);
	         
	        stmt = connection.createStatement();
	        stmt.execute(sql);
	        
	    } 
	    catch (Exception e) {
	        e.printStackTrace();
	    }
    	
    }
}