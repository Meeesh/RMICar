




import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
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
public interface ICore extends Remote{
    
   
   

    

   
    public void newGrid(int gamegui)throws RemoteException;
    
    
   public boolean StartGame(int id)throws RemoteException;
    
   public  int register(IGUI gui) throws RemoteException;
   
   public  void unregister (int gui) throws RemoteException;
   
   
    public int getScore(int gamegui)throws RemoteException;

    
    public void setScore(int aScore, int gamegui)throws RemoteException;

   
    public boolean isbGameFinishing(int gamegui) throws RemoteException;

    
    public void setbGameFinishing(boolean abGameFinishing, int gamegui) throws RemoteException;

    
    public boolean isbGameInProgress(int gamegui) throws RemoteException;

   
    public void setbGameInProgress(boolean abGameInProgress, int gamegui) throws RemoteException;

    
    public boolean isbGameQuit(int gamegui)throws RemoteException;

   
    public void setbGameQuit(boolean abGameQuit, int gamegui) throws RemoteException;
    
        
     
    public boolean isUP_P(int gamegui)throws java.rmi.RemoteException ;

    public void setUP_P(boolean aUP_P, int gamegui)throws java.rmi.RemoteException ;

    
    public boolean isDO_P(int gamegui)throws java.rmi.RemoteException ;

  
    public void setDO_P(boolean aDO_P, int gamegui) throws java.rmi.RemoteException;
   
    public boolean isRI_P(int gamegui)throws java.rmi.RemoteException ;

   
    public void setRI_P(boolean aRI_P, int gamegui) throws java.rmi.RemoteException;

   
    public boolean isLE_P(int gamegui)throws java.rmi.RemoteException ;

  
    public void setLE_P(boolean aLE_P, int gamegui)throws java.rmi.RemoteException ;
 
    
   
}
