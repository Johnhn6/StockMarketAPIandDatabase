import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class App {
    public static void main(String[] args) throws Exception {

    	System.out.println("Hello! Welcome to the Stock Market Database.");
    	System.out.println("To use this database, please have your API key ready.");
    	System.out.println("If you need one, then please go to https://www.alphavantage.co/support/#api-key.");
    	System.out.println("Special Thanks to alphavantage for allowing use of their API.");
    	Scanner user = new Scanner(System.in);
    	String apiKey = "";
    	String database = "";
    	String username = "";
    	String password = "";
    	
//    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//    	dateFormat.setLenient(false);
//    	String current = LocalDateTime.now().minusDays(3).toString();
//    	Date oldestDate = dateFormat.parse("1999-11-01");
//    	Date currentDates = dateFormat.parse(current);
//    	System.out.println(currentDates);
    	dailyStockMarket market = new dailyStockMarket();
        
    	
    	while(true)
    	{
    		System.out.println();
    		System.out.println("-------------------------------------------------------");
    		
    		//This if statement handles API key and MySQL database login.
    		if(apiKey.equals(""))
    		{
    			System.out.println("Please enter your API key. You can type Exit anytime to exit the program.");
    			apiKey = user.nextLine();
    			String apiExit = apiKey.toLowerCase();
    			if(apiExit.equals("exit"))
    			{
    				break;
    			}
    			
    			market.setAPI(apiKey);
    			
    			System.out.println("Please enter your MySQL database url.");
    			database = user.nextLine();
    			
    			String dataExit = database.toLowerCase();
    			if(dataExit.equals("exit"))
    			{
    				break;
    			}
    			
    			
    			System.out.println("Please enter your MySQL database username.");
    			username = user.nextLine();
    			
    			String userExit = username.toLowerCase();
    			if(userExit.equals("exit"))
    			{
    				break;
    			}
    			
    			System.out.println("Please enter your MySQL database password.");
    			password = user.nextLine();
    			
    			String passwordExit = password.toLowerCase();
    			if(passwordExit.equals("exit"))
    			{
    				break;
    			}
    			
    			market.setMySQL(database, username, password);
    		}
    		
    		
    		
    		//Prints our available commands.
    		System.out.println("Here is a list of Commands. For more information, type Information. You do not need to capitilize.");
    		System.out.println("Information, API, Stock, Difference, Exit");
    		
    		String command = user.nextLine();
    		command = command.toLowerCase();
    		
    		//Gives information of each command.
    		if(command.equals("information"))
    		{	
    			System.out.println("API: Lets you change your API and database login.");
    			
    			System.out.println();
    			
    			System.out.println("Stock: Gives you more information of a stock based on your inputs after selecting the command.");
    			
    			System.out.println();
    			
    			System.out.println("Difference: Shows the gains or losses of a stock from different days. "
    					+ "It is based on the closing values which is at the end of the day.");
    			
    			System.out.println();
    			
    			System.out.println("Exit: Exits the program.");
    		}
    		
    		//Resets API but not MySQL Database. You will be forced to input API and MySQL database login after this.
    		else if(command.equals("api"))
    		{
    			apiKey = "";
    		}
    		
    		//Gives values of stock from MySQL database. If not there, then uses API to get the data, but can take time if they
    		//have to go through a lot of data as it is programmed to update all of that stock's available data.
    		else if(command.equals("stock"))
    		{
    			System.out.println("Each stock can give you Open, Close, High, and Low values");
    			System.out.println("Open is the value of the stock at the beginning of the day or when the market opens.");
    			System.out.println("Close is the value of the stock at the end of the day or when the market closes.");
    			System.out.println("High is the highest value of the stock on that day.");
    			System.out.println("Low is the lowest value of the stock on that day");
    			
    			System.out.println();
    			
    			System.out.println("If the Stock is not in the database, the program will obtain it.");
    			System.out.println("It may take some time as it will update with the stock data from 1999-11-01 to 3 days behind the current day.");
    			System.out.println("Future queries on the same stock will go much faster");
    			
    			
    			System.out.println();
    			
    			System.out.println("Please enter stock.");
    			String stock = user.nextLine().toUpperCase();
    			
    			System.out.println("Please enter date. Please put it in format year-month-day. Example: 2024-06-15. Can go from"
    					+ " 1999-11-01 to 3 days behind the current day.");
    			String date = user.nextLine();
    			
    			System.out.println("Please enter which value you want. The choices are: Opening, Closing, High, Low, and All.");
    			String value = user.nextLine().toLowerCase();
    				
    			
    			
    			if(value.equals("opening"))
    			{
    				market.opening(stock, date, value);
    			}
    			
    			else if(value.equals("closing"))
    			{
    				market.closing(stock, date, value);
    			}
    			
    			else if(value.equals("high"))
    			{
    				market.highestValue(stock, date, value);
    			}
    			
    			else if(value.equals("low"))
    			{
    				market.lowestValue(stock, date, value);
    			}
    			
    			else if(value.equals("all"))
    			{
    				market.opening(stock, date, "opening");
    				market.closing(stock, date, "closing");
    				market.highestValue(stock, date, "high");
    				market.lowestValue(stock, date, "low");
    			}
    			
    			else
    			{
    				System.out.println("Wrong value option.");
    			}
    		}
    		
    		//Gives you the gains or losses of the stock value between two different dates based on closing value. 
    		//If not there, then uses API to get the data, but can take time if they
    		//have to go through a lot of data as it is programmed to update all of that stock's available data.
    		else if(command.equals("difference"))
    		{
    			System.out.println("Please enter stock.");
    			String stock = user.nextLine().toUpperCase();
    			
    			System.out.println("Please enter first date. Please put it in format year-month-day. Example: 2024-06-15. Can go from"
    					+ " 1999-11-01 to 3 days behind the current day.");
    			String day = user.nextLine();
    			
    			System.out.println("Please enter second date. Please put it in format year-month-day. Example: 2024-06-15. Can go from"
    					+ " 1999-11-01 to 3 days behind the current day.");
    			String otherDay = user.nextLine();
    			
    			market.difference(stock, day, otherDay);
    			
    		}
    		
    		//breaks while loop and allows the program to terminate
    		else if(command.equals("exit"))
    		{
    			break;
    		}
    		
    		//if you input wrong command then print the statement and go to next loop
    		else
    		{
    			System.out.println("Wrong Command. Please type it again. You do not have to worry about capitalization.");
    			
    		}
    		
    	}
    	
    	
        System.out.println("You have exited the program. Thank you for using this program.");

        
    }
}


