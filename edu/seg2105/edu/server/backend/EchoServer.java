package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import edu.seg2105.client.common.ChatIF;
import ocsf.client.AbstractClient;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  ChatIF serverCon;
  AbstractClient client;
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverCon) 
  {
    super(port);
    this.serverCon = serverCon;
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */ 
  
  public void handleMessageFromClient
  (Object msg, ConnectionToClient client)
{
	  
	  
	String input = (String) msg;
  System.out.println("Message received: " + msg + " from " + client.getInfo("loginIDKey"));
  
  if(input.startsWith("#login")) {
		String[] elems = input.split(" ");
		if(client.getInfo("KeyID") == null ) { 
			client.setInfo("KeyID", elems[1]); 
			this.serverCon.display(elems[1] + " has logged in");
		} else if(!client.isAlive()) { 
			
			client.setInfo("KeyID", elems[1]);
			this.serverCon.display(elems[1] + " has logged in");
		}else { 
			try {
				client.sendToClient("Only one login for user. Closing client.");
				client.close();
			} catch (IOException e) {
				System.out.println("Error connection.");
			}
		}
		
	}else {
		 sendToAllClients("SERVER MESSAGE> " + msg + " From "+ client.getInfo("KeyID"));
	}
}
  
  
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  @Override
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
    
    
    //System.out.println("Welcome! You are connected to the server"); 
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
    
    //System.out.println("Server had disconnected");
  }
  
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println("Client connected");
  }
  
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  // tell the server UI that a client disconnected, and who
	  System.out.println(client.getInfo("loginIDKey") + " has disconnected");
  }
  synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
	// tell the server UI that a client disconnected, and who
	  System.out.println(client.getInfo("loginIDKey") + " has disconnected");
  }
  
  
  
  public void handleMessageFromServerConsole(String input) {
	  if(input.startsWith("#")) {
		  String[] inputs = input.split(" ");
		  String function = inputs[0];
		  switch (function) {
		  
		  case "#quit":
			  try {
			  this.close();
			  } catch(IOException e) {
				  System.exit(1);
			  }
			  System.exit(0);
			  break;
		  case "#stop":
			  this.stopListening();
			  break;
		  case "#close":
			  try {
			  this.close();
			  } catch(IOException e) {
				  System.out.println("Error for closing");
			  }
			  break;
		  case "#setport":
			  if(this.isListening()) {
				  System.out.println("Server should be closed");
			  } else {
				  this.setPort(Integer.parseInt(inputs[1]));
			  }
			  break;
		  case "#start":
			  if(this.isListening()) {
				  System.out.println("Already listening!");
			  } else {
				  try {
					  this.listen();
				  } catch(IOException e) {
					  System.out.println("Failed to start listening");
				  }
			  }
			  break;
		  case "#getport":
			  System.out.println("Port: " + this.getPort());
			  break;
			  
		  default: {
		  		System.out.println("Can't interpret it. Please check spelling.");
		  		break;
		  	}
		  
		  }
	  } else {
		  try {
		  if(this.client != null) {
    		  client.sendToServer(input);
    	  }
    	  // also display in server
    	  serverCon.display(input);
		  } catch(IOException e) {
			  serverCon.display("Can't sent the message");
		  }
	  }
  }
  
  
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  } */
}
//End of EchoServer class
