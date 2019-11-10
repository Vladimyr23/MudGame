package cs3524.solutions.mud;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.rmi.Naming;

import java.rmi.server.UnicastRemoteObject;


public class MUDServerMainline {
//Server MainLine class
    
    static BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));

    
    public static void main(String args[]){
        
        if(args.length < 2){
            System.err.println("You must provide two arguments: <regport> <serverport>");
            return;
        } 
        
        int registryPort = Integer.parseInt(args[0]);
        int serverPort = Integer.parseInt(args[1]);
   
        System.out.println("Attempting to start server running on port " + Integer.toString(registryPort));
        
        try {
			//Getting Local host name from system        
            String hostname = (InetAddress.getLocalHost()).getCanonicalHostName();            
            // Setup Security Manager
            System.setProperty("java.security.policy", "mud.policy");            
			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new SecurityManager()) ;
			}

            // Generate the remote objects
            MUDServerImpl mudserver = new MUDServerImpl();
            MUDServerInterface mudstub = (MUDServerInterface)UnicastRemoteObject.exportObject(mudserver, serverPort);
            
            String regURL = "rmi://" + hostname + ":" + registryPort + "/MUD";
            
            try {
            Naming.rebind(regURL, mudstub);
            
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
            System.out.println("Server is running at "+regURL);
            System.out.println("Admin Mode");
            
            
            while(true){
                
                String input = in.readLine();
                
                if(input.contains("create")){
				//Creating new MUD world
                    String[] param = input.split(" ");					
                    
                    if(mudserver.Servers.size() < 5){
					//Limiting servers to max 5

						FileInputStream ins = null;
      					FileOutputStream outs = null;
						try {
		                    System.out.println("Creating the mud with the name " + param[1]);
							File my_edge 		= new File ("mymud.edg");
							File my_messages 	= new File ("mymud.msg");
							File my_thing 		= new File ("mymud.thg");

							File edge 		= new File (param[1] + ".edg");
							File messages 	= new File (param[1] + ".msg");
							File thing 		= new File (param[1] + ".thg");

							// Creating edge file
							if (!edge.exists()) {
								edge.createNewFile();
							}
							ins = new FileInputStream(my_edge);
							outs = new FileOutputStream(edge);
							byte[] buffer = new byte[1024];
							int length;
							 
							while ((length = ins.read(buffer)) > 0) {
								outs.write(buffer, 0, length);
							} 
							ins.close();
							outs.close();
							System.out.println(param[1] + ".edg" + " File created successfully!!");

							// Creating message file
							if (!messages.exists()) {
								messages.createNewFile();
							}
							ins = new FileInputStream(my_messages);
							outs = new FileOutputStream(messages);
							 
							while ((length = ins.read(buffer)) > 0) {
								outs.write(buffer, 0, length);
							} 
							ins.close();
							outs.close();
							System.out.println(param[1] + ".msg" + " File created successfully!!");
							// Creating things file
							if (!thing.exists()) {
								thing.createNewFile();
							}
							ins = new FileInputStream(my_thing);
							outs = new FileOutputStream(thing);
							 
							while ((length = ins.read(buffer)) > 0) {
								outs.write(buffer, 0, length);
							} 
							ins.close();
							outs.close();
							System.out.println(param[1] + ".thg" + " File created successfully!!");
						} catch(IOException ioe) {
         					ioe.printStackTrace();
      					}

                        MUD newmud = new MUD(param[1] + ".edg",param[1] + ".msg",param[1] + ".thg");
                        mudserver.Servers.put(param[1], newmud);
                    } else {
					//Max 5 servers message
                        System.out.println("Sorry, you can only have 5 MUD's running at the same time");
                    }
                }
                
                
            }
            
            
        } 
        catch (java.net.UnknownHostException e) {
            System.err.println("Can not get local host name.");
        }
        catch (java.io.IOException e){
            System.err.println("Failed to register.");
        }
        
    }
    
}