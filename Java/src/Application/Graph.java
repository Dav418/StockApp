package Application;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class Graph extends JComponent
{
    private String name;
    private double high;
    private double low;
    private double xSpace;
    private double yScale;
    private LinkedList<Double> yValues;
    private LinkedList<String> xValues;
    private LinkedList<Double> MA_yValues;
    private LinkedList<double[]> pointList = new LinkedList<>();
    private LinkedList<double[]> MA_pointList = new LinkedList<>();
    private double[] highCo = new double[2];//co ords of the high value
    private double[] lowCo = new double[2];// ""
    private final int Y_AXIS_SIZE = 675;
    private final int X_AXIS_SIZE = 1200;
    private final int L_SPACER = 60;//the left offset of the graph
    private final int T_SPACER = 120;//the top offset of the graph
    private final int FIELD_WIDTH = 60;//the space given for printed fields (e.g the high and low values printed on the y axis)
    private double selectedValue;
    private double selectedX;
    private String selectedDate;
    private Color backgroundCol = col.getBackgroundColour();
    private Color drawCol = col.getFontColour();
    private boolean MA;

    Graph(String name, LinkedList<Double> yVals, LinkedList<String> xVals)
    {
        if (yVals.size() != xVals.size())throw new GraphException("create graph object with xVals and yVals of unequal size");
        this.yValues = yVals;
        this.xValues = xVals;
        this.name = name;
        this.MA = false;
        double[] temp = getHighLow(yVals);

        this.high = temp[0];
        this.low = temp[1];

        this.xSpace = X_AXIS_SIZE / (double)xVals.size();
        this.yScale = Y_AXIS_SIZE / high;

        highCo[0] = lowCo[0] = L_SPACER-FIELD_WIDTH;
        highCo[1] = Y_AXIS_SIZE - (high * yScale) + T_SPACER;

        lowCo[1] = Y_AXIS_SIZE - (low * yScale) + T_SPACER;

        for (int x = 0;x<yVals.size();x++)
        {
            double[] tempCoords = new double[2];
            tempCoords[0] = (xSpace * x) + L_SPACER;
            tempCoords[1] = Y_AXIS_SIZE - (yVals.get(x) * yScale) + T_SPACER;
            pointList.add(tempCoords);

        }

    }

    Graph(String name, LinkedList<Double> yVals,LinkedList<Double> MA_yVals, LinkedList<String> xVals)
    {
        if (yVals.size() != xVals.size())throw new GraphException("create graph object with xVals and yVals of unequal size");
        this.yValues = yVals;
        this.xValues = xVals;
        this.MA_yValues = MA_yVals;
        this.name = name;
        this.MA = true;

        //finding the high and low with two sets of yValues
        double[] temp = getHighLow(yVals);
        double[] temp1 = getHighLow(MA_yVals);

        if (temp[0] > temp1[0]){this.high = temp[0];
        }else this.high = temp1[0];
        if (temp[1] < temp1[1]){this.low = temp[1];
        }else this.low = temp1[1];


        this.xSpace = X_AXIS_SIZE / (double)xVals.size();
        this.yScale = Y_AXIS_SIZE / high;

        highCo[0] = lowCo[0] = L_SPACER-FIELD_WIDTH;
        highCo[1] = Y_AXIS_SIZE - (high * yScale) + T_SPACER;

        lowCo[1] = Y_AXIS_SIZE - (low * yScale) + T_SPACER;

        for (int x = 0;x<yVals.size();x++)
        {
            double[] tempCoords = new double[2];
            tempCoords[0] = (xSpace * x) + L_SPACER;
            tempCoords[1] = Y_AXIS_SIZE - (yVals.get(x) * yScale) + T_SPACER;
            pointList.add(tempCoords);

        }

        for (int x = 0;x<yVals.size();x++)
        {
            double[] tempCoords = new double[2];
            tempCoords[0] = (xSpace * x) + L_SPACER;
            tempCoords[1] = Y_AXIS_SIZE - (MA_yVals.get(x) * yScale) + T_SPACER;
            MA_pointList.add(tempCoords);

        }

    }

    void mousePos(int mouseX)
    {
        if (mouseX < L_SPACER || mouseX > L_SPACER + X_AXIS_SIZE - 1)
        {
            selectedX = 0;
        }else{
            selectedX = mouseX;
            mouseX = mouseX - L_SPACER;
            double temp = mouseX / xSpace;
            selectedValue = yValues.get((int)temp);
            selectedDate = xValues.get((int)temp);
        }

        this.repaint();
    }

    void changeGraph(String name, LinkedList<Double> yVals, LinkedList<String> xVals)
    {
        if (yVals.size() != xVals.size())throw new GraphException("create graph object with xVals and yVals of unequal size");
        pointList = new LinkedList<>();
        this.yValues = yVals;
        this.xValues = xVals;
        this.name = name;
        this.selectedX = 0;
        this.MA = false;
        double[] temp = getHighLow(yVals);

        this.high = temp[0];
        this.low = temp[1];

        xSpace = X_AXIS_SIZE / (double)xVals.size();
        yScale = Y_AXIS_SIZE / high;

        highCo[0] = lowCo[0] = L_SPACER-FIELD_WIDTH;
        highCo[1] = Y_AXIS_SIZE - (high * yScale) + T_SPACER;

        lowCo[1] = Y_AXIS_SIZE - (low * yScale) + T_SPACER;

        for (int x = 0;x<yVals.size();x++)
        {
            double[] tempCoords = new double[2];
            tempCoords[0] = (xSpace * x) + L_SPACER;
            tempCoords[1] = Y_AXIS_SIZE - (yVals.get(x) * yScale) + T_SPACER;
            pointList.add(tempCoords);

        }
    }

    void changeGraph(String name, LinkedList<Double> yVals,LinkedList<Double> MA_yVals, LinkedList<String> xVals)
    {
        if (yVals.size() != xVals.size())throw new GraphException("create graph object with xVals and yVals of unequal size");
        pointList = new LinkedList<>();
        MA_pointList = new LinkedList<>();
        this.yValues = yVals;
        this.xValues = xVals;
        this.MA_yValues = MA_yVals;
        this.name = name;
        this.MA = true;

        //finding the high and low with two sets of yValues
        double[] temp = getHighLow(yVals);
        double[] temp1 = getHighLow(MA_yVals);

        if (temp[0] > temp1[0]){this.high = temp[0];
        }else this.high = temp1[0];
        if (temp[1] < temp1[1]){this.low = temp[1];
        }else this.low = temp1[1];


        this.xSpace = X_AXIS_SIZE / (double)xVals.size();
        this.yScale = Y_AXIS_SIZE / high;

        highCo[0] = lowCo[0] = L_SPACER-FIELD_WIDTH;
        highCo[1] = Y_AXIS_SIZE - (high * yScale) + T_SPACER;

        lowCo[1] = Y_AXIS_SIZE - (low * yScale) + T_SPACER;

        for (int x = 0;x<yVals.size();x++)
        {
            double[] tempCoords = new double[2];
            tempCoords[0] = (xSpace * x) + L_SPACER;
            tempCoords[1] = Y_AXIS_SIZE - (yVals.get(x) * yScale) + T_SPACER;
            pointList.add(tempCoords);

        }

        for (int x = 0;x<yVals.size();x++)
        {
            double[] tempCoords = new double[2];
            tempCoords[0] = (xSpace * x) + L_SPACER;
            tempCoords[1] = Y_AXIS_SIZE - (MA_yVals.get(x) * yScale) + T_SPACER;
            MA_pointList.add(tempCoords);

        }

    }

    private double[] getHighLow(LinkedList<Double> yVals)
    {
        double[] highlow = {Integer.MIN_VALUE,Integer.MAX_VALUE};


        for (double yVal:yVals)
        {
            if (yVal > highlow[0])highlow[0] = yVal;
            if (yVal < highlow[1])highlow[1] = yVal;
        }

        return highlow;
    }



    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        g.setColor(backgroundCol);
        int d_FIELD_WIDTH = FIELD_WIDTH * 2;
        g.fillRect(L_SPACER-FIELD_WIDTH,T_SPACER-FIELD_WIDTH,X_AXIS_SIZE+ d_FIELD_WIDTH,Y_AXIS_SIZE+ d_FIELD_WIDTH);
        g.setColor(drawCol);
        int h_FIELD_WIDTH = FIELD_WIDTH / 2;
        if (selectedX != 0)
        {

            g.drawString(selectedDate + " : " + String.format("%.2f",selectedValue),(int)selectedX -FIELD_WIDTH,(T_SPACER- h_FIELD_WIDTH));
            int tempy =(int) (Y_AXIS_SIZE - (selectedValue * yScale) + T_SPACER);
            g.setColor(Color.BLUE);
            g.drawLine((int)selectedX,tempy-10,(int)selectedX,tempy+10);
            g.drawLine((int)selectedX+5,tempy-10,(int)selectedX-5,tempy+10);
            g.drawLine((int)selectedX-5,tempy-10,(int)selectedX+5,tempy+10);
            g.setColor(drawCol);
        }


        g.drawString(name,L_SPACER+(X_AXIS_SIZE/2),T_SPACER);//Graph title
        g.drawString(String.format("%.2f",high),(int)highCo[0],(int)highCo[1]);//high
        g.drawString(String.format("%.2f",low),(int)lowCo[0],(int)lowCo[1]);//low

        g.drawString(xValues.getFirst(),L_SPACER - h_FIELD_WIDTH,T_SPACER+Y_AXIS_SIZE+ h_FIELD_WIDTH);//starting date
        g.drawString(xValues.getLast(),L_SPACER+X_AXIS_SIZE - h_FIELD_WIDTH,T_SPACER+Y_AXIS_SIZE+ h_FIELD_WIDTH);

        g.drawLine(L_SPACER,T_SPACER,L_SPACER,T_SPACER+Y_AXIS_SIZE);//y axis
        g.drawLine(L_SPACER,T_SPACER+Y_AXIS_SIZE,L_SPACER+X_AXIS_SIZE,T_SPACER+Y_AXIS_SIZE);//x axis
        double[] temp;
        double[] temp1;
        g.setColor(Color.GREEN);
        for (int x = 0;x<pointList.size();x++)
        {

            if (x>0)
            {

                temp = pointList.get(x-1);
                temp1 = pointList.get(x);
                if (temp[1] < temp1[1]){g.setColor(Color.RED);
                }else g.setColor(Color.GREEN);
                g.drawLine((int)temp[0],(int)temp[1],(int)temp1[0],(int)temp1[1]);
            }
        }

        if(MA)
        {
            g.setColor(Color.WHITE);
            for (int x = 0;x<MA_pointList.size();x++)
            {

                if (x>0)
                {
                    temp = MA_pointList.get(x-1);
                    temp1 = MA_pointList.get(x);
                    g.drawLine((int)temp[0],(int)temp[1],(int)temp1[0],(int)temp1[1]);
                }
            }
        }


    }



}
