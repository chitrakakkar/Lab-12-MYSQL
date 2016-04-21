package com.Chitra;
import java.sql.*;
import java.util.Scanner;
//* This class has a connection to the database created on SQL shell with a user name and password
//* We use statement object to set the connection between the code and the database.

public class Main
{
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";        //Configure the driver needed
    static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/rubik";     //Connection string – where's the database?
    static final String USER = "root";   //TODO replace with your username
    static final String PASSWORD = "password";   //TODO replace with your password

    public static void main(String[] args)
    {
        try
        {
            Class.forName(JDBC_DRIVER);

        }
        catch (ClassNotFoundException cnfe) {
            System.out.println("Can't instantiate driver class; check you have drives and classpath configured correctly?");
            cnfe.printStackTrace();
            System.exit(-1);  //No driver? Need to fix before anything else will work. So quit the program
        }
        Statement statement = null;
        Connection conn = null;
        ResultSet rs = null;
        //use a PreparedStatement object for sending SQL statements to the database
        PreparedStatement stmt = null;
        try
        {
            conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
            statement = conn.createStatement();
            //creating a table
            String createTableCUBE = "CREATE TABLE  if not exists testTable (Cube_Solver varchar(30), Time_Taken double)";
            statement.executeUpdate(createTableCUBE);
            System.out.println("Created rubik table");
            // Inserting values into a table
            String addDataCUBE = "INSERT INTO testTable VALUES ('CubeStormer II robot', 5.270)";
            statement.executeUpdate(addDataCUBE);
            addDataCUBE = "INSERT INTO testTable VALUES ('Fakhri Raihaan', 27.93)";
            System.out.println("Added two rows of data");
            // Updating the table with new data
            statement.executeUpdate(addDataCUBE);
            addDataCUBE = "INSERT INTO testTable VALUES ('Ruxin Liu', 99.33)";
            statement.executeUpdate(addDataCUBE);
            addDataCUBE = "INSERT INTO testTable VALUES ('Mats Valk ', 6.27)";
            statement.executeUpdate(addDataCUBE);
            String fetchAllDataSQL = "SELECT * FROM testTable";

            //this is option -> when you need to fetch all the data from the database
            // Result set is a list which contains table's info.
            rs = statement.executeQuery(fetchAllDataSQL);
            //prints all row and column from the table
            while (rs.next())
            {
                String Solver_Name = rs.getString("Cube_Solver");
                Double BestTime = rs.getDouble("Time_Taken");
                System.out.println("Solver_Name = " + Solver_Name + " Best Time = " + BestTime);

            }
            //Scanner for User  input to add new data
            Scanner scanner = new Scanner(System.in);
            System.out.println("DO you want to add a new Solver and Time (Y/N)?");
            String UserInput = scanner.next();
            // Condition to add new record(could do that in a separate method)
            if(UserInput.equalsIgnoreCase("y"))
            {
                Scanner scanner1 = new Scanner(System.in);
                System.out.println("Enter the solver's name");
                String newName = scanner1.nextLine();
                scanner1.nextLine();
                System.out.println("Enter the best time");
                // two next line written to read the space entered by the user for the name
                Double BestTime = scanner1.nextDouble();
                scanner1.nextLine();
                String sql = "INSERT INTO testTable(Cube_Solver, Time_Taken) VALUES ('" + newName + "','" + BestTime + "')";
                stmt = conn.prepareStatement(sql);
                stmt.executeUpdate(sql);
                System.out.println("A new row has been added");
            }

            System.out.println("Do you want to improve the best time for any solver already in the list(Y/N)?");
            String UserInput2 = scanner.next();
            // To update the Time on the basis of search string input by the user
            if(UserInput2.equalsIgnoreCase("Y"))
            {
                Scanner scanner1 = new Scanner(System.in);
                System.out.println("Enter the solver's name you want to upgrade the time for");
                String Solver_Name = scanner1.nextLine();
                scanner1.nextLine();
                Scanner scanner2 = new Scanner(System.in);
                System.out.println("Input the new Score");
                Double newScore = scanner2.nextDouble();
                scanner2.nextLine();
                String newScoreE = newScore.toString();

                //String sql2 = "UPDATE testTable SET Time_Taken =  newScore  WHERE Cube_Solver = Solver_Name";
                String sql2 = " UPDATE testTable SET Time_Taken = ?" + "Where Cube_Solver = ?";
                PreparedStatement stmt2 = conn.prepareStatement(sql2);
                stmt2.setString(1,newScoreE);
                stmt2.setString(2,Solver_Name);
                stmt2.executeUpdate();
                System.out.println("A new Upgrade has been performed");
            }
            else
            {
                System.out.println("Thanks for your time");
            }

        }
        /// Exception handling
        catch (SQLException se)
        {
            se.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally {
            //A finally block runs whether an exception is thrown or not. Close resources and tidy up whether this code worked or not.
            try {
                if (statement != null) {
                    statement.close();
                    System.out.println("Statement closed");
                }
            } catch (SQLException se){
                //Closing the connection could throw an exception too
                se.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.close();  //Close connection to database
                    System.out.println("Database connection closed");
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("End of program");

    }
}
