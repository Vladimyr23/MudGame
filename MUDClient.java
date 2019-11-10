package cs3524.solutions.mud;

import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;

import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.InetAddress;
import java.util.Iterator;


public class MUDClient {
//Class to arrange a client to login and interact with server

	//Arranging interaction with server through the server interface and name it service
    static MUDServerInterface service;
	//Streaming prameters from command line
    static BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));

	//Client parameters defenition
    private static String username;
    private static String location;
	private List<String> inv;
	//server name if none pointed 
    private static String servername = "world1";
    
    
    public static void main(String args[]) throws Exception{
        //Check if all needed arguments passed in
        if(args.length < 2){
            System.err.println("Missing arguments <host> <port>");
            return;
        } 
        
        // Arguments assignment
        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
        
        // Setup Security Manager
        System.setProperty("java.security.policy", "mud.policy");        
        if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager()) ;
		}
        try {            
            String regURL = "rmi://" + hostname + ":" + port + "/MUD";
            service = (MUDServerInterface)Naming.lookup(regURL);
            //Choosing the server
            setup();           
        } 
        catch (java.io.IOException e) {
            System.err.println( "I/O error." );
            System.err.println( e.getMessage() );
        }
        catch (java.rmi.NotBoundException e) {
            System.err.println( "Server not bound." );
            System.err.println( e.getMessage() );
        }
        
    }
    
    static void setup() throws Exception {
		//Choosing server
        System.out.println(service.getServers());            
        System.out.println("Please, choose server:");
        servername = in.readLine();
        service.changeMUD(servername);

        System.out.println(service.header());
        username = in.readLine();
        location = service.getStartLocation();

		//Check if the max amount of clients exceeded
        if(service.addUser(username)){
                        systemStarted();
        } else {
            System.out.println("Sorry - this server is currently busy. Please try again later");
        }
    }
    
    
    static void systemStarted() throws Exception{
        //Game interaction with server arrangement

        boolean accepting = true;        
        location(location);
        
        try {
        
            while(accepting){
                String input = in.readLine();    
                service.changeMUD(servername);

                if(input.equals("quit") || input.equals("exit")){
				//Exit command
					service.clientQuit(username);
                    accepting = false;

                } else if(input.equals("whoami?")){
				//Username command
                    System.out.println(username + " in the " + servername + " world.\n");

                } else if(input.toLowerCase().contains("move")){
				//Move command
                    String moving[] = input.split(" ");
                    String direction = moving[1];                    
                    if(direction.equalsIgnoreCase("north") || direction.equalsIgnoreCase("east") || direction.equalsIgnoreCase("south") || direction.equalsIgnoreCase("west")){
					//Move direction
                       String newlocation = service.moveDirection(location, direction);
                       if(newlocation.equals(location)){
                           System.out.println("Can not move " + direction);
                       } else {
                           location = newlocation;
                           location(location);
                           service.updateUserLocation(username, location);
                       }
                    } else {
                        System.out.println("Unknown Direction " + direction);
                    }
                    
                } else if(input.equalsIgnoreCase("who")){
				//Command returns all users currently in the MUD
                    System.out.println(service.getPlayersAtLocation() + "\n");

				} else if(input.equals("inv") || input.equals("inventory")){
				//Command returns items collected by user
                    ArrayList<String> items = service.getPlayersInventory(username);
					boolean retval = items.isEmpty();
					if (retval == true) {
					//No items in possession
						System.out.println("inventory is empty \n");
					} else {
						// printing all the elements available in list
					  for (String line : items) {
						 System.out.println(line);
					  }
					}   				
								
                } else if(input.contains("take")){
					//Item picking up
                    String splt[] = input.split(" ");
                    String item = splt[1];                    
                    if(service.takeItem(item, location)){
						service.updateUserItems(username, item);
                        System.out.println("You've got the " + item+"\n");
                    } else {
                        System.out.println("Can not take the " + item+"\n");
                    }

                } else if(input.contains("server")){
					//Server change
                    service.clientQuit(username);
					setup();
                }
            }
			
        } catch(Exception e){
            return;
        }
        
    }    
    static void location(String locationname) throws Exception{        
        System.out.println(service.location(locationname));        
    }    
}