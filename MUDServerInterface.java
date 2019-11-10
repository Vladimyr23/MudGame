package cs3524.solutions.mud;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface MUDServerInterface extends Remote
{
    
    public String header() throws RemoteException;    
    public String getStartLocation() throws RemoteException;
    public String location(String location) throws RemoteException;
    public String moveDirection(String current, String direction) throws RemoteException;
    public boolean addUser(String username) throws RemoteException;
    public void updateUserLocation(String username, String location) throws RemoteException;
	public void updateUserItems(String username, String item) throws RemoteException;
    public String getPlayersAtLocation() throws RemoteException;
    public boolean takeItem(String item, String location) throws RemoteException;
	public ArrayList<String> getPlayersInventory(String username) throws RemoteException;
    public void changeMUD(String name) throws RemoteException;
    public String getServers() throws RemoteException;
	public void clientQuit(String username) throws RemoteException;

}