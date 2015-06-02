package service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import models.User;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

@Path(value = "/user")
public class UserService {
	
	@POST
	@Path(value="/newUser")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public User newUser(@FormParam("name")String name, @FormParam("password")String password){
		try {
		    Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		    e.printStackTrace();
		} 
		
		Statement myStatement = null;
		
		try {
			Connection myConn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/simple_rpg_db", "root", "root");
			myStatement = (Statement) myConn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Check if user exist for this name
		try {
			ResultSet existingCharacter = myStatement.executeQuery("SELECT * FROM simple_rpg_db.users WHERE name =\""+name+"\"");
			if(existingCharacter.first()){
				return null;
			}
		
			myStatement.executeUpdate("INSERT INTO characters values ()");
			ResultSet lastInsertedID = myStatement.executeQuery("select last_insert_id() as last_id");
			lastInsertedID.next();
			int id_character = Integer.valueOf(lastInsertedID.getString("last_id"));
			
			SecureRandom random = new SecureRandom();
			String token = new BigInteger(130, random).toString(32);
			System.out.println(">>>>>>>>>>>> name:"+name);
			String userSQL = new String("INSERT INTO users (name,password,token,id_character) values (\""+name+"\",\""+password+"\",\""+token+"\","+id_character+")");
			int newUser = myStatement.executeUpdate(userSQL);
			
			User user = new User();
			user.setName(name);
			user.setToken(token);
			
			return user;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@GET
	@Path(value="/add")
	@Produces(MediaType.APPLICATION_JSON)
	public int add(@QueryParam(value="a")int a, @QueryParam(value="b")int b){
		return a+b;
	}
	
	@POST
	@Path(value="/mult")
	@Produces(MediaType.APPLICATION_JSON)
	public int multiply(@FormParam(value="a")int a, @FormParam(value="b")int b){
		return a+b;
	}
	
	@GET
	@Path(value="/list")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> list(){
		List<String> li = new ArrayList<String>();
		for(int i=0;i<10; i++){
			li.add("ele:"+i);
		}
		
		return li;
	}
}
