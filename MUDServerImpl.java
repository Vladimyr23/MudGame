package cs3524.solutions.mud;

import java.io.*;

import java.rmi.*; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class MUDServerImpl implements MUDServerInterface {
//Mud server implementation class which creates a remote object trough the remote interface

    //Creating MUD maps and default worlds
    private MUD mudmap; 
    public Map<String, MUD> Servers = new HashMap<String, MUD>();
    
    public MUDServerImpl() throws RemoteException
    { 
	//Creating default worlds servers world1 and world2
        Servers.put("world1", new MUD("mymud.edg","mymud.msg","mymud.thg"));
        Servers.put("world2", new MUD("mymud.edg","mymud.msg","mymud.thg"));
    }
    
    public String header() throws RemoteException
    {
	//Method to print out welcome message and get the username

        if(mudmap==null){
			//Gets world1 server if none
            mudmap = Servers.get("world1");
        }
        String output = ( "================================== \n \n ***Welcome to the MUD Server!*** \n \n================================== \n" );
		//Username 
        output += "Please enter your in game name: ";
        return output;        
    }
    
    public String getStartLocation() throws RemoteException {
	//method to get players start location in a mud

        return mudmap.startLocation();
    }
    
    
    public String location(String location) throws RemoteException{
    //method to get current location
    
        return mudmap.getVertex(location).toString();        
    }
    
    public String moveDirection(String current, String direction) throws RemoteException{
	//method to move players (location change)

        Vertex currentVertex = mudmap.getVertex(current);
        if(currentVertex._routes.containsKey(direction)){
            Edge newLocation = currentVertex._routes.get(direction);
            Vertex newVert = (newLocation._dest);            
        	return newVert._name;
        } else {
            return current;
        }
    }

    public boolean addUser(String username) throws RemoteException {
    //method to add user and their inventory file  
 
        if(mudmap.users.size() < 10){
		//max 10 players alowed
            mudmap.users.put(username, mudmap.startLocation());
			System.out.println("Player " + username + " joined the game");
			try {				
				File userFile = new File (username + ".plr");
				userFile.createNewFile();			
		        return true;
			}
        	catch(IOException ex) {
	            System.out.println("Error writing to file '" + username + ".plr" + "'");	            
				return false;
	        }
        } else {
			System.out.println("10 Players are already playing");
            return false;
        }
    }
    
	public void clientQuit (String username) throws RemoteException{
	//Method to quit client from server
		System.out.println("Client '" + username + "' left the game.");
		mudmap.users.remove(username);
			
	}
    public void updateUserLocation(String username, String location) throws RemoteException {
    //method to update users location (move)
    
        mudmap.users.remove(username);
        mudmap.users.put(username, location);    
    }

    public void updateUserItems(String username, String item) throws RemoteException {
    //Method to update users inventory

        mudmap.users.put(username, item);
    	// The name of the file to open.
        String fileName = username + ".plr";

        try {
            // Assume default encoding.
            FileWriter fileWriter =
                new FileWriter(fileName, true);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter =
                new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            bufferedWriter.write(item + "\n ");            

            // Always close files.
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '"
                + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
    }
	public ArrayList<String> getPlayersInventory(String username) throws RemoteException {
 	//Reading inventory from user file

		// This will reference one line at a time
        String line = null;
		ArrayList<String> items = new ArrayList<String>();
		try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(username + ".plr");

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                //System.out.println(line);
				items.add(line);
            }   

            // Always close files.
            bufferedReader.close();
			         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                username + ".plr" + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + username + ".plr" + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
		return items;
	}

    public String getPlayersAtLocation() throws RemoteException{
    //Method to get all users names currently using this mud
    
        ArrayList<String> Players = new ArrayList<String>();
        String username;        
        StringBuilder sb = new StringBuilder();        
        Iterator itter = mudmap.users.keySet().iterator();        
        while (itter.hasNext()) {
	    username = itter.next().toString();
            //if(mudmap.users.get(username).equalsIgnoreCase(location)){
                Players.add(username);
                sb.append(username);
                sb.append(", ");
            //}                        
	} 
        sb.setLength(sb.length() - 2);        
        return "Currently on this map you can see: " + sb.toString();        
    }
    
    public boolean takeItem(String item, String location) throws RemoteException {
	//Method to pick up the item

        Vertex currentVertex = mudmap.getVertex(location);
        List<String> things = currentVertex._things;        
        if(things.contains(item)){
            mudmap.delThing(location, item);            
            return true;
        }        
        return false;
    }
    
    public void changeMUD(String name) throws RemoteException {
        mudmap = Servers.get(name);        
    }
    
    public String getServers() throws RemoteException{
    //Method to get currently running servers names
        StringBuilder sb = new StringBuilder();
        Iterator it = Servers.keySet().iterator();
        
        while(it.hasNext()){
            sb.append(it.next().toString());
            sb.append(", ");
        }
        
        sb.setLength(sb.length() - 2);
        
        return "Currently running servers: " + sb.toString();
    }    
    
}