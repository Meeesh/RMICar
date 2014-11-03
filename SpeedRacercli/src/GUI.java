
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * GUI.java
 *
 * Created on 10-juin-2013, 10:21:22
 */


 
import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Iterator;


/**
 * Graphical user interface (extends JFrame)
 * @author Sam
 * @version 1.0
 */
public class GUI extends javax.swing.JFrame   {

    /**
     * The image to refresh
     */
    public BufferedImage image;

    /**
     * The graphics environment which can be used to draw squares or pictures in the BufferedImage
     */
    public java.awt.Graphics2D g2;

    /**
     * The doubleBuffered JPanel which contains the BufferedImage
     */
    public myJPanel jpBoard;

    /**
     * A copy of the instance of the car that the player controls
     */
    public Car myCar;

    /**
     * Constructor
     */
    public GameClient game;
   
    
    public GUI()
    {
        //Calls the private method which initializes the panels, the buttons, etc...
        initComponents();
       
         
        //Creation of the BufferedImage
        image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);

        //Creation of the JPanel
        jpBoard = new myJPanel(true,image);
        jpBoard.setMinimumSize(new java.awt.Dimension(400, 400));
        jpBoard.setPreferredSize(new java.awt.Dimension(400, 400));

        //This code replaces the automatically generated layout code in such a way to include jpBoard
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jpBoard);
        jpBoard.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 156, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 396, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(208, 208, 208)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jYourScore, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jpBoard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jYourScore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addComponent(jpBoard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );
        pack();

        //The Keyboard listener
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new MyDispatcher());

        //Get the graphics environment and load the title page image
        g2 = jpBoard.image.createGraphics();
        try
        {
            BufferedImage bi = ImageIO.read(this.getClass().getResource("/images/Title_page.png"));
            g2.drawImage(bi, null, 0, 0);
        }
        catch(Exception e)
        {
            //or a black tile if the image is missing
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, 400, 400);
        }

        //Wait for the calibration process to be complete before starting the game
        jButton1.setEnabled(false);

        //Finalize and refresh the display
        jpBoard.setVisible(true);
        jpBoard.repaint();
        this.repaint();
    }

    /**
     * Refreshes the display by adding all the rectangles in the vDisplay vector
     * @param vDisplay The rectangles that must be displayed
     */
    public void refreshGrid(Vector<Rectangle> vDisplay)
    {    
        try
        {
            //For each rectangle in the vector
            Iterator<Rectangle> iDisplay = vDisplay.iterator();
            while(iDisplay.hasNext())
            {
                Rectangle currentRectangle = iDisplay.next();
                if(currentRectangle.id == 0)
                {
                    //Grass land
                    g2.setColor(new Color(34,139,34));
                    g2.fillRect((int)currentRectangle.x, (int)currentRectangle.y, currentRectangle.width, currentRectangle.height);
                }
                else if(currentRectangle.id == 1)
                {
                    //Road segment
                    g2.setColor(Color.BLACK);
                    g2.fillRect((int)currentRectangle.x, (int)currentRectangle.y, currentRectangle.width, currentRectangle.height);
                }
                else if(currentRectangle.id == 2)
                {
                    //White separator or finish line
                    g2.setColor(Color.WHITE);
                    g2.fillRect((int)currentRectangle.x, (int)currentRectangle.y, currentRectangle.width, currentRectangle.height);
                }
                else if(currentRectangle.id == 3)
                {
                    //Road border
                    g2.setColor(Color.GRAY);
                    g2.fillRect((int)currentRectangle.x, (int)currentRectangle.y, currentRectangle.width, currentRectangle.height);
                }
                else if(currentRectangle.id == 4)
                {
                    //Tree
                    BufferedImage bi = ImageIO.read(this.getClass().getResource("/images/tree_orig.png"));
                    if((int)currentRectangle.y == 0)
                    {
                        //Get the displayable sub-image
                        bi = bi.getSubimage(0, 64-currentRectangle.height, currentRectangle.width, currentRectangle.height);
                    }
                    g2.drawImage(bi, null, (int)currentRectangle.x, (int)currentRectangle.y);
                }
                else if(currentRectangle.id == 5)
                {
                    //Concrete block
                    BufferedImage bi = ImageIO.read(this.getClass().getResource("/images/beton.JPG"));
                    if((int)currentRectangle.y == 0)
                    {
                        //Get the displayable sub-image
                        bi = bi.getSubimage(0, 32-currentRectangle.height, currentRectangle.width, currentRectangle.height);
                    }
                    g2.drawImage(bi, null, (int)currentRectangle.x, (int)currentRectangle.y);
                }
                else if(currentRectangle.id == 6)
                {
                    //Player car
                    BufferedImage bi = ImageIO.read(this.getClass().getResource("/images/simple-travel-car-top_view_scaled.png"));
                    if((int)currentRectangle.y == 0)
                    {
                        //Get the displayable sub-image
                        bi = bi.getSubimage(0, 64-currentRectangle.height, currentRectangle.width, currentRectangle.height);
                    }
                    g2.drawImage(bi, null, (int)currentRectangle.x, (int)currentRectangle.y);
                }
                else if(currentRectangle.id == 7)
                {
                    //Opponent car
                    BufferedImage bi = ImageIO.read(this.getClass().getResource("/images/white-car-top-view.png"));
                    if((int)currentRectangle.y == 0)
                    {
                        //Get the displayable sub-image
                        bi = bi.getSubimage(0, 64-currentRectangle.height, currentRectangle.width, currentRectangle.height);
                    }
                    g2.drawImage(bi, null, (int)currentRectangle.x, (int)currentRectangle.y);
                }
                else if(currentRectangle.id == 8)
                {
                    //Civilian car
                    BufferedImage bi = ImageIO.read(this.getClass().getResource("/images/simple-blue-car-top_view.png"));
                    if((int)currentRectangle.y == 0)
                    {
                        //Get the displayable sub-image
                        bi = bi.getSubimage(0, 64-currentRectangle.height, currentRectangle.width, currentRectangle.height);
                    }
                    g2.drawImage(bi, null, (int)currentRectangle.x, (int)currentRectangle.y);
                }
                else if(currentRectangle.id == 10)
                {
                    //Red block (for collision warning)
                    g2.setColor(Color.RED);
                    g2.fillRect((int)currentRectangle.x, (int)currentRectangle.y, currentRectangle.width, currentRectangle.height);
                }
                else if(currentRectangle.id == 11)
                {
                    //Road sign
                    BufferedImage bi = ImageIO.read(this.getClass().getResource("/images/300px-Limite_130.svg.png"));
                    if((int)currentRectangle.y == 0)
                    {
                        //Get the displayable sub-image
                        bi = bi.getSubimage(0, 64-currentRectangle.height, currentRectangle.width, currentRectangle.height);
                    }
                    g2.drawImage(bi, null, (int)currentRectangle.x, (int)currentRectangle.y);
                }
                else if(currentRectangle.id == 12)
                {
                    //Police car
                    BufferedImage bi = ImageIO.read(this.getClass().getResource("/images/police_car.png"));
                    if((int)currentRectangle.y == 0)
                    {
                        //Get the displayable sub-image
                        bi = bi.getSubimage(0, 64-currentRectangle.height, currentRectangle.width, currentRectangle.height);
                    }
                    g2.drawImage(bi, null, (int)currentRectangle.x, (int)currentRectangle.y);
                }
                else if(currentRectangle.id == 14)
                {
                    //Speed indicator
                    BufferedImage bi = ImageIO.read(this.getClass().getResource("/images/Speedometer.png"));
                    boolean bComplete = true;
                    if((int)currentRectangle.y == 0)
                    {
                        //Get the displayable sub-image
                        bi = bi.getSubimage(0, 64-currentRectangle.height, currentRectangle.width, currentRectangle.height);
                        bComplete = false;
                    }
                    g2.drawImage(bi, null, (int)currentRectangle.x, (int)currentRectangle.y);
                    if(bComplete)
                    {
                        //Display the player's speed in the speed indicator
                        int iSpeed = (int)(myCar.ySpeed*50);
                        String sSpeed = new String(iSpeed+"");
                        if(iSpeed <= 130)
                            g2.setColor(Color.GREEN);
                        else
                            g2.setColor(Color.RED);
                        g2.setFont(new Font("Arial", Font.BOLD, 16));
                        g2.drawChars(sSpeed.toCharArray(), 0, sSpeed.length(), (int)currentRectangle.x+2, (int)currentRectangle.y+15);
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Key listener
     */
    public class MyDispatcher implements KeyEventDispatcher {
        /**
         * Listens to KEY_PRESSED and KEY_RELEASED events
         * @param e The triggered key event
         * @return false if the event should be dispatched to the focused component, true otherwise
         */
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                formKeyPressed(e);
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                formKeyReleased(e);
            }
            return false;
        }
    }

    
    /**
     * Called by Core instance. Updates the display according to the given parameters
     * @param vDisplayRoad The road Rectangles to display (layer 1)
     * @param vDisplayObstacles The obstacles warning Rectangles to display (layer 2)
     * @param vDisplayCars The cars Rectangles to display (layer 3)
     * @param myCar Copied object of the player's car
     * @param pos The position (rank) of the player
     * @param nbParticipants The total number of contestants
     * @param bGameOver True if the game is finishing and the game over message should be displayed
     * @param sPosition The position (rank) to display if bGameOver is true
     */
    public void update(Vector<Rectangle> vDisplayRoad, Vector<Rectangle> vDisplayObstacles, Vector<Rectangle> vDisplayCars, Car myCar, int pos, int nbParticipants, boolean bGameOver, int sPosition)
    {
        //Set the player's score
        jYourScore.setText(game.getScore()+"");

        //Updates the kept Car reference and extract its speed
        this.myCar = myCar;
        double Speed = myCar.ySpeed;

        try
        {
            //Displays the rectangles
            refreshGrid(vDisplayRoad);          //Layer 1
            refreshGrid(vDisplayObstacles);     //Layer 2
            refreshGrid(vDisplayCars);          //Layer 3

            //Display the speed in the bottom left corner
            String sSpeed = new String((int)(Speed*50)+" Km/h");
            g2.setColor(Color.BLACK);
            g2.fillRect(5, 358, 100, 30);
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.drawChars(sSpeed.toCharArray(), 0, sSpeed.length(), 10, 380);


            //Display the remaining distance using an orange bar in the bottom right corner
            g2.setColor(Color.BLACK);
            g2.fillRect(290, 328, 110, 30);
            g2.setColor(Color.ORANGE);
            int iDistance = (int)(((int)myCar.y-1200)/415);
            if(iDistance < 0)
                iDistance = 0;
            g2.fillRect(295, 333, iDistance , 20);

            //Display the position (rank) in the bottom right corner (under the distance bar)
            String sPos = new String(pos+"/"+nbParticipants);
            g2.setColor(Color.BLACK);
            g2.fillRect(325, 358, 75, 30);
            g2.setColor(Color.RED);
            g2.drawChars(sPos.toCharArray(), 0, sPos.length(), 330, 380);

            //If we are within 1000 pixels to the finish line
            if(myCar.y < 2200 && !bGameOver)
            {
                //Display the distance to the finish line at the top
                g2.setFont(new Font("Arial", Font.BOLD, 28));
                String sTemp = new String((int)((myCar.y-1200)/3)+"");
                g2.drawChars(sTemp.toCharArray(), 0, sTemp.length(), 180, 30);
            }

            //If we passed the finish line, display the game over sign with final rank information
            if(bGameOver)
            {
                g2.setColor(Color.BLACK);
                g2.fillRect(110, 150, 160, 60);
                g2.setColor(Color.RED);
                String sGameOver = new String("GAME OVER");
                g2.setFont(new Font("Arial", Font.BOLD, 24));
                g2.drawChars(sGameOver.toCharArray(), 0, sGameOver.length(), 120, 180);
                String sGameOver2 = new String("You ranked " + sPosition + " !");
                g2.setFont(new Font("Arial", Font.BOLD, 18));
                g2.drawChars(sGameOver2.toCharArray(), 0, sGameOver2.length(), 120, 200);
            }

            //If we are busted by the police
            if(myCar.bustedTime > 0)
            {
                //Prepare the black background rectangle
                g2.setColor(Color.BLACK);
                g2.fillRect(50, 50, 300, 300);

                //Write "BUSTED" in the top right corner of the black rectangle
                g2.setColor(Color.RED);
                String sGameOver = new String("BUSTED!!!");
                g2.setFont(new Font("Arial", Font.BOLD, 30));
                g2.drawChars(sGameOver.toCharArray(), 0, sGameOver.length(), 190, 110);

                //Display the image of the policeman in the top left corner of the black rectangle
                BufferedImage bi = ImageIO.read(this.getClass().getResource("/images/Angry_policeman.png"));
                g2.drawImage(bi, null, 60, 60);

                //Display the policeman speech
                g2.setColor(Color.WHITE);
                sGameOver = new String("\"Easy on the gas, boy!\"");
                g2.setFont(new Font("Arial", Font.BOLD, 24));
                g2.drawChars(sGameOver.toCharArray(), 0, sGameOver.length(), 60, 200);

                //Display the speed
                sGameOver = new String("You were controlled at " + myCar.bustedSpeed + " Km/h");
                g2.setFont(new Font("Arial", Font.BOLD, 18));
                g2.drawChars(sGameOver.toCharArray(), 0, sGameOver.length(), 60, 230);

                //Display the fine
                sGameOver = new String("You must pay $" + (50+(myCar.bustedSpeed-130)*10));
                g2.setFont(new Font("Arial", Font.BOLD, 18));
                g2.drawChars(sGameOver.toCharArray(), 0, sGameOver.length(), 60, 260);

                //Display the warning
                sGameOver = new String("Watch out for traffic signs!");
                g2.setFont(new Font("Arial", Font.BOLD, 18));
                g2.drawChars(sGameOver.toCharArray(), 0, sGameOver.length(), 60, 290);
            }

            //If game is finished, the "Play" button can be pushed again
            if(!game.isbGameInProgress())
            {
                jButton1.setEnabled(true);
            }

            //Refresh the display
            this.repaint();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the frame content
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jYourScore = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        jButton1.setText("Play");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Your Score");

        jYourScore.setText("0");
        jYourScore.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(208, 208, 208)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jYourScore, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(261, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jYourScore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 454, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Private method called when the "Play" Button has been pressed
     * @param evt The corresponding ActionEvent
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        //The button cannot be pushed while a game is in progress
        jButton1.setEnabled(false);

        //Reset the score
        game.setScore(0);

        //Initisalize the grid on the server's side
        //SpeedRacer.cCore.newGrid();
       /* try{
          game.newGrid();
           
            
        }catch(Exception e) {
            
            e.printStackTrace();
        }*/
       
        game.setbGameFinishing(false);
        game.setbGameInProgress(true);

    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * Private method called when the window is closing
     * @param evt The corresponding WindowEvent
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // Warn the server that we closed the GUI and that it can stop
       
   
        game.setbGameInProgress(false);
        game.setbGameQuit(true);
         game.unregister();

        //Delete the GUI
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    /**
     * Private method called when a key is being pressed
     * @param evt The corresponding KeyEvent
     */
    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed

        //If the game is running, the car has been displayed once and we are not currently busted
        if(game.isbGameInProgress() && myCar != null && myCar.bustedTime == 0)
        {
            switch(evt.getKeyCode())
            {
                case KeyEvent.VK_LEFT : game.setLE_P(true);   //Left arrow pressed
                                    break;
                case KeyEvent.VK_RIGHT : game.setRI_P(true);  //Right arrow pressed
                                    break;
                case KeyEvent.VK_UP : game.setUP_P(true);     //Up arrow pressed
                                    break;
                case KeyEvent.VK_DOWN : game.setDO_P(true);   //Down arrow pressed
                                    break;
                default : break;
            }
        }
      
    }//GEN-LAST:event_formKeyPressed

    /**
     * Private method called when a key is released
     * @param evt The corresponding KeyEvent
     */
    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased

        //If the game is running, the car has been displayed once and we are not currently busted
        if(game.isbGameInProgress() && myCar != null && myCar.bustedTime == 0)
        {
            switch(evt.getKeyCode())
            {
                case KeyEvent.VK_LEFT : game.setLE_P(false);  //Left arrow released
                                    break;
                case KeyEvent.VK_RIGHT : game.setRI_P(false); //Right arrow released
                                    break;
                case KeyEvent.VK_UP : game.setUP_P(false);    //Up arrow released
                                    break;
                case KeyEvent.VK_DOWN : game.setDO_P(false);  //Down arrow released
                                    break;
                default : break;
            }
        }
    }//GEN-LAST:event_formKeyReleased
    
    public void setbutton(boolean t){
    
    this.jButton1.setEnabled(t);
}
    
   public void setGameclient(GameClient game){
       
       this.game = game;
   }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField jYourScore;
    // End of variables declaration//GEN-END:variables

}

