package com.opensourceFramework.utils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DBMapper is a utility class that helps execute a SQL query and map its result
 * set to a list of objects of a specified class type.
 */
public class DBMapper {

    /**
     * Executes the provided SQL query and maps the result to a list of objects
     * of the provided class type.
     *
     * @param <T>       The generic type of the class to map the result to.
     * @param type      The class type to map each row of the result set.
     * @param query     The SQL query to execute.
     * @param dbName    The name of the database.
     * @param hostName  The database server's host name.
     * @return          A list of objects of the provided class type, each representing
     *                  a row in the result set.
     * @throws SQLException If an SQL error occurs during execution.
     */
    public <T> List<T> selectQueryAndMapToClass(Class<T> type, String query, String dbName, String hostName) throws SQLException {
        // List to hold mapped objects from the result set
        List<T> list = new ArrayList<>();

        // Open a database connection
        try (Connection conn = (Connection) new DBConnection(dbName)) {
            // Create a statement to execute the query
            try (Statement stmt = conn.createStatement()) {
                // Execute the query and get the result set
                try (ResultSet rst = stmt.executeQuery(query)) {
                    // For each row in the result set, create an instance of the class and map data
                    while (rst.next()) {
                        T t = type.newInstance(); // Create a new instance of the provided class
                        loadResultSetIntoObject(rst, t); // Map the result set to the class object
                        list.add(t); // Add the object to the list
                    }
                }
                // Close the connection after use
                conn.close();
            } catch (InstantiationException | IllegalAccessException e) {
                // Handle errors related to object creation and mapping
                throw new RuntimeException("Unable to get the records : " + e.getMessage(), e);
            }
        }
        return list; // Return the list of mapped objects
    }

    /**
     * Maps the fields of an object to the corresponding columns in the result set.
     *
     * @param rst     The result set from the SQL query.
     * @param object  The object whose fields will be populated with data from the result set.
     * @throws IllegalAccessException  If field access is not allowed.
     * @throws SQLException            If an SQL error occurs.
     */
    public static void loadResultSetIntoObject(ResultSet rst, Object object)
            throws IllegalAccessException, IllegalArgumentException, SQLException {
        // Get the class of the provided object
        Class<?> zclass = object.getClass();

        // Loop through all fields (variables) of the class
        for (Field field : zclass.getDeclaredFields()) {
            field.setAccessible(true); // Allow access to private fields
            DBTable column = field.getAnnotation(DBTable.class); // Get the database column name via annotation
            Object value = rst.getObject(column.columnName()); // Get the value from the result set

            // Check if the field is of a primitive type (like int, double, etc.)
            Class<?> type = field.getType();
            if (isPrimitive(type)) {
                // Convert primitive types to their wrapper objects (like int to Integer)
                Class<?> boxed = boxPrimitive(type);
                value = boxed.cast(value); // Cast the value to the boxed type
            }
            // Set the field in the object to the value from the result set
            field.set(object, value);
        }
    }

    /**
     * Checks if the provided class type is a primitive (int, double, etc.).
     *
     * @param type  The class type to check.
     * @return      True if the class is a primitive type, false otherwise.
     */
    public static boolean isPrimitive(Class<?> type) {
        return (type == int.class || type == long.class ||
                type == double.class || type == float.class ||
                type == boolean.class || type == byte.class ||
                type == char.class || type == short.class);
    }

    /**
     * Converts a primitive class type to its corresponding boxed (wrapper) type.
     *
     * @param type  The primitive class type (like int.class).
     * @return      The corresponding wrapper class (like Integer.class).
     * @throws IllegalArgumentException If the provided type is not a primitive.
     */
    public static Class<?> boxPrimitive(Class<?> type) {
        if (type == int.class) {
            return Integer.class;
        } else if (type == long.class) {
            return Long.class;
        } else if (type == double.class) {
            return Double.class;
        } else if (type == float.class) {
            return Float.class;
        } else if (type == boolean.class) {
            return Boolean.class;
        } else if (type == byte.class) {
            return Byte.class;
        } else if (type == char.class) {
            return Character.class;
        } else if (type == short.class) {
            return Short.class;
        } else {
            throw new IllegalArgumentException("class '" + type.getName() + "' is not a primitive");
        }
    }
}
