
import java.lang.reflect.InvocationTargetException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
//Ceci importe la classe Scanner du package java.util
import java.util.Scanner; 
//Ceci importe toutes les classes du package java.util
import java.util.*;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Arnold
 */
public class GameClient extends UnicastRemoteObject implements IGUI{
    
    private static GUI gGUI;
    private static ICore myServer;
    private static int idclient;
    private static ArrayList playerList;
    private boolean isgamestart = false;
    private int n = 0 ; //nbre d'adversaires de la partie
   
    
    
    
    
    
    
      public GameClient() throws RemoteException{
    
         super();
         
          //The Core
            connectToServer("rmi://localhost/CoreServer");
           // myServer = new Core();
            
            
           playerList = new ArrayList();
           playerList.add(0, idclient); //ajoute l'id du client dans la liste des joueurs
           myServer.StartGame(idclient); //demarre la partie
           createGui();
          
   

    }
      
      public int getnumberplay(){  //recupere le nombre de joueurs de la partie
          
         Scanner sc = new Scanner(System.in);
          int id;
          id = sc.nextInt();
          return id;
      }
      
      public int getCompetitors(){  //recupere les id des adversaires de la partie
           Scanner sc = new Scanner(System.in);
          int id;
          id = sc.nextInt();
          return id;
          
          
          
        /*  Scanner sc = new Scanner(System.in);int id;
          
          for(int i = 1; i <= n ; i++){
               
               
               while(true){
                   
                    System.out.print("Opponent's ID "+i+":  ");
                    id = sc.nextInt();

                    
                     if(!(playerList.contains(id))){
                         
                         break;
                     }
                     
                     System.err.println("Please enter a different competitor' s ID ");

               } 
               playerList.add(i, id); //ajouter les id dans le tableau
           }
           
          return playerList;*/
          
      }

      public void setistartgame(boolean t){
          
          isgamestart = t;
          
      }
      public void createGui(){
          
           try {
            //The GUI Thread
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                @Override public void run() {

                    //Create the GUI
                   gGUI = new GUI();
                   

                    //Set size and location
                    gGUI.setSize(500, 550);
                    gGUI.setLocation(100, 100);

                    //Makes it visible
                    gGUI.setVisible(true);

                }
            });
            
           
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

          
            //myServer.setGUI(this);
            gGUI.setGameclient(this);            
            //myServer.runGame(this);
           
          
      }
 
   public void update(final Vector<Rectangle> vDisplayRoad, final Vector<Rectangle> vDisplayObstacles, final Vector<Rectangle> vDisplayCars, final Car myCar, final int pos, final int nbParticipants, final boolean bGameOver, final int sPosition){
        try {
            //Ask the GUI to perform its update
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                @Override public void run() {
                    
                    try{
                        gGUI.update(vDisplayRoad, vDisplayObstacles, vDisplayCars, myCar, pos, nbParticipants, bGameOver, sPosition);
                        //doCallbacks();
                    } catch (Exception e) {
                        
                        e.printStackTrace();
                    }
                }
            });
        } catch (InterruptedException ex) {
            Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
       
   }
   
     public void connectToServer(String address)
      {
      //look up the server and store a reference to the returned object in a class variable
      try
         {
         myServer = (ICore) java.rmi.Naming.lookup(address);
         
          
         //give the server a remote reference to myself with which it can call me back and return client id
         idclient = this.myServer.register((IGUI) java.rmi.server.RemoteObject.toStub(this));
         
        
         
         }
         catch(Exception e)
         {
         System.out.println("Help! " + e);
         e.printStackTrace();
         }
      }
     
     public GUI getGUI(){
         
         return gGUI;
         
     }
     
     public void notifymessage(String mess){
         
         System.out.println(mess);
         
     }
 
   
   public void setbutton(boolean t)throws RemoteException{
       
       gGUI.setbutton(t);
   }
   
   public void unregister(){
        try {
            myServer.unregister(idclient);
        } catch (RemoteException ex) {
            Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
   
   

  public void newGrid(){
      
     
      
      try
         {
        myServer.newGrid(idclient);
        
         }
         catch(Exception e)
         {
         System.out.println("Help! " + e);
         e.printStackTrace();
         }
  }
  
    public int getScore(){
        
         int score = 0;
         
         try
         {
           score = myServer.getScore(idclient);
         }
         catch(Exception e)
         {
       
         e.printStackTrace();
         }
        return score;
    }

    
    public void setScore(int aScore){
        
         try
         {
           myServer.setScore(aScore, idclient);
         }
         catch(Exception e)
         {
       
         e.printStackTrace();
         }
    }
    
     public boolean isbGameInProgress() {
         
         boolean b = false;
         
         try
         {
          b= myServer.isbGameInProgress(idclient);
         }
         catch(Exception e)
         {
       
         e.printStackTrace();
         }
         
         return b;
     }

   
    public void setbGameInProgress(boolean abGameInProgress){
        
       
        
        try
         {
           myServer.setbGameInProgress(abGameInProgress, idclient);
         }
         catch(Exception e)
         {
       
         e.printStackTrace();
         }
        
    }
    
      public boolean isbGameFinishing() {
          
            boolean b = false;
         
         try
         {
          b= myServer.isbGameFinishing(idclient);
         }
         catch(Exception e)
         {
       
         e.printStackTrace();
         }
         
         return b;
      }

    
    public void setbGameFinishing(boolean abGameFinishing){
         
           
         
        try
         {
           myServer.setbGameFinishing(abGameFinishing, idclient);
         }
         catch(Exception e)
         {
       
         e.printStackTrace();
         }
        
    }
    
    public boolean isbGameQuit(){
        
         boolean b = false;
         
         try
         {
          b= myServer.isbGameQuit(idclient);
         }
         catch(Exception e)
         {
       
         e.printStackTrace();
         }
         
         return b;
        
    }

   
    public void setbGameQuit(boolean abGameQuit) {
        try
         {
           myServer.setbGameQuit(abGameQuit,idclient);
         }
         catch(Exception e)
         {
       
         e.printStackTrace();
         }
        
        
    }
    
     public boolean isUP_P(){
         
         boolean c = false;
         try
         {
           c = myServer.isUP_P(idclient);
         }
         catch(Exception e)
         {
       
         e.printStackTrace();
         }
         
         return c;
     }

    public void setUP_P(boolean aUP_P){
        
         try
         {
           myServer.setUP_P(aUP_P, idclient);
         }
         catch(Exception e)
         {
       
         e.printStackTrace();
         }
    }

    
    public boolean isDO_P(){
        
         boolean c = false;
         try
         {
           c = myServer.isDO_P(idclient);
         }
         catch(Exception e)
         {
       
         e.printStackTrace();
         }
         
         return c;
    }

  
    public void setDO_P(boolean aDO_P) {
        
          try
         {
           myServer.setDO_P(aDO_P, idclient);
         }
         catch(Exception e)
         {
       
         e.printStackTrace();
         }
    }
   
    public boolean isRI_P() {
        
         boolean c = false;
         try
         {
           c = myServer.isRI_P(idclient);
         }
         catch(Exception e)
         {
       
         e.printStackTrace();
         }
         
         return c;
    }

   
    public void setRI_P(boolean aRI_P) {
        
          try
         {
           myServer.setRI_P(aRI_P, idclient);
         }
         catch(Exception e)
         {
       
         e.printStackTrace();
         }
    }

   
    public boolean isLE_P() {
        
         boolean c = false;
         try
         {
           c = myServer.isLE_P(idclient);
         }
         catch(Exception e)
         {
       
         e.printStackTrace();
         }
         
         return c;
        
    }

  
    public void setLE_P(boolean aLE_P){
        
          try
         {
           myServer.setLE_P(aLE_P, idclient);
         }
         catch(Exception e)
         {
       
         e.printStackTrace();
         }
    }
 
  

      
 public static void main(String[ ] args) {         
         
          try {
              
            IGUI gameClient = new GameClient();
           

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
    


    

