package Application;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.*;

public class Stock extends JComponent {

    private double shares;
    private LinkedList<LinkedList<String>> stockInfo;
    private LinkedList<Double> opens;
    private String name;
    private double invested;
    private double sales;

    Stock(String csvName)
    {
        this.invested = 0;
        this.shares = 0;
        this.sales = 0;
        this.name = csvName;
        this.stockInfo = this.readOnlineCSV();
        this.opens = getOpens(3);

    }

   public String getName(){ return name;} //setters and getters for all the private variables

   double getShares(){
        return shares;
    }
   void updateShares(double i){ //add shares when you are buying or selling
        shares += i;
    }

   double getSales(){
        return sales;
    }
   public void setSales(double i){ //when selling stock
        sales += i;
    }

   double getInvested(){
        return invested;
    }
   void changeInvested(double i){
        invested+=i;
    }


    //gets the value of a stock on a given date, returns todays value if empty string is passed
    double getValue(String date)
    {

        if (date.length() == 0){
            return this.opens.getLast();
        }else{
            for (int i=0;i<stockInfo.get(3).size();i+=9){
                if (stockInfo.get(3).get(i).equals(date)  ){
                    return Double.parseDouble(stockInfo.get(3).get(i+1));
                }
            }
        }

        return -0.1;//if no matches were found then we return this value
    }

    //gets all the open values of a stock from the past five years
    LinkedList<Double> getOpens(int period)//0,1,2,3 - 1m,3m,1y,5y
    {
        LinkedList<Double> openList = new LinkedList<>();

        for (int x = 1;x< stockInfo.get(period).size();x+=9)
        {
            openList.add(Double.parseDouble(stockInfo.get(period).get(x)));
        }

        return openList;
    }

    LinkedList<String> getDates(int period)//0,1,2,3 - 1m,3m,1y,5y
    {
        LinkedList<String> dateList = new LinkedList<>();

        for (int x = 0;x<stockInfo.get(period).size();x+=9)
        {
            dateList.add(stockInfo.get(period).get(x));
        }

        return dateList;
    }


    //reworked version that only adds the first ten values of each line, thus avoiding the issue with date formatting changing half way through the csv
    private LinkedList<LinkedList<String>> readOnlineCSV()
    {
        LinkedList<LinkedList<String>> priceData = new LinkedList<>();

        priceData.add(new LinkedList<>());//1m
        priceData.add(new LinkedList<>());//3m
        priceData.add(new LinkedList<>());//1y
        priceData.add(new LinkedList<>());//5y

        String[] csv = {
                "https://api.iextrading.com/1.0/stock/"+name+"/chart/1m?format=.csv",
                "https://api.iextrading.com/1.0/stock/"+name+"/chart/3m?format=.csv",
                "https://api.iextrading.com/1.0/stock/"+name+"/chart/1y?format=.csv",
                "https://api.iextrading.com/1.0/stock/"+name+"/chart/5y?format=.csv"};


        int count = 0;
        int add = 0;
        for (String csvLink:csv)
        {

            try {
                URL stockURL = new URL(csvLink);
                BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openStream()));
                String line;
                String[] splitLine;

                line = in.readLine();//in the online csv there are no newlines

                String[] wrdsToDel ={"[{","}]","{","}","\"","date:","open:","high:","low:","close:","volume:","unadjustedVolume:","change:","changePercent:","vwap:","label:","changeOverTime:","open:"};
                for (String aWrdsToDel : wrdsToDel) {
                    line = line.replace(aWrdsToDel, "");
                }

                splitLine = line.split(",");

                for (String value:splitLine)
                {
                    if (value.matches("([0-9]{4}+)-([0-9]{2}+)-([0-9]{2}+)"))
                    {
                        add = 9;
                    }

                    if (add > 0)
                    {
                        priceData.get(count).add(value);
                        add--;
                    }
                }

            } catch(IOException e){
                System.out.println("No such stock exists");
            }

            count++;
        }


        return priceData;
    }

    /*
    ##########################################################################################
    #This is how we read the data initially, but the csv file had a formatting change 1/2 way#
    ##########################################################################################

    public static LinkedList<String> readOnlineCsv( String stockName)//e.g. 1 for month 2 for two month etc, String is the stocks name
    {   //AMZN is amazon, AAPL is apple, NFLX is netflix, MSFT is microsoft GOOGL is google
        
        LinkedList<String> csvData = new LinkedList<>();
        String st = "https://api.iextrading.com/1.0/stock/"+stockName+"/chart/3m?format=.csv";
        try {
            URL stockURL = new URL(st);
            BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openStream()));
            String s = null;
            String[] wrdsToAdd ={"date","open","high","low","close","volume","unadjustedVolume","change","changePercent","vwap","label","changeOverTime"};

            csvData.addAll(Arrays.asList(wrdsToAdd));
            while ((s=in.readLine())!=null) {
                String[] wrdsToDel ={"[{","}]","{","}","\"","date:","open:","high:","low:","close:","volume:","unadjustedVolume:","change:","changePercent:","vwap:","label:","changeOverTime:","open:"};
                for (String aWrdsToDel : wrdsToDel) {
                    s = s.replace(aWrdsToDel, "");
                }
                csvData.addAll(Arrays.asList(s.split(",")));
            }
        } catch(IOException e){
            System.out.println("No such stock exists");
        }


        //reads the data straight from source and adds to a linkedList
        //if you can, try to solve the issue wit date formatting changing half way through the csv
        return csvData;
    }
    */
}
