package com.opensourceFramework.utils;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The DBConnection class provides methods for establishing a connection to a MySQL database,
 * executing basic SQL operations such as inserting, deleting, updating, and querying data,
 * and closing the database connection. It is designed for simple database operations using
 * JDBC (Java Database Connectivity).
 *
 * <p>This class manages a connection to the database using static fields for the database driver,
 * connection string, username, and password. It also provides methods to connect, close connections,
 * and perform SQL queries such as inserting, deleting, and counting rows.</p>
 *
 * <p>For simplicity, database credentials and connection details are hardcoded in the class.
 * This is not recommended for production environments due to security concerns, and should
 * be externalized (e.g., via environment variables).</p>
 *
 * Example:
 * <pre>
 *     DBConnection db = new DBConnection("myDatabase");
 *     db.insertData("word", "meaning", "synonym", "antonym");
 * </pre>
 */
public class DBConnection {
    // Database credentials (username, password, and connection string) and driver
    private static String driver = "com.mysql.cj.jdbc.Driver"; // MySQL database driver
    private static String connection = "jdbc:mysql://localhost:3306/"; // JDBC connection URL for the database
    private static String user = "root"; // Username for the database
    private static String password = ""; // Password for the database

    // Static variables for managing connections and statements
    private static Connection con = null;
    private static Statement state = null;
    private static ResultSet result;
    private static PreparedStatement pstate;

    /**
     * Constructor to establish a connection to the specified database.
     *
     * @param dbName the name of the database to connect to
     */
    public DBConnection(String dbName) {
        try {
            // Load the database driver
            Class.forName(driver);
            // Establish a connection to the database using the provided database name
            con = DriverManager.getConnection(connection + dbName, user, password);
            System.out.println("Successfully connected to database.");
        } catch (ClassNotFoundException e) {
            System.err.println("Couldn't load driver.");
        } catch (SQLException e) {
            System.err.println("Couldn't connect to database.");
        }
    }

    /**
     * Main method demonstrating database operations such as connecting, showing data, and closing the connection.
     * Uncomment the relevant lines for other operations (e.g., insert, delete).
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Connect to the MySQL database
        mysqlConnect();
        // Example: Show the data from a table
        showData("surayea");
        // Close the connection
        closeConnection();
    }

    /**
     * Establishes a connection to the MySQL database using the hardcoded credentials and connection URL.
     */
    public static void mysqlConnect() {
        try {
            Class.forName(driver); // Load the MySQL driver
            con = DriverManager.getConnection(connection, user, password); // Connect to the database
            System.out.println("Successfully connected to database.");
        } catch (ClassNotFoundException e) {
            System.err.println("Couldn't load driver.");
        } catch (SQLException e) {
            System.err.println("Couldn't connect to database.");
        }
    }

    /**
     * Closes the database connection if it is open.
     */
    public static void closeConnection() {
        try {
            if (!con.isClosed()) {
                con.close(); // Close the connection to the database
                System.out.println("Database closed successfully.");
            }
        } catch (NullPointerException e) {
            System.err.println("Couldn't load driver.");
        } catch (SQLException e) {
            System.err.println("Couldn't close database.");
        }
    }

    /**
     * Inserts data into the 'dictionary' table in the database.
     *
     * @param word the word to insert
     * @param meaning the meaning of the word
     * @param synonyms the synonyms of the word
     * @param antonyms the antonyms of the word
     */
    public static void insertData(String word, String meaning, String synonyms, String antonyms) {
        try {
            // Using PreparedStatement to execute the insert query
            pstate = con.prepareStatement("INSERT INTO dictionary(word, meaning, synonyms, antonyms) VALUES(?,?,?,?)");
            pstate.setString(1, word);
            pstate.setString(2, meaning);
            pstate.setString(3, synonyms);
            pstate.setString(4, antonyms);
            int value = pstate.executeUpdate(); // Execute the query
            System.out.println("Query OK, 1 row inserted.");
        } catch (SQLException e) {
            System.err.println("Query error.");
        }
    }

    /**
     * Deletes a row from the 'dictionary' table where the word matches the given value.
     *
     * @param word the word to delete
     */
    public static void deleteData(String word) {
        try {
            // Using PreparedStatement to execute the delete query
            pstate = con.prepareStatement("DELETE FROM dictionary WHERE word = ?");
            pstate.setString(1, word);
            int value = pstate.executeUpdate(); // Execute the query
            System.out.println("Query OK, 1 row deleted.");
        } catch (SQLException e) {
            System.err.println("Query error.");
        }
    }

    /**
     * Counts the number of rows in the specified table and prints the result.
     *
     * @param table the name of the table to count rows in
     */
    public static void countRow(String table) {
        try {
            // Execute the count query
            result = state.executeQuery("SELECT COUNT(*) FROM " + table);
            result.next(); // Move to the result row
            int rowcount = result.getInt(1); // Get the row count
            System.out.println("Number of rows: " + rowcount);
        } catch (SQLException e) {
            System.err.println("Query error.");
        }
    }

    /**
     * Displays data from a table in the 'employees' database.
     *
     * @param word the parameter is not used, but part of the method signature
     */
    public static void showData(String word) {
        try {
            state = con.createStatement(); // Create a statement
            result = state.executeQuery("SELECT * FROM employees.employees ORDER BY emp_no DESC LIMIT 1"); // Execute query
            System.out.println(result);
            while (result.next()) { // Iterate through the results
                String empNo = result.getString("emp_no"); // Get the employee number
                System.out.println(empNo);
            }
        } catch (SQLException e) {
            System.err.println("Query error.");
        } catch (NullPointerException e) {
            System.err.println("Element not found.");
        }
    }

    /**
     * Updates the meaning of a word in the 'dictionary' table.
     *
     * @param word the word to update
     * @param meaning the new meaning for the word
     */
    public static void updateData(String word, String meaning) {
        try {
            // Using PreparedStatement to execute the update query
            pstate = con.prepareStatement("UPDATE dictionary SET meaning = ? WHERE word = ?");
            pstate.setString(1, meaning);
            pstate.setString(2, word);
            pstate.executeUpdate(); // Execute the update
            System.out.println("Query OK, 1 row updated.");
        } catch (SQLException e) {
            System.err.println("Query error." + e.getMessage());
        }
    }
}
