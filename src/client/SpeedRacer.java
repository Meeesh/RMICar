package client;
import server.Core;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Entry point of the program. Launches everything
 * @author Sam
 * @version 1.0
 */
public class SpeedRacer {

    /**
     * A reference to the instance of Core, which does most of the computations
     */
    public static Core cCore = null;

    /**
     * A reference to the instance of GUI, which is in charge of displaying the game status and tracks the pressed keys an buttons
     */
    public static GUI gGUI = null;

    /**
    * The starting point of the program
    * @param args the command line arguments
    */
    public static void main(String[] args) {

        try
        {
            //The GUI Thread
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                @Override public void run() {

                    //Create the GUI
                    SpeedRacer.gGUI = new GUI();

                    //Set size and location
                    gGUI.setSize(500, 550);
                    gGUI.setLocation(100, 100);

                    //Makes it visible
                    gGUI.setVisible(true);

                }
            });

            //The Core
            cCore = new Core(gGUI);
            cCore.runGame();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

}
