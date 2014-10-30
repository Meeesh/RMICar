

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Arnold
 */



import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService; 
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;



public class CoreServer extends UnicastRemoteObject implements ICore {
    
    private static Core CurrentCore;
    private static boolean serverisup; 
    private static boolean ispresent;
    private static Hashtable<Integer, Core> htIdCore; //hashtable contenant le stub client et le core auquel il est associé
    private static Hashtable<Integer, IGUI> htIdStub; //hashtable contenant le stub client et l'id associé
    //private static ExecutorService executor;
    private static  ExecutorService executor;
    private static int nextID = 0;
    //private static ArrayBlockingQueue<IGUI> guiQ;
     private HashSet<IGUI> clients;

    private Timer timer;
    
    
    public CoreServer() throws Exception{
        
        super();
         
        String  registryURL;
      
    try{     
      Properties props = System.getProperties(); 
      props.setProperty("java.rmi.server.codebase", "file:///Users//raul//Documents//NetBeansProjects//SpeedRacerServer");
      props.setProperty("java.security.policy", "server.policy");
      startRegistry(1099);
      //ICore exportedObj =  new Core();
      registryURL =  "rmi://localhost/CoreServer";
      Naming.rebind(registryURL,(CoreServer) this);
      serverisup = false;
      ispresent = false;
       
     }// end try
    catch (Exception re) {    
        
        System.out.println("Server Exception " + re);  
        re.printStackTrace();
            System.exit(1);
    
    } // end catch
    
               htIdStub = new Hashtable<Integer, IGUI>();
               htIdCore = new Hashtable<Integer, Core>();

              // guiQ = new ArrayBlockingQueue<IGUI>(128);// cree une file d'attente pour les clients
               executor = Executors.newFixedThreadPool(2); 
               clients = new HashSet<IGUI>();
    }
    
    public static void main(String[ ] args) {
        

              CoreServer coreserver = null ; int id;

        try {
            // je cree une instance de Coreserver
            coreserver = new CoreServer();
            serverisup = true;
            System.out.println("Server is ready and waiting");
            
            
         
            
        } catch (Exception ex) {
            Logger.getLogger(CoreServer.class.getName()).log(Level.SEVERE, null, ex);
        }
       
     
        }
        
    
    
    
    /* public void remind(int seconds) throws RemoteException{
         
         timer = new Timer();
         timer.schedule(CurrentCore, seconds*10000);
         
     }*/
 //This method starts a RMI registry on the local host, if it does not already exists at the specified port number.
  private static void startRegistry(int RMIPortNum)   throws RemoteException{
    try {
      Registry registry = LocateRegistry.getRegistry("127.0.0.1",RMIPortNum);
      registry.list( );  // This call will throw an exception if the registry does not already exist
    }   catch (RemoteException e) { 
      // No valid registry at that port.
      Registry registry = LocateRegistry.createRegistry(RMIPortNum);}
  } // end startRegistry

   

   
    
    

  
     public int register(IGUI callbackClientObject)throws java.rmi.RemoteException{
     
         int id;
         
         
         synchronized(this) {
            id = ++nextID;
        }
			clients.add(callbackClientObject); //ajoute le client dans le vecteur
                        callbackClientObject.notifymessage("Connecting to the server ...");

			//htIdCore.put(var, CurrentCore);  //creer une entrée gui-core dans la table
                        htIdStub.put(Integer.valueOf(id), callbackClientObject);  // creer une entrée id-gui dans la table
                        //CurrentCore.registerForCallback(var);  //enrégistrer le gui auprès de son core
			System.out.println( "player with ID: "+ id +" has joined the server. " );
                        callbackClientObject.notifymessage("your ID number is :"+id);
                        
        return id;                
               
   }
     
     public  boolean StartGame(int idcl) throws RemoteException{
         
         //ArrayList playerList = new ArrayList();  
         int nbreadv = 0 ;
         
        if(htIdCore.containsKey(idcl)){  //teste si le joueur est déja associé à une partie
            try {
                this.notifyclient(idcl, "Please wait..the game is starting ");
                 htIdStub.get(idcl).createGui();
                htIdStub.get(idcl).setistartgame(true); // signale que le jeu est deja lancé et demarre la gui
                
            } catch (RemoteException ex) {
                Logger.getLogger(CoreServer.class.getName()).log(Level.SEVERE, null, ex);
            }
       
        }else{
            
                ArrayList playerList;  //cree une liste de joueur
               // playerList.add(0, idcl); //ajoute l'id du joueur dans la liste
                
                try {
                    CurrentCore = new Core(this); //creer un nouveau core
                    this.notifyclient(idcl, "Enter the number of opponents: ");
                    nbreadv = htIdStub.get(idcl).getnumberplay();
                    this.notifyclient(idcl, "number of opponents for this game:  " + nbreadv);
                    
                      

                } catch (RemoteException ex) {
                        Logger.getLogger(CoreServer.class.getName()).log(Level.SEVERE, null, ex);
                  }
                
                if(nbreadv >= 1){  //mode multijoueur
                    
                    this.notifyclient(idcl, "Please enter competitor' s ID "); 
                    playerList = htIdStub.get(idcl).getCompetitors(nbreadv); //obtient la liste des joueurs
                    
                    for (int i = 0; i < playerList.size() ; i++){

                           htIdCore.put((Integer)playerList.get(i), CurrentCore);  //creer une entrée idclient-core dans la table pour les adversaires du cleint
                           CurrentCore.registerForCallback((Integer)playerList.get(i)); //enrégistrer les adversaires du client avec leurs id auprès de du core
                           htIdStub.get((Integer)playerList.get(i)).createGui();//demarre la gui des autres joueurs
                           htIdStub.get((Integer)playerList.get(i)).setistartgame(true); // signale que le jeu est deja lancé 


                     }
      
                }else{ //mode single player
                    
                    this.notifyclient(idcl, "You are going to start a single player mode "); 
 
                           htIdCore.put(idcl, CurrentCore);  //creer une entrée idclient-core dans la table pour les adversaires du cleint
                           CurrentCore.registerForCallback(idcl); //enrégistrer l'id du client  auprès  du core
                           //htIdStub.get(idcl).setistartgame(true);
                }
                


                
                executor.execute(CurrentCore); // lancer une partie dans un thread
                
                 try {
                     this.notifyclient(idcl, "Game Started... ");
                 } catch (RemoteException ex) {
                     Logger.getLogger(CoreServer.class.getName()).log(Level.SEVERE, null, ex);
                 }

                     
          }

          
        return (true);
    
             
    }
         
  
 
// This remote method allows an object client to 
// cancel its registration for callback
// @param id is an ID for the client; to be used by
// the server to uniquely identify the registered client.
  public synchronized void unregister(int callbackClientObject) throws java.rmi.RemoteException{
     try {
               
                 Core retire = htIdCore.remove(callbackClientObject);
                 IGUI gu = htIdStub.remove(callbackClientObject);
                 retire.unregisterForCallback(gu);

                  
            } catch (Exception ex) {
                Logger.getLogger(CoreServer.class.getName()).log(Level.SEVERE, null, ex);
            }
           
  } 



  
   

    public void newGrid(int gamegui) throws RemoteException {
        htIdCore.get(gamegui).newGrid();
    }

  

    public boolean isbGameFinishing(int gamegui) throws RemoteException {
        return htIdCore.get(gamegui).isbGameFinishing();
    }

    public void setbGameFinishing(boolean abGameFinishing, int gamegui) throws RemoteException {
        htIdCore.get(gamegui).setbGameFinishing(abGameFinishing);
    }

    public boolean isbGameInProgress(int gamegui) throws RemoteException {
        return htIdCore.get(gamegui).isbGameInProgress();
    }

    public void setbGameInProgress(boolean abGameInProgress, int gamegui) throws RemoteException {
        htIdCore.get(gamegui).setbGameInProgress(abGameInProgress);
    }

    public boolean isbGameQuit(int gamegui) throws RemoteException {
        return htIdCore.get(gamegui).isbGameQuit();
    }

    public void setbGameQuit(boolean abGameQuit, int gamegui) throws RemoteException {
        htIdCore.get(gamegui).setbGameQuit(abGameQuit);
    }

    public boolean isUP_P(int gamegui) throws RemoteException {
        return htIdCore.get(gamegui).isUP_P();
    }

    
    public void setUP_P(boolean aUP_P, int gamegui) throws RemoteException {
       htIdCore.get(gamegui).setUP_P(aUP_P);
    }

    public boolean isDO_P(int gamegui) throws RemoteException {
        return htIdCore.get(gamegui).isDO_P();
    }

    
    public void setDO_P(boolean aDO_P, int gamegui) throws RemoteException {
         htIdCore.get(gamegui).setDO_P(aDO_P);
    }

    public boolean isRI_P(int gamegui) throws RemoteException {
        return htIdCore.get(gamegui).isRI_P();
    }

    public void setRI_P(boolean aRI_P, int gamegui) throws RemoteException {
         htIdCore.get(gamegui).setRI_P(aRI_P);
    }

    public boolean isLE_P(int gamegui) throws RemoteException {
        return htIdCore.get(gamegui).isLE_P();
    }

    public void setLE_P(boolean aLE_P, int gamegui) throws RemoteException {
        htIdCore.get(gamegui).setLE_P(aLE_P);
    }

    public int getScore(int gamegui) throws RemoteException {
       return htIdCore.get(gamegui).getScore();
    }

    public void setScore(int aScore, int gamegui) throws RemoteException {
        htIdCore.get(gamegui).setScore(aScore);
    }
    
    public void update(int clientid, Vector<Rectangle> vDisplayRoad, Vector<Rectangle> vDisplayObstacles, Vector<Rectangle> vDisplayCars, Car myCar, int pos, int nbParticipants, boolean bGameOver, String sPosition) throws RemoteException{
        
          htIdStub.get(clientid).update(vDisplayRoad, vDisplayObstacles, vDisplayCars, myCar, pos, nbParticipants, bGameOver, sPosition);
    }
    
    public void setbutton(int clientid, boolean etat)throws RemoteException{
        
        htIdStub.get(clientid).setbutton(etat);
    }
    
    public void notifyclient(int clientid, String message)throws RemoteException{
        
        htIdStub.get(clientid).notifymessage(message);
        
    }

}
  
    
    

