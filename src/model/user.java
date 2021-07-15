package model;


import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class user {
	private static String Id;
	private static String Name;
	private static String gender;
	private static int height;
	private static int weight;
	private static int age;
	
	public static String getId() {
		return Id;
	}
	public static void setId(String id) {
		Id = id;
	}
	public static String getName() {
		return Name;
	}
	public static void setName(String name) {
		Name = name;
	}
	public static String getGender() {
		return gender;
	}
	public static void setGender(String gender) {
		user.gender = gender;
	}
	public static int getHeight() {
		return height;
	}
	public static void setHeight(int height) {
		user.height = height;
	}
	public static int getWeight() {
		return weight;
	}
	public static void setWeight(int weight) {
		user.weight = weight;
	}
	public static void setAge(String date) {
		
		//get current date
		LocalDate now = LocalDate.now();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
		LocalDate localDate = LocalDate.parse(date, formatter);
        
		//compare the dates and get the age
        Period period = Period.between(localDate, now);
        int age = period.getYears();
        
		user.age = age;
	}
	public static int getAge() {
		return age;
	}
}
