package jdes.gdeltedu;

import java.sql.DriverManager;
import java.sql.*;

public class MysqlCon {

	public static void main(String[] args) {
		try {
		Class.forName("com.mysql.jdbc.Driver");
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
		Connection con = null;
		// look up how to store this in a separate file
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gdelt","root","vmuser");
		if (con != null) {
			System.out.println("yay");
		} else {
			System.out.println("not yay");
		}
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	

	}

}
