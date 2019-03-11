package Application;
/*
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.style.Styler;
import java.util.*;
import java.util.List;

class StockChart extends BarChart<CategoryChart> {

    private User user;

    void setUser(User u){user = u;}


    public CategoryChart getChart() {
       //User user=new User("BarGraph","BarGraph"); //dummy user so that all the stocks will be displayed on the graph


        CategoryChart chart = new CategoryChartBuilder().width(1000).height(0).xAxisTitle("").yAxisTitle("Value [£]").build(); //height doesnt matter as its being streshed to fit the JPanel

        List<String> stockName = Arrays.asList("NFLX","AMZN","GOOGL","AAPL","MSFT");

        //for (String a:stockName) { user.addStock(new Stock(a)); }

        ArrayList<Stock> stocks = user.getAllStocks();
        for (Stock s:stocks
             ) {
            System.out.println(s.getName() + " " + s.getShares());

        }

        if (stocks.isEmpty()) {

            System.out.println("No shares owned");

            List<String> dates = Arrays.asList("Date1","Date2","Date3","Date4","Date5"); //fixed date values
            chart.addSeries("Empty",dates,Arrays.asList(0, 0, 0, 0,0));

        } else {
            LinkedList<String> d = stocks.get(0).getDates(0);
            Iterator lit = d.descendingIterator();

            ArrayList<String> datesToUse = new ArrayList<>();
            int i = 0;
            while (i < 5){
                datesToUse.add(lit.next().toString());
                i++;
            }


            //System.out.println(user.getLastFiveDays(stocks.get(0).getName()));

            ArrayList<List> chartValues = new ArrayList<>();
            for (Stock s:stocks)
            {
                chartValues.add(user.getLastFiveDays(s.getName()));
            }


            List<String> dates = Arrays.asList(datesToUse.get(0),datesToUse.get(1),datesToUse.get(2),
                    datesToUse.get(3),datesToUse.get(4)); //fixed dates
            int stockStart = 0;

            for (List e: chartValues) {

                chart.addSeries(stocks.get(stockStart).getName() , dates ,e);
                stockStart++;


            }
        }


        LinkedList<String> d = stocks.get(0).getDates(0);
        Iterator lit = d.descendingIterator();

        ArrayList<String> datesToUse = new ArrayList<>();
        int i = 0;
        while (i < 5){
            datesToUse.add(lit.next().toString());
            i++;
        }


        //System.out.println(user.getLastFiveDays(stocks.get(0).getName()));

        ArrayList<List> chartValues = new ArrayList<>();
        for (Stock s:stocks)
        {
            chartValues.add(user.getLastFiveDays(s.getName()));
        }


        List<String> dates = Arrays.asList(datesToUse.get(0),datesToUse.get(1),datesToUse.get(2),
                datesToUse.get(3),datesToUse.get(4)); //fixed dates
        int stockStart = 0;

        for (List e: chartValues) {

            chart.addSeries(stocks.get(stockStart).getName() , dates ,e);
            stockStart++;


        }


        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE); //Style
        chart.getStyler().setHasAnnotations(true);
        chart.getStyler().setChartBackgroundColor(col.getBackgroundColour());
        chart.getStyler().setChartFontColor(col.getFontColour());
        chart.getStyler().setPlotBackgroundColor(col.getBackgroundColour());
        chart.getStyler().setLegendBackgroundColor(col.getBackgroundColour());
        chart.getStyler().setAxisTickLabelsColor(col.getFontColour());



        //made up values
        //chart.addSeries("Netflix",dates,Arrays.asList(10, 500, 0, 300));
        //chart.addSeries("Amazon",dates,Arrays.asList(220, 200, 200, 200));
        //chart.addSeries("Google",dates,Arrays.asList(300, 300, 300, 300));
        //chart.addSeries("Apple",dates,Arrays.asList(400, 400, 400, 400));
        //chart.addSeries("Microsoft",dates,Arrays.asList(500, 500, 500, 500));

        return chart;
    }


}
*/