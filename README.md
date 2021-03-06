# MudGame
Distributed systems and security.  The Mud is a multiplayer text game made in Java.

How to operate the application.
To compile the code run: ‘make mud’ command from the ‘Assesm’ folder.
Server.
In first terminal window start rmiregistry service to listen on an appropriate port: ‘rmiregistry 50011’ as an example. 
This command produces no output and running at the background.

![Screenshot 1](/screenshots/1.png?raw=true "Screenshot 1")

Open second terminal from ‘Assessm’ folder and start server:					 
‘java cs3524.solutions.mud.MUDServerMainline 50011 50012’
        where 50011 – rmiregistry port and 50012 – the server port.
After getting the message:
Server is running at rmi://<system host name>:50011/MUD
Admin Mode
        
![Screenshot 2](/screenshots/2.png?raw=true "Screenshot 2")

Server is ready for administrator commands:
Only one command ‘create <worldname>’ available in this version. 
This command creates a new mud world server with the name given by server administrator and files associated with this world: 

![Screenshot 3](/screenshots/3.png?raw=true "Screenshot 3")

<worldname>.edg, <worldname>.msg, <worldname>.thg. Amount of mud servers running at the same time is limited to maximum 5.

Client.
To start client, need to open new terminal and run command:
java cs3524.solutions.mud.MUDClient <server host name> 50011
After receiving messages:
Currently running servers: world2, world_g, world1
Please, choose server:

![Screenshot 4](/screenshots/4.png?raw=true "Screenshot 4")

After selecting a server and typing in the username game starts and server code creates the user file (<username>.plr) 
to store inventory items. Server displays username joined the game.

![Screenshot 5](/screenshots/5.png?raw=true "Screenshot 5")

In game commands:
Move <direction> - changes the player location

![Screenshot 6](/screenshots/6.png?raw=true "Screenshot 6")

Whoami? – returns the player their name and world’s name.

![Screenshot 7](/screenshots/7.png?raw=true "Screenshot 7")

Who – returns player names currently in this location.

![Screenshot 8](/screenshots/8.png?raw=true "Screenshot 8")

Inv or inventory– returns inventory collected by player.

![Screenshot 9](/screenshots/9.png?raw=true "Screenshot 9")

Take – removes item from the map and puts it into the players inventory.

![Screenshot 10](/screenshots/10.png?raw=true "Screenshot 10")

Player vvv takes sword before player sss.


Player sss not able to get a sword.

![Screenshot 11](/screenshots/11.png?raw=true "Screenshot 11")

Server – command that gives possibility to player to change the mud world.

![Screenshot 12](/screenshots/12.png?raw=true "Screenshot 12")

Exit or quit – allows client to leave the game.
Client

![Screenshot 13](/screenshots/13.png?raw=true "Screenshot 13")

Server.

![Screenshot 14](/screenshots/14.png?raw=true "Screenshot 14")	
