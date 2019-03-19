package edu.ycp.cs320.booksdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

import edu.ycp.cs320.sqldemo.DBUtil;

public class InsertNewBookWithAuthor {

	public static void main(String[] args) throws Exception {
		
		int id=0;
		String isbn;
		String year;
		String title;
		String firstname;
		String lastname;
		
		
		// load Derby JDBC driver
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Exception e) {
			System.err.println("Could not load Derby JDBC driver");
			System.err.println(e.getMessage());
			System.exit(1);
		}

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		
		
		
		
		
		// connect to the database
		conn = DriverManager.getConnection("jdbc:derby:test.db;create=true");
	
	
		

		@SuppressWarnings("resource")
		Scanner keyboard = new Scanner(System.in);
		//INITIAL ATTEMPT TO FIND AUTHOR ID 
		try {
			conn.setAutoCommit(true);
			
			// prompt user for title to search for
			System.out.print("Author's first name to insert: ");
			firstname = keyboard.nextLine();
			
			System.out.print("Author's last name to insert: ");
			lastname = keyboard.nextLine();
			
			System.out.print("Title to insert: ");
			title = keyboard.nextLine();
			
			System.out.print("ISBN to insert: ");
			isbn = keyboard.nextLine();
			
			System.out.print("Year to insert: ");
			year = keyboard.nextLine();

			// a canned query to find book information (including author name) from title
			stmt = conn.prepareStatement(
					"select authors.author_id "
					+ "  from authors "
					+ "  where authors.firstname = ? and authors.lastname = ? "
			);
			
			// substitute the title entered by the user for the placeholder in the query
			stmt.setString(1, firstname);
			stmt.setString(2, lastname);

			// execute the query
			resultSet = stmt.executeQuery();

			// get the precise schema of the tuples returned as the result of the query
			ResultSetMetaData resultSchema = stmt.getMetaData();

			// iterate through the returned tuples, printing each one
			// count # of rows returned
			int rowsReturned = 0;
			
			while (resultSet.next()) {
				for (int i = 1; i <= resultSchema.getColumnCount(); i++) {
					Object obj = resultSet.getObject(i);
					id= (int)obj ;
					System.out.println("Author Found");
				}
				
				
				// count # of rows returned
				rowsReturned++;
			}
			
			// indicate if the query returned nothing
			if (rowsReturned == 0) {
				System.out.println("No rows returned that matched the author");
				
			}
		} finally {
			// close result set, statement, connection
			DBUtil.closeQuietly(resultSet);
			DBUtil.closeQuietly(stmt);
			DBUtil.closeQuietly(conn);
		}
		
	// AUTHOR NOT FOUND --- CREATE AUTHOR AND GET ID --- PART 2 OF LAB 5	
	if(id==0) {
		// reopen conn
		conn = DriverManager.getConnection("jdbc:derby:test.db;create=true");
		try {
		conn.setAutoCommit(true);

		// a canned query to find book information (including author name) from title
		stmt = conn.prepareStatement(
				"insert into authors ( firstname , lastname  ) "
				+ "  values ( ? , ?  )"
		);
		
		// substitute the title entered by the user for the placeholder in the query
		stmt.setString(1, firstname);
		stmt.setString(2, lastname);
		

		// execute the query
		stmt.executeUpdate();

		// get the precise schema of the tuples returned as the result of the query

		System.out.println("Author Made");
		
	} finally {
		// close statement, connection
		
		DBUtil.closeQuietly(stmt);
		DBUtil.closeQuietly(conn);
	}
		//SECOND ATTEMPT TO GET AUTHOR ID
		conn = DriverManager.getConnection("jdbc:derby:test.db;create=true");
		try {
			conn.setAutoCommit(true);
			
			// a canned query to find book information (including author name) from title
						stmt = conn.prepareStatement(
								"select authors.author_id "
								+ "  from authors "
								+ "  where authors.firstname = ? and authors.lastname = ? "
						);
						
						// substitute the title entered by the user for the placeholder in the query
						stmt.setString(1, firstname);
						stmt.setString(2, lastname);

						// execute the query
						resultSet = stmt.executeQuery();

						// get the precise schema of the tuples returned as the result of the query
						ResultSetMetaData resultSchema = stmt.getMetaData();

						// iterate through the returned tuples, printing each one
						// count # of rows returned
						int rowsReturned = 0;
						
						while (resultSet.next()) {
							for (int i = 1; i <= resultSchema.getColumnCount(); i++) {
								Object obj = resultSet.getObject(i);
								id= (int)obj ;
								System.out.println("Author Found");
							}
							
							
							// count # of rows returned
							rowsReturned++;
						}
						
						// indicate if the query returned nothing
						if (rowsReturned == 0) {
							System.out.println("No rows returned that matched the author");
							
						}
		}finally {
			// close result set, statement, connection
			DBUtil.closeQuietly(resultSet);
			DBUtil.closeQuietly(stmt);
			DBUtil.closeQuietly(conn);
		}
	}

		
	if( id != 0){	
		conn = DriverManager.getConnection("jdbc:derby:test.db;create=true");
		try {
			conn.setAutoCommit(true);

			// a canned query to find book information (including author name) from title
			stmt = conn.prepareStatement(
					"insert into books ( author_id , title , isbn , published ) "
					+ "  values ( ? , ? , ? , ? )"
			);
			
			// substitute the title entered by the user for the placeholder in the query
			stmt.setInt(1, id);
			stmt.setString(2, title);
			stmt.setString(3, isbn);
			stmt.setString(4, year);

			// execute the query
			stmt.executeUpdate();

			// get the precise schema of the tuples returned as the result of the query

			System.out.println("Success");
			
		} finally {
			// close result set, statement, connection
			
			DBUtil.closeQuietly(stmt);
			DBUtil.closeQuietly(conn);
		}
	}
	else {
		System.out.println("Author was not found and not created");
	}
		

	}

}
