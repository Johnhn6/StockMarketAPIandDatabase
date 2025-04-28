import java.util.HashMap;

public interface stockMarket {
	// {Stock:
	//		date:
	//		"1. open": "228.9500", "2. high": "233.3600", "3. low": "226.3200", "4. close": "232.4100", "5. volume": "6700068"}
    public HashMap<String, HashMap<String, String>> apiGetJsonData(String stock);

    public void opening(String stock, String date, String info) throws ClassNotFoundException;

    public void closing(String stock, String date, String info) throws ClassNotFoundException;

    public void highestValue(String stock, String date, String info) throws ClassNotFoundException;

    public void lowestValue(String stock, String date, String info) throws ClassNotFoundException;

    public void difference(String stock, String day, String otherDay) throws ClassNotFoundException;

    public void setAPI(String api);
    
    public void setMySQL(String dataBase, String userName, String passWord);

}
