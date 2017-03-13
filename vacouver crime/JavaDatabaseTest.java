import java.sql.*;

public class JavaDatabaseTest
{
 /**
  * Carries out various SQL operations after establishing the
  * database connection.
  */
 public static void main (String args[])
 {
  Connection conn = null;
  try
  {
   // Loads the class object for the Oracle driver into the DriverManager.
   Class.forName("oracle.jdbc.driver.OracleDriver");

   // Attempt to establish a connection to the specified database via the
   // DriverManager
    conn = DriverManager.getConnection("jdbc:oracle:thin:@studentdb.tru.ca:1521:btacs2", "your_user_id", "Your_password");

   // Check the connection
   if (conn != null)
   {
    System.out.println("Java program has been connected to TRU Oracle database!");

    // Create the table and show the table columns
    Statement stmt = conn.createStatement();
    boolean result = stmt.execute("CREATE TABLE EMP " +
      " (EMPNO NUMERIC(4) NOT NULL, " +
      "  ENAME VARCHAR(10), " +
      "  JOB VARCHAR(9))");

    System.out.println("\tTable creation result: " + result);       
    JavaDatabaseTest.showColumns(conn);

    // Insert the data into the database and show the values in the table
    Statement stmt2 = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
      ResultSet.CONCUR_UPDATABLE);
    int rowCount = stmt2.executeUpdate("INSERT INTO EMP VALUES " +
      "(7369, 'SMITH', 'CLERK')");
    JavaDatabaseTest.showValues(conn);

    // Close the database
    conn.close();
   }

  } catch (SQLException ex) {
   System.out.println("SQLException: " + ex.getMessage());
   ex.printStackTrace();
  } catch (Exception ex) {
   System.out.println("Exception: " + ex.getMessage());
   ex.printStackTrace();
  }
 }

 /**
  * Obtains and displays a ResultSet from the EMP table.
  */
 public static void showValues(Connection conn)
 {
  try
  {
   Statement stmt = conn.createStatement();
   ResultSet rset = stmt.executeQuery("SELECT * FROM EMP");
   JavaDatabaseTest.showResults("EMP", rset);
  } catch (SQLException ex) {
   System.out.println("SQLException: " + ex.getMessage());
   ex.printStackTrace();
  }
 }

 /**
  * Displays the columns of the EMP table.
  */
 public static void showColumns(Connection conn)
 {
  try
  {
   Statement stmt = conn.createStatement();
   ResultSet rset = stmt.executeQuery("SELECT COLUMN_NAME " +
     "FROM ALL_TAB_COLUMNS " +
     "WHERE TABLE_NAME='EMP'");
   JavaDatabaseTest.showResults("EMP", rset);
  } catch (SQLException ex) {
   System.out.println("SQLException: " + ex.getMessage());
   ex.printStackTrace();
  }
 }

 /**
  * Displays the contents of the specified ResultSet.
  */
 public static void showResults(String tableName, ResultSet rSet)
 {
  try
  {
   ResultSetMetaData rsmd = rSet.getMetaData();
   int numColumns = rsmd.getColumnCount();
   String resultString = null;
   if (numColumns > 0)
   {
    resultString = "\nTable: " + tableName + "\n" +
      "=======================================================\n";
    for (int colNum = 1; colNum <= numColumns; colNum++)
     resultString += rsmd.getColumnLabel(colNum) + "     ";
   }
   System.out.println(resultString);
   System.out.println(
     "=======================================================");

   while (rSet.next())
   {
    resultString = "";
    for (int colNum = 1; colNum <= numColumns; colNum++)
    {
     String column = rSet.getString(colNum);
     if (column != null)
      resultString += column + "     ";
    }
    System.out.println(resultString + '\n' +
      "-------------------------------------------------------");
   }
  } catch (SQLException ex) {
   System.out.println("SQLException: " + ex.getMessage());
   ex.printStackTrace();
  }
 }
}