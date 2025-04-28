 import java.sql.*;


public class sqlCommands {
    private String database;
    private String user;
    private String password;


    public sqlCommands(String data, String u, String pw)
    {
        database = data;
        user = u;
        password = pw;
    }
    
    public void sqlInsert(String stock, String date, String opening, String closing, String high, String low) throws ClassNotFoundException, SQLException
    {
   
		Class.forName("com.mysql.cj.jdbc.Driver");
		
    	
    	Connection connect;
		
		connect = DriverManager.getConnection(database, user, password);
		
		///They check if the data/row exists first then do the action between BEGIN and END if it does not exists
		///Update: Using "IF NOT EXISTS" is not valid as that is only for stored procedures. This is not exactly efficient 
		///but for now I will have to use my getSQL function to see if the database is empty or not then input it in 
		///For the Future: I can try to see if I can do a check and insert at the same time in this function
    	String insertOpen = "INSERT INTO stockopening VALUES(?, ?, ?)";
    	String insertClose = "INSERT INTO stockclosing VALUES(?, ?, ?)";
    	String insertHigh = "INSERT INTO stockhigh VALUES(?, ?, ?)";
    	String insertLow = "INSERT INTO stocklow VALUES(?, ?, ?)";
    	
    	
    	String[] checkOpen = sqlGet(stock, date, "opening");
    	String[] checkClose = sqlGet(stock, date, "closing");
    	String[] checkHigh = sqlGet(stock, date, "high");
    	String[] checkLow = sqlGet(stock, date, "low");
    	
    	
    	//Insert the stock data in each table. One for each value of Open, Close, High, and Low
    	
    	////////////////////////////////////
    	
    	if(checkOpen[0].equals("Empty"))
    	{
    		PreparedStatement preparedOpen = connect.prepareStatement(insertOpen);
        	preparedOpen.setString(1, stock);
        	preparedOpen.setString(2, date);
        	preparedOpen.setFloat(3, Float.parseFloat(opening));
        	preparedOpen.executeUpdate();
        	preparedOpen.close();
    	}
    	
    	
    	///////////////////////////////////////////////////
    	
    	if(checkClose[0].equals("Empty"))
    	{
    		PreparedStatement preparedClose = connect.prepareStatement(insertClose);
        	preparedClose.setString(1, stock);
        	preparedClose.setString(2, date);
        	preparedClose.setFloat(3, Float.parseFloat(closing));
        	
        	preparedClose.executeUpdate();
        	preparedClose.close();
    	}
    	
    	
    	//////////////////////////////////////////////////////
    	
    	if(checkHigh[0].equals("Empty"))
    	{
    		PreparedStatement preparedHigh = connect.prepareStatement(insertHigh);
        	preparedHigh.setString(1, stock);
        	preparedHigh.setString(2, date);
        	preparedHigh.setFloat(3, Float.parseFloat(high));
        	
        	preparedHigh.executeUpdate();
        	preparedHigh.close();
    	}
    	
    	
    	//////////////////////////////////////////////////
    	
    	if(checkLow[0].equals("Empty"))
    	{
    		PreparedStatement preparedLow = connect.prepareStatement(insertLow);
        	preparedLow.setString(1, stock);
        	preparedLow.setString(2, date);
        	preparedLow.setFloat(3, Float.parseFloat(low));
        	
        	preparedLow.executeUpdate();
        	preparedLow.close();
    	}
    	
    	
    	connect.close();
    	
    }
    
    //checks if stock data is in MySQL database. If not, then do the sqlinsert(). If it is in the database, then get it.
    public String[] sqlGet(String stock, String date, String info) throws ClassNotFoundException
    {
    	String[] answer = new String[3];
    	try 
    	{
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		
        	
        	Connection connect;
    		
    		connect = DriverManager.getConnection(database, user, password);
    		
    		PreparedStatement preparedQuery;
    		
    		///I was getting an error about syntax and after trial and error
    		///found out that the table cannot be inputted from preparedstatement.setString()
    		///it must already be typed in. For example, "From stockopening" not "From ?"
    		///Update: So the ? that will be replaced can only be in the values
        	if(info.equals("opening"))
        	{
        		String query = "SELECT * FROM stockopening WHERE Stock = ? and Dates = ?";
        		preparedQuery = connect.prepareStatement(query);
        		preparedQuery.setString(1, stock);
        		preparedQuery.setString(2, date);
        		
        	}
        	
        	else if(info.equals("closing"))
        	{
        		String query = "SELECT * FROM stockclosing WHERE Stock = ? and Dates = ?";
        		preparedQuery = connect.prepareStatement(query);
        		preparedQuery.setString(1, stock);
        		preparedQuery.setString(2, date);
        		
        	}
        	
        	else if(info.equals("high"))
        	{
        		
        		String query = "SELECT * FROM stockhigh WHERE Stock = ? and Dates = ?";
        		preparedQuery = connect.prepareStatement(query);
        		preparedQuery.setString(1, stock);
        		preparedQuery.setString(2, date);
        		
        	}
        	
        	else if(info.equals("low"))
        	{
        		
        		String query = "SELECT * FROM stocklow WHERE Stock = ? and Dates = ?";
        		preparedQuery = connect.prepareStatement(query);
        		preparedQuery.setString(1, stock);
        		preparedQuery.setString(2, date);
        		
        	}
        	
        	else 
        	{
        		//System.out.println("Wrong Search Information.");
            	answer[0] = "Wrong";
//            	System.out.println("Wrong");
            	return answer;
        	}
        	
        	
        	ResultSet results = preparedQuery.executeQuery();
        	
        	///checks if the ResultSet is empty
        	///Having an issue where column is not found even when it is in there
        	///Update: Fixed it. So it was actually getting data and it wasn't empty but 
        	///the results.getString(stock) and the others was not working
        	///but the other method that is based on column index works
        	///Most likely its because it is set to varChar and the parameter
        	///has to be a specific one
        	///Though I wonder why the index starts at 1. In computer science,
        	///everything starts at 0.
        	if(!results.isBeforeFirst())
        	{
//        		System.out.println("Not in Database.");
        		answer[0] = "Empty";
        		preparedQuery.close();
            	connect.close();
        		return answer;
        	}
        	
        	while(results.next())
        	{
        		String s = results.getString(1);
        		String d = results.getString(2);
        		Float i = results.getFloat(3);
        		String iString = i.toString();
        		answer[0] = s;
        		answer[1] = d;
        		answer[2] = iString;
//        		System.out.println("Stock: " +s + " Date: " + d + "  " + info + ":" + iString);
        		
        	}
        	
        	preparedQuery.close();
        	connect.close();
        	return answer;
    	}

    	
    	catch(SQLException e)
    	{
    		System.out.println(e.getMessage());
    	}
    	
    	//should need to do the bottom two lines, but i needed to put a return outside of the try() block since I return a String[]
    	answer[0] = "Empty";
    	return answer;
  
    }
    
    ///I don't think this will be needed but I decided to make one in case I want to delete something.
    ///Can be used to start from a clean slate.
    public void sqlDelete(String stock, String date, String info) throws ClassNotFoundException
    {
    	try 
    	{
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		
        	
        	Connection connect;
    		
    		connect = DriverManager.getConnection(database, user, password);
        	
    		PreparedStatement preparedQuery;
    		

        	if(info.equals("opening"))
        	{
        		String query = "Delete FROM stockopening WHERE Stock = ? and Dates = ?";
        		preparedQuery = connect.prepareStatement(query);
        		preparedQuery.setString(1, stock);
        		preparedQuery.setString(2, date);
        		preparedQuery.executeUpdate();
            	preparedQuery.close();
        	}
        	
        	else if(info.equals("closing"))
        	{
        		String query = "Delete FROM stockclosing WHERE Stock = ? and Dates = ?";
        		preparedQuery = connect.prepareStatement(query);
        		preparedQuery.setString(1, stock);
        		preparedQuery.setString(2, date);
        		preparedQuery.executeUpdate();
            	preparedQuery.close();
        	}
        	
        	else if(info.equals("high"))
        	{
        		String query = "Delete FROM stockhigh WHERE Stock = ? and Dates = ?";
        		preparedQuery = connect.prepareStatement(query);
        		preparedQuery.setString(1, stock);
        		preparedQuery.setString(2, date);
        		preparedQuery.executeUpdate();
            	preparedQuery.close();
        	}
        	
        	else if(info.equals("low"))
        	{
        		String query = "Delete FROM stocklow WHERE Stock = ? and Dates = ?";
        		preparedQuery = connect.prepareStatement(query);
        		preparedQuery.setString(1, stock);
        		preparedQuery.setString(2, date);
        		preparedQuery.executeUpdate();
            	preparedQuery.close();
        	}
        	
        	else 
        	{
        		System.out.println("Wrong Delete Information.");
        	}
        	

        	connect.close();
    	}
    	
    	catch(SQLException e)
    	{
    		System.out.println(e.getMessage());
    	}
    }
    
    //Not sure if I need it, but it clears the table of all data
    public void sqlClearTable(String info) throws ClassNotFoundException
    {
    	try 
    	{
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		
        	
        	Connection connect;
    		
    		connect = DriverManager.getConnection(database, user, password);
        	
    		
    		
    		PreparedStatement preparedQuery;
    		

        	if(info.equals("opening"))
        	{
        		String query = "Delete FROM stockopening";
        		preparedQuery = connect.prepareStatement(query);
        		preparedQuery.executeUpdate();
            	preparedQuery.close();
        	}
        	
        	else if(info.equals("closing"))
        	{
        		String query = "Delete FROM stockclosing";
        		preparedQuery = connect.prepareStatement(query);
        		preparedQuery.executeUpdate();
            	preparedQuery.close();
        	}
        	
        	else if(info.equals("high"))
        	{
        		String query = "Delete FROM stockhigh";
        		preparedQuery = connect.prepareStatement(query);
        		preparedQuery.executeUpdate();
            	preparedQuery.close();
        	}
        	
        	else if(info.equals("low"))
        	{
        		String query = "Delete FROM stocklow";
        		preparedQuery = connect.prepareStatement(query);
        		preparedQuery.executeUpdate();
            	preparedQuery.close();
        	}
        	
        	else 
        	{
        		System.out.println("Wrong Delete Information.");
        	}
        	
        	
        	connect.close();
    	}
    	
    	catch(SQLException e)
    	{
    		System.out.println(e.getMessage());
    	}
    }


}
