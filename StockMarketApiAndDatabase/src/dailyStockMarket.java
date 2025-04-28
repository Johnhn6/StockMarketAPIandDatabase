import java.io.IOException;


import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.util.HashMap;




public class dailyStockMarket implements stockMarket{
    private String apiKey;
    private String database;
    private String user;
    private String password;

    
    ///This will take a while. I ran the stock, IBM, and it took about 27 mins.
    ///But I did have print statements after every insert which made it go longer.
    ///But When I performed a search on an IBM row, I got the data in .01 seconds.
    ///The idea behind this project is to make a database that will grow and update with new information
    ///for each stock and to show it to the user based on their inputs.
    ///For every new stock that is entered, the program will do an SQL insert
    ///for 4 different tables. It will be O(4N) which is O(N) as we drop the constant.
    ///But it is a huge amount of data as we go through every day from 11/1/1999 to 3 days before the current day.
    @Override
    public HashMap<String, HashMap<String, String>> apiGetJsonData(String stock)
    {
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY" + "&symbol=" + stock + "&outputsize=full" +"&apikey=" + apiKey;
//        System.out.println(url);
        URL api;
        HashMap<String, HashMap<String, String>> jsonMap = new HashMap<String, HashMap<String, String>>();
        
        try 
        {

            api = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) api.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if(responseCode != 200)
            {
                throw new RuntimeException("Reponse Code is " + responseCode);
            }
            
            //Use scanner to get stream then put the json data into a string then later put it back to JSON and use the
            //Key TIME SERIES (DAILY) to enter the inner dictionary where the key is dates and the value is the stock value
            //at open, close, high, and low
            String data = "";
            Scanner scanner = new Scanner(api.openStream());
            
            while(scanner.hasNext())
            {
            	data += scanner.nextLine();
            }
            
            scanner.close();
            connection.disconnect();
            
            JSONParser parse = new JSONParser();
            try 
            {
				JSONObject jsonData = (JSONObject) parse.parse(data);
				
				jsonMap = (HashMap)jsonData.get("Time Series (Daily)");
				sqlCommands inserting = new sqlCommands(database, user, password);
				for(String i: jsonMap.keySet())
				{
					
					try 
					{
						inserting.sqlInsert(stock, i, jsonMap.get(i).get("1. open"), jsonMap.get(i).get("4. close"), jsonMap.get(i).get("2. high"), jsonMap.get(i).get("3. low"));
					}
					
					catch (ClassNotFoundException e) 
					{	
						e.printStackTrace();
					}
					
					catch (SQLException e) 
					{
						e.printStackTrace();
					}
					
				}
				
				
			} 
            catch (ParseException e) 
            {
				e.printStackTrace();
			}
            
            //System.out.println(stockMap.toString());
        } 

        catch (MalformedURLException ex) 
        {
            System.out.println(ex);
        } 
        
        catch (IOException ex) 
        {
            System.out.println(ex);
        }
        
		return jsonMap;
    }

    ///next 4 functions just give you each of the 4 values of open, close, high, and low
    @Override
    public void opening(String stock, String date, String info) throws ClassNotFoundException
    {
    	sqlCommands checking = new sqlCommands(database, user, password);
		String[] results = checking.sqlGet(stock, date, info);
		
		if(results[0].equals("Wrong"))
		{
			System.out.print("Wrong Search Setting.");
		}
		
		else if(results[0].equals("Empty"))
		{
			///stockMap is a hashmap with another hashmap as the value 
		    ///key = 2024-12-31 
		    ///value = {"3. low":"218.4400","5. volume":"2270512","1. open":"220.7200","2. high":"221.0493","4. close":"219.8300"}
			HashMap<String, HashMap<String,String>> stockMap = apiGetJsonData(stock);
			System.out.println(stockMap.get(date).get("1. open"));
		}
		
		else
		{
			System.out.println("Stock: " + results[0] + " Date: " + results[1] + "  Open: "  + results[2]);
		}
        
    }

    @Override
    public void closing(String stock, String date, String info) throws ClassNotFoundException
    {
    	
    	sqlCommands checking = new sqlCommands(database, user, password);
		String[] results = checking.sqlGet(stock, date, info);
		
		if(results[0].equals("Wrong"))
		{
			System.out.print("Wrong Search Setting.");
		}
		
		else if(results[0].equals("Empty"))
		{
			HashMap<String, HashMap<String,String>> stockMap = apiGetJsonData(stock);
			System.out.println(stockMap.get(date).get("4. close"));
		}
		
		else
		{
			System.out.println("Stock: " + results[0] + " Date: " + results[1] + "  Close: "  + results[2]);
		}
    }

    @Override
    public void highestValue(String stock, String date, String info) throws ClassNotFoundException
    {
    	
    	sqlCommands checking = new sqlCommands(database, user, password);
		String[] results = checking.sqlGet(stock, date, info);
		
		if(results[0].equals("Wrong"))
		{
			System.out.print("Wrong Search Setting.");
		}
		
		else if(results[0].equals("Empty"))
		{
			HashMap<String, HashMap<String,String>> stockMap = apiGetJsonData(stock);
			System.out.println(stockMap.get(date).get("2. high"));
		}
		
		else
		{
			System.out.println("Stock: " + results[0] + " Date: " + results[1] + "  High: "  + results[2]);
		}
    }

    @Override
    public void lowestValue(String stock, String date, String info) throws ClassNotFoundException
    {
    	sqlCommands checking = new sqlCommands(database, user, password);
		String[] results = checking.sqlGet(stock, date, info);
		
		if(results[0].equals("Wrong"))
		{
			System.out.print("Wrong Search Setting.");
		}
		
		else if(results[0].equals("Empty"))
		{
			HashMap<String, HashMap<String,String>> stockMap = apiGetJsonData(stock);
			System.out.println(stockMap.get(date).get("3. low"));
		}
		
		else
		{
			System.out.println("Stock: " + results[0] + " Date: " + results[1] + "  Low: "  + results[2]);
		}
    }
    
    //get data of both dates and subtract the closing value
    @Override
    public void difference(String stock, String day, String otherDay) throws ClassNotFoundException
    {
    	sqlCommands checking = new sqlCommands(database, user, password);
		String[] results = checking.sqlGet(stock, day, "closing");
    	String[] resultsOther =checking.sqlGet(stock, otherDay, "closing");
		
		if(results[0].equals("Wrong") || resultsOther[0].equals("Wrong"))
		{
			System.out.print("Wrong Search Setting.");
		}
		
		else if(results[0].equals("Empty") || resultsOther[0].equals("Empty"))
		{
			HashMap<String, HashMap<String,String>> stockMap = apiGetJsonData(stock);
	    	float x = Float.parseFloat(stockMap.get(day).get("Closing"));
	    	float y = Float.parseFloat(stockMap.get(otherDay).get("Closing"));
	    	float calculation = x - y;
	    	if (calculation == 0)
	    	{
	    		System.out.println("No change: " + calculation);
	    	}
	    	else if (calculation > 0)
	    	{
	    		System.out.println("Gains: " + calculation);
	    	}
	    	else
	    	{
	    		System.out.println("Loss: " + calculation);
	    	}
		}
		
		else
		{
			float x = Float.parseFloat(results[2]);
	    	float y = Float.parseFloat(resultsOther[2]);
	    	float calculation = x - y;
			if (calculation == 0)
	    	{
	    		System.out.println("No change: " + calculation);
	    	}
	    	else if (calculation > 0)
	    	{
	    		System.out.println("Gains: " + calculation);
	    	}
	    	else
	    	{
	    		System.out.println("Loss: " + calculation);
	    	}
		}
    	
    	
    }

    @Override
    public void setAPI(String api)
    {
        apiKey = api;
    }
    
    @Override
    public void setMySQL(String dataBase, String userName, String passWord)
    {
    	database = dataBase;
    	user = userName;
    	password = passWord;
    }
}
