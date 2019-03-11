package Application;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.LinkedList;

public class StockPieChart extends JComponent
{
    private User currentUser;
    private double scalingFac;
    private LinkedList<String> portfolioMakeup;
    private LinkedList<Double> makeupValue;
    private LinkedList<Integer> angleSizes;
    private LinkedList<Integer> angleStarts;
    private Color[] colors = {Color.RED,Color.BLUE,Color.GREEN,Color.MAGENTA,Color.ORANGE,Color.YELLOW,Color.WHITE,Color.CYAN,Color.PINK};
    private double total;//total portfolio value, used for showing percentages

    StockPieChart(User currentUser)
    {
        this.currentUser = currentUser;
        makeupValue = new LinkedList<>();
        portfolioMakeup = new LinkedList<>();
        angleSizes = new LinkedList<>();
        angleStarts = new LinkedList<>();
        total = 0;
        double temp;
        double funds = currentUser.getFunds();
        portfolioMakeup.add("Personal Funds");
        makeupValue.add(funds);
        total += funds;


        //getting the value of all shares
        for (Stock stock:currentUser.getAllStocks())
        {
            portfolioMakeup.add(stock.getName());
            temp = stock.getValue("") * stock.getShares();
            makeupValue.add(temp);
            total += temp;
        }
        //getting the scaling factor of the pie chart
        this.scalingFac = 360/total;
        //translating share value into starting and finishing angles

        int angleSize;
        int startingAngle = 0;
        for (double value:makeupValue)
        {
            angleStarts.add(startingAngle);
            angleSize = (int) (value * scalingFac);
            angleSizes.add(angleSize);
            startingAngle+=angleSize;

        }

        int fill = 360 - (angleStarts.getLast() + angleSizes.getLast());
        int fillStart = angleSizes.getLast() + fill;
        angleSizes.removeLast();
        angleSizes.add(fillStart);

    }

    void updateChart()
    {
        makeupValue = new LinkedList<>();
        portfolioMakeup = new LinkedList<>();
        angleSizes = new LinkedList<>();
        angleStarts = new LinkedList<>();
        total = 0;
        double temp;
        double funds = currentUser.getFunds();
        portfolioMakeup.add("Personal Funds");
        makeupValue.add(funds);
        total += funds;


        //getting the value of all shares
        for (Stock stock:currentUser.getAllStocks())
        {
            portfolioMakeup.add(stock.getName());
            temp = stock.getValue("") * stock.getShares();
            makeupValue.add(temp);
            total += temp;
        }

        //getting the scaling factor of the pie chart
        this.scalingFac = 360/total;
        //translating share value into starting and finishing angles

        int angleSize;
        int startingAngle = 0;
        for (double value:makeupValue)
        {
            angleStarts.add(startingAngle);
            angleSize = (int) (value * scalingFac);
            angleSizes.add(angleSize);
            startingAngle+=angleSize;

        }

        int fill = 360 - (angleStarts.getLast() + angleSizes.getLast());
        int fillStart = angleSizes.getLast() + fill;
        angleSizes.removeLast();
        angleSizes.add(fillStart);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        final int L_SPACER = 80;//left spacer size
        final int T_SPACER = 350;
        final int C_SIZE = 300;//chart size
        final int K_T_SPACER = 100;//top spacer for the key
        final int K_SPACER = 10;//the gap between each item in the key (also used for the size of the coloured squares in key)

        int yPos = K_T_SPACER;
        int count = 0;
        String nameAndPerc;

        g.setColor(col.getFontColour());
        g.setFont(new Font("TimesRoman", Font.BOLD , 20));
        g.drawString("Portfolio Value by percentage",L_SPACER+K_SPACER,50);
        g.setFont(new Font("TimesRoman", Font.PLAIN , 12));



        for (String name:portfolioMakeup)
        {
            g.setColor(colors[count%9]);
            g.fillRect(L_SPACER,yPos,K_SPACER,K_SPACER);
            g.setColor(col.getFontColour());
            DecimalFormat df = new DecimalFormat("####0.00");
            nameAndPerc =  name + " : " + df.format((makeupValue.get(count)/total)*100) + "%";
            g.drawString(nameAndPerc,L_SPACER+(2*K_SPACER),yPos + K_SPACER);
            yPos+= 2*K_SPACER;
            count++;
        }


        for (int x = 0;x<angleStarts.size();x++)
        {
            g.setColor(colors[x]);
            g.fillArc(L_SPACER,T_SPACER,C_SIZE,C_SIZE,angleStarts.get(x),angleSizes.get(x));
        }

    }
}
