package Application;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
//moving averages crossover 
//https://www.investopedia.com/articles/active-trading/052014/how-use-moving-average-buy-stocks.asp

public class MovingAverages {

    private final Queue<Double> Dataset = new LinkedList<Double>();
    private final int smoothingFac;
    private double sum;





    public MovingAverages(int smoothingFac)
    {
        //period refers to the smoothing of the moving average graph 
        //not the period pf stock data
        //when calling try values 5,10,50
        this.smoothingFac = smoothingFac;
    }


    public void addData(double num)
    {
        sum += num;
        Dataset.add(num);
        if (Dataset.size() > smoothingFac)
        {
            sum -= Dataset.remove();
        }
    }


    public double getMean()
    {
        double mean;
        //String temp;
        //DecimalFormat formatter = new DecimalFormat("#.##");
        mean = sum / smoothingFac;
        //temp = formatter.format(mean);
        //mean = Double.parseDouble(temp);
        return mean;
    }

    //not needed
    /*
    public LinkedList<Double> retrieveData(String csvName){
        LinkedList<Double> inputData = new LinkedList<>();
        Stock obj = new Stock(csvName);
        inputData = obj.getOpens(period);
        return inputData;
    }
    */
    public LinkedList<Double> plotDataY(LinkedList<Double> inputData){
        LinkedList<Double> plotDataY = new LinkedList<>();
        for (double x : inputData) {
            addData(x);
            plotDataY.add(getMean());
        }
        return plotDataY;
    }

    //not needed
    /*
    public LinkedList<String> plotDataX(int periodStock, String csvName){
        //use periodStock to specify the csv call
        Stock obj = new Stock(csvName);
        LinkedList<String> xData = new LinkedList<>();
        xData = obj.getDates(periodStock);
        return xData;

    }
    */

    public static void main(String[] args)

            //test
            //plotDataX to get the x values, plotDataY to get the y values and plot it on a graph
            
    {
        double[] input_data = { 1, 3, 5, 6, 8,
                12, 18, 21, 22, 25 };
        int per = 3;
        MovingAverages obj = new MovingAverages(per);
        for (double x : input_data) {
            obj.addData(x);
            System.out.println(x + " is added," +
                    " simpleMovingAverage is  = " + obj.getMean());
        }


        //System.out.println(obj.retrieveData( "NFLX"));


    }
}