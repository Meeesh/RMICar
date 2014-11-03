



import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Arnold
 */
public interface IGUI extends Remote {
    
     public void update(Vector<Rectangle> vDisplayRoad, Vector<Rectangle> vDisplayObstacles, Vector<Rectangle> vDisplayCars, Car myCar, int pos, int nbParticipants, boolean bGameOver, int sPosition)throws RemoteException;
    
     public void setbutton(boolean t)throws RemoteException;
             
     public void notifymessage(String mess) throws RemoteException;
     
     public void  createGui()throws RemoteException;
     
      public void  setistartgame(boolean t)throws RemoteException;
      
      public int getCompetitors()throws RemoteException;
      
      public int getnumberplay()throws RemoteException;
     
    
}
