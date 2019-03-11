package Application;

import javax.swing.border.Border;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.ImageIcon;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import static Application.loginMenu.getMD5;


class UserInterface
{

    UserInterface()
    {
        JFrame frame;
        //initializes the main menu
        frame = new loginMenu();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible( true );
        frame.pack();
        frame.setLocationRelativeTo(null);

    }


}

class AlertMessage
{

    static void infoBox(String infoMessage) // this is used so that all the pop up boxes in the program are styled in dark mode
    {
        UIManager.put("OptionPane.background", col.getBackgroundColour());
        UIManager.put("Panel.background", col.getBackgroundColour());
        UIManager.put("OptionPane.messageForeground", col.getFontColour());

        ImageIcon errorSign = new ImageIcon("errorSign.png");
        Image image = errorSign.getImage();
        Image transformedError = image.getScaledInstance(40,40,Image.SCALE_SMOOTH);
        errorSign = new ImageIcon(transformedError);

        JButton but = new CustomButton("OK");
        but.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.getRootFrame().dispose();
            }
        });
        JButton[] buttons = { but };
        JOptionPane.showOptionDialog(null, infoMessage, "ALERT!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,errorSign, buttons,buttons[0]);

    }
}
class col{ // this is so that everything in the all the gui uses the same colours

    private static Color nonActiveColour = new Color(0, 0, 0);
    private static Color backgroundColour = new Color(43, 42, 45);
    private static Color fontColour = new Color(199, 204, 216);
    private static Color buttonCol = new Color(64, 64, 64);
    
    static Color getNonActiveColour(){return nonActiveColour;}
    static Color getBackgroundColour(){return backgroundColour;}
    static Color getFontColour(){return fontColour;}
    static Color getButtonCol(){return buttonCol;}
}

class CustomButton extends JButton{ // this is so that all the buttons are the same throughout the gui
    CustomButton(String text){
        super(text);
        setBackground(col.getButtonCol());
        setForeground(col.getFontColour());
        setBorder(new RoundedBorder(6));
    }
}

class RoundedBorder implements Border { // this allows the buttons to have round borders
    private int radius;

    RoundedBorder(int radius) {
        this.radius = radius;
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
    }

    public boolean isBorderOpaque() {
        return true;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.drawRoundRect(x, y, width-1, height-1, radius, radius);
    }

}

class MainMenu extends JFrame // main menu, where all the stuff happens
{

    private User currentUser;
    
    /*
    private Graph graph;
    private Graph MVgraph;
    private MovingAverages MV;
    */
    
    private Graph graph; // line graph
    private StockPieChart chart; // pie chart
    private Stock selected;
    private LinkedList<Stock> stocks = new LinkedList<>();
    private JTextField entryText;
    private JLabel totalOutput;
    private JLabel shareOutput;
    private JTextArea sName;
    private JTextArea table;
    private JTextField fundEntry = new JTextField(20);
    private JLabel fundsOutput = new JLabel();
    private JLabel fundsAvailable;
    private int period = 2;//used to set the graph time period, 1m by default
    private String[] stockList = {"A","AA","AAAU","AABA","AAC","B","BA","BAB","BABA","C","CAAP","CAAS","CABO","CAC","D","DAC",
            "DAG","DAIO","DAKT","E","EA","EAB","EAD","EAE","F","FAAR","FAB","FAD","FAF","G","GAA","GAB","H","HA","HABT",
            "HACK","I","IAC","IAE","IAF","JACK","JAG","JAGX","K","KAI","KALA","KALL","L","LABD","LABL","LABU","M","MA",
            "MAA","NAC","NACP","NAD","O","OAK","OAS","PAA","PAAS","PAC","PACA","QABA","QADA","QAI","R","RA","RAAX","S",
            "SA","SAB","SABR","T","TA","TAC","TACO","UA","UAA","UAE","V","VAC","VALE","W","WAAS","WAB","X","XAN","XAR",
            "XBI","Y","YANG","YCL","Z","ZAGG","ZAYO"}; // all possible stocks that we found and that can be dynamically added to the stock list

    private String[] dStockList = stockList.clone();

    private LinkedList<String> addedStocks = new LinkedList<>();
    private JMenu stockMenu = new JMenu("My Stocks");
    private boolean shareGraph = false;
    private static int stockCount = 4;
    private JMenu stockMenu1 = new JMenu("My Stocks");
    private JTextField stockSearch = new JTextField(20);
    private JComboBox<String> stockDropDown;
    private boolean MAGraph = false;

    private JComboBox<String> dayDropdown;
    private JComboBox<String> monthDropdown;
    private JComboBox<String> yearDropDown;

    private String[] days = {"DD","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
    private String[] months = {"MM","01","02","03","04","05","06","07","08","09","10","11","12"};
    private String[] years = {"YYYY","2014","2015","2016","2017","2018","2019"};



    MainMenu(User u)
    {
        this.currentUser=u; //from here to line 540 is just gui creation

        //set the colour of an active tab
        UIManager.put("TabbedPane.selected",col.getBackgroundColour());
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFocusable(false); // remove the dotted line around the active tab
         //set the background colour of a tab
        tabs.setBackground(col.getNonActiveColour());
        tabs.setForeground(col.getFontColour());
        Font defaultFont = new Font("Serif", Font.PLAIN, 30);
        
        
        //remove the tab styling
        tabs.setUI(new BasicTabbedPaneUI() {
            private final Insets borderInsets = new Insets(0, 0, 0, 0);
            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
            }
            @Override
            protected Insets getContentBorderInsets(int tabPlacement) {
                return borderInsets;
            }
        });
        String[] preAdded = {"AMZN","NFLX","APPL","GOOGL","MSFT"};
        addedStocks.addAll(Arrays.asList(preAdded));
        //Creating and populating the graphTab and its contents
        //Creating the components
        JPanel graphTab = new JPanel(new BorderLayout());
        graphTab.setBackground(col.getBackgroundColour());
        JPanel topBar = new JPanel();
        //creating the left button panel


        JPanel leftBar = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        leftBar.setPreferredSize(new Dimension(400,600));
        c.gridx = 40;
        c.gridy = 0;
        c.gridheight = 20;
        c.gridwidth = 60;
        c.anchor = GridBagConstraints.CENTER;


        leftBar.setBackground(col.getBackgroundColour());
        JPanel stockSearchPan = new JPanel(new GridLayout(2,1));
        JPanel addStocksPan = new JPanel(new GridLayout(1,2));
        stockDropDown = new JComboBox<>(dStockList);
        JButton addStock = new CustomButton("Add Stock");

        stockDropDown.setForeground(col.getFontColour());
        stockDropDown.setBackground(col.getButtonCol());
        addStock.setBackground(col.getButtonCol()); //button styling
        addStock.setForeground(col.getFontColour());
        addStock.setBorder(new RoundedBorder(6));

        addStock.addActionListener(new ButtonHandler(this,8));
        addStocksPan.add(stockDropDown);
        addStocksPan.add(addStock);
        stockSearch.addActionListener(new ButtonHandler(this,9));
        stockSearchPan.add(stockSearch);
        stockSearchPan.add(addStocksPan);
        leftBar.add(stockSearchPan,c);


        JPanel periodSelection = new JPanel(new GridLayout(2,2));
        JButton period_1m = new CustomButton("One Month");
        JButton period_3m = new CustomButton("Three Months");
        JButton period_1y = new CustomButton("One Year");
        JButton period_5y = new CustomButton("Five Years");



        period_1m.addActionListener(new ButtonHandler(this,0));
        period_3m.addActionListener(new ButtonHandler(this,1));
        period_1y.addActionListener(new ButtonHandler(this,2));
        period_5y.addActionListener(new ButtonHandler(this,3));
        periodSelection.add(period_1m);
        periodSelection.add(period_3m);
        periodSelection.add(period_1y);
        periodSelection.add(period_5y);

        c.gridy = 40;
        c.gridwidth = 60;
        c.gridheight = 60;

        leftBar.add(periodSelection,c);


        JButton viewPortfolioGraph = new CustomButton("View Portfolio Value History");
        viewPortfolioGraph.addActionListener(new ButtonHandler(this,4));

        c.gridy = 100;

        leftBar.add(viewPortfolioGraph,c);


        JButton toggleMA = new CustomButton("Display Moving Average");
        toggleMA.addActionListener(new ButtonHandler(this,10));

        c.gridy = 200;
        leftBar.add(toggleMA,c);

        graphTab.add(leftBar,BorderLayout.WEST);

        topBar.setBackground(col.getBackgroundColour());
        totalOutput = new JLabel();
        shareOutput = new JLabel();
        totalOutput.setForeground(col.getFontColour());
        shareOutput.setForeground(col.getFontColour());
        totalOutput.setFont(defaultFont);
        shareOutput.setFont(defaultFont);
        JMenuBar stockBar = new JMenuBar();


        JMenuItem stock1 = new JMenuItem("NFLX");
        JMenuItem stock2 = new JMenuItem("AMZN");
        JMenuItem stock3 = new JMenuItem("GOOGL");
        JMenuItem stock4 = new JMenuItem("APPL");
        JMenuItem stock5 = new JMenuItem("MSFT");


        //Adding action listeners to the buttons
        stock1.addActionListener(new ButtonHandler(this,100));
        stock2.addActionListener(new ButtonHandler(this,101));
        stock3.addActionListener(new ButtonHandler(this,102));
        stock4.addActionListener(new ButtonHandler(this,103));
        stock5.addActionListener(new ButtonHandler(this,104));


        //Adding the components to their parents

        stockMenu.add(stock1);
        stockMenu.add(stock2);
        stockMenu.add(stock3);
        stockMenu.add(stock4);
        stockMenu.add(stock5);

        stockMenu.setFont(defaultFont);
        stockBar.add(stockMenu);
        topBar.add(stockBar);
        topBar.add(new JLabel("      "));//temp spacer
        topBar.add(shareOutput);
        topBar.add(new JLabel("      "));//temp spacer
        topBar.add(totalOutput);
        graphTab.add(topBar, BorderLayout.NORTH);

        //Populating the stock list, in the future we aim to do this dynamically

        stocks.add(new Stock("NFLX"));
        stocks.add(new Stock("AMZN"));
        stocks.add(new Stock("GOOGL"));
        stocks.add(new Stock("AAPL"));
        stocks.add(new Stock("MSFT"));




        //Finishing touches + graph
        selected = stocks.get(0);
        graph = new Graph(stocks.get(0).getName(),stocks.get(period).getOpens(period),stocks.get(0).getDates(period));

        /*

        */
        
        graph.addMouseListener(new mouseHandler(this));
        double total = 0;
        for (Stock temp:currentUser.getAllStocks())
        {
            total += (temp.getValue("") * temp.getShares());
        }

        shareOutput.setText("Share Value: "+ String.format("%.2f",total));
        totalOutput.setText("Portfolio Value: " + String.format("%.2f",total+currentUser.getFunds()));
        graphTab.add(this.graph,BorderLayout.CENTER);
        
        //styling string for tab
        String style = "width:720px;height:15px;"
                + "text-align:center;border:none;"+
                "padding:5px;"+
                "font-size: 10px";
        
        String prehtml = "<html><body style = '"+style+"'>";
        String posthtml =  "</body></html>";
        
        tabs.addTab(prehtml + "Graph" + posthtml ,graphTab);
        add(tabs);

        //Creating and populating the shareTab
        JPanel shareTab = new JPanel(new BorderLayout());
        shareTab.setBackground(col.getBackgroundColour());
        table = new JTextArea();
        table.setBackground(col.getBackgroundColour());
        table.setFont(new Font("monospaced", Font.PLAIN, 10));
        table.setPreferredSize(new Dimension(900,200));
        table.setForeground(col.getFontColour());
        entryText = new JTextField(10);

        JPanel dateEntry = new JPanel(new GridLayout(1,3));
        setForeground(col.getFontColour());
        dayDropdown = new JComboBox<>(days);
        monthDropdown = new JComboBox<>(months);
        yearDropDown = new JComboBox<>(years);
        dateEntry.add(dayDropdown);
        dateEntry.add(monthDropdown);
        dateEntry.add(yearDropDown);

        sName = new JTextArea(1,10);
        sName.setEditable(false);
        sName.setText(this.selected.getName());
        sName.setForeground(col.getFontColour());
        JPanel topBar1 = new JPanel();
        topBar1.setBackground(col.getBackgroundColour());
        JMenuBar stockBar1 = new JMenuBar();

        JMenuItem stock1_1 = new JMenuItem("Netflix");
        JMenuItem stock1_2 = new JMenuItem("Amazon");
        JMenuItem stock1_3 = new JMenuItem("Google");
        JMenuItem stock1_4 = new JMenuItem("Apple");
        JMenuItem stock1_5 = new JMenuItem("Microsoft");


        JButton print_to_pdf = new CustomButton("Print to PDF");
        print_to_pdf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportAsPDF doc = new exportAsPDF(currentUser);

            }
        });



        JButton buyShares = new CustomButton("Buy Shares");
        JButton sellShares = new CustomButton("Sell Shares");


        fundsAvailable = new JLabel("Funds: " + String.format("%.2f",currentUser.getFunds()));
        fundsAvailable.setForeground(col.getFontColour());
        //JButton refreshFunds = new CustomButton("Refresh funds");
        ButtonHandler ntflx = new ButtonHandler(this,100);
        ntflx.printShareInfo();
        stock1_1.addActionListener(ntflx);
        stock1_2.addActionListener(new ButtonHandler(this,101));
        stock1_3.addActionListener(new ButtonHandler(this,102));
        stock1_4.addActionListener(new ButtonHandler(this,103));
        stock1_5.addActionListener(new ButtonHandler(this,104));

        buyShares.addActionListener(new ButtonHandler(this,5));
        sellShares.addActionListener(new ButtonHandler(this,6));




        topBar1.add(fundsAvailable);
        stockMenu1.add(stock1_1);
        stockMenu1.add(stock1_2);
        stockMenu1.add(stock1_3);
        stockMenu1.add(stock1_4);
        stockMenu1.add(stock1_5);
        stockMenu1.setFont(defaultFont);
        stockBar1.add(stockMenu1);
        topBar1.add(stockBar1);

        topBar1.add(new JLabel("      "));

        topBar1.add(print_to_pdf);
        topBar1.add(new JLabel("      "));

        topBar1.add(sName);
        topBar1.add(new JLabel("      "));
        topBar1.add(buyShares);
        topBar1.add(new JLabel("      "));
        topBar1.add(sellShares);
        JLabel text1 = new JLabel("      Shares: ");
        text1.setForeground(col.getFontColour());
        topBar1.add(text1);
        topBar1.add(entryText);
        JLabel text2 = new JLabel("       ");
        text2.setForeground(col.getFontColour());
        topBar1.add(text2);
        topBar1.add(dateEntry);
        shareTab.add(topBar1,BorderLayout.NORTH);
        shareTab.add(table,BorderLayout.WEST);



        setVisible(false);
        chart = new StockPieChart(currentUser);
        chart.setPreferredSize(new Dimension(500,1000));
        JPanel chartPan = new JPanel();
        chartPan.add(chart,BorderLayout.EAST);
        chartPan.setBackground(col.getBackgroundColour());
        shareTab.add(chartPan);
        tabs.addTab(prehtml + "Share" + posthtml, shareTab);



        JPanel userSettings = new JPanel(new GridLayout(2,1));
        userSettings.setBackground(col.getBackgroundColour());
        userSettings.setForeground(col.getFontColour());
        tabs.addTab(prehtml + "User Settings"+posthtml,userSettings);

        JPanel buttonPan = new JPanel();
        buttonPan.setBackground(col.getBackgroundColour());

        JButton updateBut = new CustomButton("Update Details");


        JPanel outputPan = new JPanel();
        JPanel fundPan = new JPanel();
        fundPan.setForeground(col.getFontColour());
        fundPan.setBackground(col.getBackgroundColour());
        JPanel updateFunds = new JPanel(new GridLayout(2,1));
        updateFunds.setBackground(col.getBackgroundColour());
        updateFunds.setForeground(col.getFontColour());
        JPanel userPassChange = new JPanel();
        userPassChange.setForeground(col.getFontColour());
        userPassChange.setBackground(col.getBackgroundColour());
        JPanel eMail = new JPanel();
        eMail.setBackground(col.getBackgroundColour());
        JLabel textEmail = new JLabel("Email:");
        textEmail.setForeground(col.getFontColour());
        JTextField userEmail = new JTextField(20);

        eMail.add(textEmail);
        eMail.add(userEmail);


        JPanel pass = new JPanel();
        pass.setBackground(col.getBackgroundColour());
        JLabel textPass = new JLabel("Password:");
        textPass.setForeground(col.getFontColour());
        JTextField userPass = new JTextField(20);

        pass.add(textPass);
        pass.add(userPass);

        updateBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newUserEmail = userEmail.getText();
                String newUserPass = userPass.getText();

                if(newUserEmail.equals("")){
                    newUserEmail=currentUser.getUserName();
                }
                if (newUserPass.equals("")){
                    AlertMessage.infoBox("Please input new or old password to confirm the changes!");} //needs pass or else user wont be able to log in properly
                else{
                    ReadWriteData.deleteOldFile(currentUser.getUserName());

                    currentUser.setUserName(newUserEmail);
                    try
                    {
                        currentUser.setPassword(getMD5(newUserPass));
                    }
                    catch(Exception k){AlertMessage.infoBox(k.toString());}

                    ReadWriteData.writeData(currentUser);
                }
            }
        });

        buttonPan.add(updateBut);

        userPassChange.add(eMail);
        userPassChange.add(pass);
        userPassChange.add(buttonPan);

        JButton addFunds = new CustomButton("Add funds");
        addFunds.addActionListener(new ButtonHandler(this,7));
        fundsOutput.setBackground(col.getBackgroundColour());
        fundsOutput.setForeground(col.getFontColour());
        fundsOutput.setText("Current funds: "+String.format("%.2f",currentUser.getFunds()));

        fundPan.add(fundEntry);
        fundPan.add(addFunds);

        outputPan.setBackground(col.getBackgroundColour());
        updateFunds.add(fundPan);
        outputPan.add(fundsOutput);
        updateFunds.add(outputPan);


        userSettings.add(userPassChange);
        userSettings.add(updateFunds);

        ImageIcon logo = new ImageIcon("logo.jpg");
        setIconImage(logo.getImage());

        int frameWidth = 1920;
        int frameHeight = 1080;
        setPreferredSize(new Dimension(frameWidth, frameHeight));

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible( true );
        setExtendedState(Frame.MAXIMIZED_BOTH);
        pack();
        setLocationRelativeTo(null);



    }

    private void addToMyStocks(Stock stock)
    {
        stockCount++;
        stocks.add(stock);
        JMenuItem stockTemp = new JMenuItem(stock.getName());
        JMenuItem stockTemp1 = new JMenuItem(stock.getName());
        stockTemp.addActionListener(new ButtonHandler(this,100+stockCount));
        stockTemp1.addActionListener(new ButtonHandler(this,100+stockCount));
        stockMenu.add(stockTemp);
        stockMenu1.add(stockTemp1);
    }

    class mouseHandler implements MouseListener
    {

        MainMenu menu;

        mouseHandler(MainMenu menu)
        {
            this.menu = menu;
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
            menu.graph.mousePos(e.getX());
        }

        @Override
        public void mouseEntered(MouseEvent e) { }
        @Override
        public void mouseExited(MouseEvent e) { }
        @Override
        public void mouseReleased(MouseEvent e) { }
        @Override
        public void mousePressed(MouseEvent e) { }
    }

    class ButtonHandler implements ActionListener
    {

        MainMenu menu;
        int butAction;


        ButtonHandler(MainMenu menTmp, int butAction)
        {
            this.menu = menTmp;
            this.butAction = butAction;
        }

        public void actionPerformed(ActionEvent a)
        {
            if (butAction <= 3){
                //change graph period
                menu.period = butAction;
                if (shareGraph){menu.graph.changeGraph("Total Share Price History",currentUser.getPriceHistory(period),currentUser.getAllStocks().get(0).getDates(period));
                }else if (MAGraph){
                    showMAGraph();
                }else{
                    menu.graph.changeGraph(menu.selected.getName(),menu.selected.getOpens(menu.period),menu.selected.getDates(menu.period));
                }

            }else if (butAction == 4){
                //view share value history
                MAGraph = false;
                shareGraph = true;
                menu.graph.changeGraph("Total Share Price History",currentUser.getPriceHistory(period),currentUser.getAllStocks().get(0).getDates(period));
            }else if(butAction == 5){
                //buy shares
                buyShares();
                ReadWriteData.writeData(currentUser);
            }else if (butAction == 6){
                //sell shares
                sellShares();
                ReadWriteData.writeData(currentUser);
            }else if (butAction == 7){
                //add funds
                try{
                    double fundsToAdd = Double.parseDouble(fundEntry.getText());
                    currentUser.addFunds(fundsToAdd);
                    ReadWriteData.writeData(currentUser);

                }catch(NumberFormatException e){
                   AlertMessage.infoBox("Invalid entry!");
                }


            }else if(butAction == 8){
                //add stock to my stocks
                String nameTemp = dStockList[stockDropDown.getSelectedIndex()];
                if (addedStocks.contains(nameTemp))
                {
                    AlertMessage.infoBox("Selected stock is already added");
                }else{
                    addedStocks.add(nameTemp);
                    addToMyStocks(new Stock(nameTemp));
                }

            }else if(butAction == 9){
                //update dropdown with stocks matching searchtext
                String searchtext = stockSearch.getText();
                updateStockDropDown(searchtext);

            }else if(butAction == 10){
                //toggle MA display
                if (!MAGraph){
                    MAGraph = true;
                    showMAGraph();
                }else {
                    MAGraph=false;
                    menu.graph.changeGraph(menu.selected.getName(),menu.selected.getOpens(menu.period),menu.selected.getDates(menu.period));
                }


            }else if (butAction >= 100)
            {
                //Change selected stock and update graph
                shareGraph = false;
                menu.selected = menu.stocks.get(butAction-100);

                if (MAGraph){
                    showMAGraph();
                }else{
                    menu.graph.changeGraph(menu.selected.getName(),menu.selected.getOpens(menu.period),menu.selected.getDates(menu.period));
                }

            }


            double total = 0;
            for (Stock temp:currentUser.getAllStocks())
            {
                total += (temp.getValue("") * temp.getShares());
            }

            //updates the share tab

            fundsOutput.setText("Current funds: "+String.format("%.2f",currentUser.getFunds()));
            menu.fundsAvailable.setText("Funds: " + String.format("%.2f",currentUser.getFunds()));
            menu.shareOutput.setText("Share Value: " + String.format("%.2f",total));
            menu.totalOutput.setText("Portfolio Value: " + String.format("%.2f",total+menu.currentUser.getFunds()));
            menu.sName.setText(menu.selected.getName());
            printShareInfo();
            chart.updateChart();
            menu.repaint();
        }

        private void showMAGraph()
        {
            int[] smothingFacs = {2,6,25,125};
            MovingAverages MV = new MovingAverages(smothingFacs[period]);
            String name = selected.getName();
            LinkedList<Double> yVals = selected.getOpens(period);
            LinkedList<Double> MA_yVals  = MV.plotDataY(yVals);
            LinkedList<String> xVals = selected.getDates(period);
            graph.changeGraph(name,yVals,MA_yVals,xVals);
        }

        private void updateStockDropDown(String searchtext)
        {
            dStockList = new String[stockList.length];
            int count = 0;
            searchtext = searchtext.toUpperCase();

            for (String stock:stockList)
            {
                if (stock.startsWith(searchtext))
                {
                    dStockList[count] = stock;
                    count++;
                }
            }

            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(dStockList);
            stockDropDown.setModel(model);
        }

        private void buyShares()
        {
            String date = getEntryDate();
            if (checkValidDate(date)){
                Double salePrice = menu.selected.getValue(date);
                Double shares = Double.parseDouble(menu.entryText.getText());
                if (salePrice == -0.1)
                {
                   AlertMessage.infoBox("Specified date not found!");
                }else if (currentUser.getFunds() < salePrice*shares){
                    double missing = salePrice*shares - currentUser.getFunds();
                    AlertMessage.infoBox("You must have funds to buy stocks! \n You are missing £" + missing);}

                else{
                    if(currentUser.getStock(menu.selected.getName()) == null){
                        currentUser.addStock(menu.selected);
                    }
                    currentUser.getStock(menu.selected.getName()).changeInvested( salePrice*shares);
                    currentUser.changeShares(menu.selected.getName(),shares,true,salePrice);


                }

            }else{
                AlertMessage.infoBox("The date entered is invalid");
            }
        }


        private void sellShares()
        {
            String date = getEntryDate();
            if (checkValidDate(date)){
                double salePrice = menu.selected.getValue(date);
                double shares = Double.parseDouble(menu.entryText.getText());
                if (salePrice == -0.1)
                {
                    AlertMessage.infoBox("Specified date not found!");
                }else if(currentUser.getStock(menu.selected.getName()).getShares() < shares){
                //else if (menu.selected.shares < shares){
                    AlertMessage.infoBox("You must own shares to sell them!");
                }else{
                    currentUser.getStock(menu.selected.getName()).setSales(shares);
                    currentUser.changeShares(menu.selected.getName(),shares,false,salePrice);
                }

            }else{
                AlertMessage.infoBox("The date entered is invalid");
            }
        }

        private String getEntryDate()
        {
            return years[menu.yearDropDown.getSelectedIndex()] + "-" + months[menu.monthDropdown.getSelectedIndex()] + "-" + days[dayDropdown.getSelectedIndex()];
        }


        boolean checkValidDate(String date)
        {

            return date.matches("([0-9]{4}+)-([0-9]{2}+)-([0-9]{2}+)");

        }

        void printShareInfo()
        {
            String[] colNames ={"Stock","Shares","Invested","Sold","Share Value","Current Profits","Percentage Change"};
            StringBuilder table = new StringBuilder();
            Formatter form = new Formatter(table);
            double tempVal;
            menu.table.setText("");
            for (String temp:colNames){
                form.format("%-20s|", temp);
            }
            table.append("\n");
            //populate the table depending on user stocks
            for (Stock stock:currentUser.getAllStocks())
            {
                if (stock.getShares()> 0){
                    form.format("%-20s|", stock.getName().toLowerCase());
                    form.format("%-20s|", String.format("%.2f",stock.getShares()));
                    form.format("%-20s|", String.format("%.2f",stock.getInvested()));
                    form.format("%-20s|", String.format("%.2f",stock.getSales()));
                    tempVal = stock.getValue("") * stock.getShares();
                    form.format("%-20s|", String.format("%.2f",tempVal));
                    tempVal = tempVal + stock.getSales() - stock.getInvested();
                    form.format("%-20s|", String.format("%.2f",tempVal));
                    tempVal += stock.getInvested();
                    tempVal = 100 - ((tempVal/stock.getInvested()) * 100);
                    if (tempVal > 0)form.format("%-20s|", "-" + String.format("%.2f",tempVal) + "%");
                    else form.format("%-20s|", "+" + String.format("%.2f",-tempVal) + "%");
                    table.append("\n");
                }
            }
            menu.table.setText(table.toString());
        }
    }
}


class loginMenu extends JFrame
{

    private JTextField userEmail = new JTextField(20);
    private JTextField userPass = new JTextField(20);


    loginMenu()
    {


        JPanel main = new JPanel();
        setPreferredSize(new Dimension(400, 200));
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(col.getBackgroundColour());

        JPanel buttonPan = new JPanel();
        buttonPan.setBackground(col.getBackgroundColour());

        JButton loginBut = new CustomButton("Login");
        loginBut.addActionListener(new loginHandler(this,0));

        JButton createBut = new CustomButton("Create Account");



        createBut.addActionListener(new loginHandler(this,1));

        JPanel eMail = new JPanel();
        eMail.setBackground(col.getBackgroundColour());
        JLabel textEmail = new JLabel("Email:");
        textEmail.setForeground(col.getFontColour());

        eMail.add(textEmail);
        eMail.add(userEmail);

        JPanel pass = new JPanel();
        pass.setBackground(col.getBackgroundColour());
        JLabel textPass = new JLabel("Password:");
        textPass.setForeground(col.getFontColour());

        pass.add(textPass);
        pass.add(userPass);

        buttonPan.add(loginBut);
        buttonPan.add(createBut);

        JPanel msg = new JPanel();

        msg.setBackground(col.getBackgroundColour());

        main.add(eMail);
        main.add(pass);
        main.add(buttonPan);
        main.add(msg);



        ImageIcon logo = new ImageIcon("logo.jpg");
        setIconImage(logo.getImage());
        setTitle("Please login to continue");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        add(main);

    }



    public class loginHandler implements ActionListener
    {
        int butAction;
        loginMenu theApp;
        private String fileName;
        private User userToPass;

        loginHandler(loginMenu theApp,int butAction)
        {
            this.theApp = theApp;
            this.butAction = butAction;
        }

        private void setFileName(String userName){
            fileName = "accountData\\" + userName +".txt";
        }
        private void setUserToPass(User u){
            userToPass = u; //sets the user so that the main menu knows what account to use
        }


        public void actionPerformed(ActionEvent a)
        {
            if (butAction == 0)
            {
                //login
                if (checkLoginDetails(theApp.userEmail.getText(),theApp.userPass.getText()))
                {

                   loginSuccess(userToPass);

                }else{
                    AlertMessage.infoBox("Incorrect login information");
                }

            }else if(butAction == 1)
            {
                // new account creation
                if (createAccount(theApp.userEmail.getText(),theApp.userPass.getText()))
                {
                    AlertMessage.infoBox("Account creation successful");
                    loginSuccess(userToPass);
                }else{
                    AlertMessage.infoBox("Account creation unsuccessful");
                }
            }
        }


        boolean checkLoginDetails(String userEmail, String userPassword)
        {
            //checks the account csv for the presence of entered details (userEmail & MD5 of password)
            //returns true if details correct
            if(getAccount(userEmail)){
                ReadWriteData write = new ReadWriteData();
                User u = write.readData(userEmail);
                try {
                    if((u.getPass().equals(getMD5(userPassword)))){
                        setUserToPass(u);
                    return true;}
                } catch (NoSuchAlgorithmException e) {
                    AlertMessage.infoBox("Error when encrypting passphrase");

                }
            }
            return false;
        }

        boolean isValidEmail(String email)
        {
           //checks if the user has supplied a email. this is done by chescking if the submitted string matches what a typical email would look like


            String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
            java.util.regex.Matcher m = p.matcher(email);

            if(!m.matches())
            {
                AlertMessage.infoBox("Email is not valid");
            }


            return m.matches();
            
        }

        boolean isValidPass(String pass)
        {
            //just checks that the pass is the correct length and matches the regex (Should be letters with at least one upper case and one number)

            boolean valid = (pass.length() > 5);

            if (!valid)
            {
                AlertMessage.infoBox("Password must be at least 6 characters in length and contain at least one upper case letter and one number");
            }
            return valid;

        }

        boolean createAccount(String email, String password)
        {
            //Attempts to create the new account, returns true if successful
            if (isValidEmail(email) && isValidPass(password) && !checkLoginDetails(email,password))
            {

                try{
                    User user = new User(email,getMD5(password)); //will make a new user and pass it if created successfully
                    ReadWriteData.writeData(user);
                    setUserToPass(user);
                    return true;

                }catch (NoSuchAlgorithmException f){
                   AlertMessage.infoBox("Error when encrypting password!");
                }
            }
            return false;
        }

        void loginSuccess(User u)
        {
            //if the user is successful in logging in, then the sign in menu closes and the main program opens
            theApp.dispatchEvent(new WindowEvent(theApp, WindowEvent.WINDOW_CLOSING));
            MainMenu UI = new MainMenu(u);

            UI.setVisible(true);
        }

        boolean getAccount(String userName)
        {
            //checks if the user exists
            setFileName(userName);
            File tempFile = new File(fileName);
            return tempFile.exists();
        }

    }

    static String getMD5(String plaintext) throws NoSuchAlgorithmException
    //password protection. when the user signs in the password they give is also put through this function, then
            //its checked if they match
    {

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(plaintext.getBytes(StandardCharsets.UTF_8));

        StringBuilder encText = new StringBuilder();

        for (byte b:hash)
        {
            encText.append(String.format("%02x", b));
        }

        return encText.toString();

    }


}




