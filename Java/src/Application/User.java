package Application;
// written by Dawid (Dav) Gorski
import java.util.*;

class User implements java.io.Serializable{
    private static final long serialVersionUID = 1L;//needed for the serializable otherwise different machines will
                                                    // save/load differently

    private String userName;
    private String password;

    //populated when user buys a stock, depopulated when user sells last stock
    private Map<String, Stock> stocksOwned = new TreeMap<>();
    private double funds = 0;

    User(String name, String password ){//constructor
         setUserName(name);
         setPassword(password);
    }
    void setUserName(String name){//setName so that the user can change it later
        this.userName = name;
    }

    void setPassword(String pass) { //setPass so that the user can change it later
        this.password=pass;
    }

    String getUserName(){ //getters to check if the user exists and other stuff
        return userName;
    }
    String getPass(){ //used when logging in
        return password;
    }

    void addStock( Stock stock){ // when buying a stock
        stocksOwned.put(stock.getName(), stock);
    }

    //input is stock name
    Stock getStock(String id){ //get the stock
        if (stocksOwned.containsKey(id)){
            return stocksOwned.get(id);
        }
        return null;
    }
    ArrayList<Stock> getAllStocks(){ //gets all user stocks
        ArrayList<Stock> stocks = new ArrayList<>();
        for(Map.Entry<String,Stock> entry : stocksOwned.entrySet()) {
            Stock value = entry.getValue();
            stocks.add(value);
        }

        return stocks;
    }//end

    private void removeStock(String id){ //when user sells last stock
        stocksOwned.remove(id);
    }

    LinkedList<Double> getPriceHistory(int period)
    {
        ArrayList<Stock> stocks = this.getAllStocks();
        LinkedList<Double> priceHistory = new LinkedList<>();
        if (stocks.isEmpty())return null;
        Stock temp = stocks.get(0);

        LinkedList<String> dates = temp.getDates(period);
        double priceTemp = 0;
        double totalDayValue = 0;
        double prevDayValue = 0;
        int countMax = dates.size();
        int count = 0;
        int countTemp;
        boolean missingdata = false;

        for (String date:dates)//O(n^3)
        {
            for (Stock stock:stocks)//O(n^2)
            {
                priceTemp = -0.1;//the "Specified date not found" value
                countTemp = count;

                while (priceTemp == -0.1 && countTemp < countMax)
                {
                    //this loop means that if data is missing for a specific date, it will use the next available previous open value
                    priceTemp = stock.getValue(date);//O(n)
                    countTemp++;
                }

                totalDayValue+= priceTemp * stock.getShares();
                if (priceTemp == -0.1)missingdata = true;
            }
            //if there is data missing then it uses the value for the previous day
            if (missingdata){
                priceHistory.add(prevDayValue);
                missingdata = false;
            }else{
                priceHistory.add(totalDayValue);
                prevDayValue = totalDayValue;
            }

            totalDayValue = 0;
            count++;
        }
        return priceHistory;
    }//end

    double getFunds() { //get the funds
        return funds;
    }

    void addFunds(double i) { //add funds
        this.funds += i;
    }

    void changeShares(String id, Double shares, boolean buySell, Double salePrice){
        //changes the value of shares for use with buying/selling
        Stock stockToChange = getStock(id);
        if (buySell){
            //buying
            stockToChange.updateShares(shares);
            addFunds((shares*salePrice)*-1 );//because buying so giving the function a negative
        }else{
            //selling
            stockToChange.updateShares(shares*-1);
            addFunds(shares*salePrice);
            if (stockToChange.getShares() <0) {
                removeStock(stockToChange.getName());
            }
        }
    }//end
}
